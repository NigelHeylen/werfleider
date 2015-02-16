package nigel.com.werfleider.ui.plaatsbeschrijf;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.core.MainScope;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfDbHelper;
import nigel.com.werfleider.model.PlaatsBeschrijf;
import nigel.com.werfleider.model.PlaatsBeschrijfLocatie;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.pdf.FileOperations;
import nigel.com.werfleider.ui.werfoverzicht.WerfDetailScreen;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

/**
 * Created by nigel on 25/11/14.
 */
@Layout(R.layout.plaatsbeschrijf_view)
public class PlaatsBeschrijfScreen implements Blueprint, HasParent<WerfDetailScreen> {

    private final Werf werf;

    public PlaatsBeschrijfScreen(final Werf werf) {

        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module(werf);
    }

    @Override public WerfDetailScreen getParent() {
        return new WerfDetailScreen(werf);
    }

    @dagger.Module(injects = {
            PlaatsBeschrijfView.class,
            PlaatsBeschrijfLocationAdapter.class
    }, addsTo = CorePresenter.Module.class)
    static class Module {

        private final Werf werf;

        public Module(final Werf werf) {

            this.werf = werf;
        }

        @Provides Werf provideWerf() {
            return werf;
        }


        @Provides @Singleton PlaatsBeschrijf providePlaatsBeschrijf(final PlaatsBeschrijfDbHelper plaatsBeschrijfDbHelper) {
            return plaatsBeschrijfDbHelper.getPlaatsBeschrijf(werf.getId());
        }

        @Provides FileOperations provideFileOperations(final Context context) {
            return new FileOperations(context);
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<PlaatsBeschrijfView> {

        @Inject FileOperations fop;

        @Inject PlaatsBeschrijf plaatsBeschrijf;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject Werf werf;

        @Inject PlaatsBeschrijfDbHelper plaatsBeschrijfDbHelper;

        @Override public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            final PlaatsBeschrijfView view = getView();
            if (view == null) {
                return;
            }


            ActionBarOwner.MenuAction menu =
                    new ActionBarOwner.MenuAction(
                            "Save", new Action0() {
                        @Override public void call() {
                            view.savePlaatscbeschrijf();
                        }
                    });
            actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, "Plaatsbeschrijf", menu));

            initLocationNumber();

            initTextFields();

        }

        private void initTextFields() {
            getView().initView(plaatsBeschrijf);
        }

        private void initLocationNumber() {
            getView().showLocationNumber(Integer.toString(plaatsBeschrijf.getFotoReeksList().size()));
        }

        public void newImageCollection() {
            flow.goTo(new PictureGridScreen(plaatsBeschrijf, new PlaatsBeschrijfLocatie(""), werf));
        }

        public void write() {
            fop.write(plaatsBeschrijf.getOpdrachtLocatie(), plaatsBeschrijf);
            if (fop.write(plaatsBeschrijf.getOpdrachtLocatie(), plaatsBeschrijf)) {
                getView().showToast(plaatsBeschrijf.getOpdrachtLocatie() + ".pdf created");
            } else {
                getView().showToast("I/O error");
            }

        }

        public void read(final String filename) {
            String text = fop.read(filename);
            if (text != null) {
                getView().showPdfText(text);
            } else {
                getView().showToast("File not Found");
                getView().showPdfText(null);
            }
        }

        public void savePlaatsbeschrijf(
                final String ontwerper,
                final String ontwerperAdres,
                final String ontwerperLocatie,
                final String opdrachtgever,
                final String opdrachtLocatie) {
            plaatsBeschrijf.setOntwerper(ontwerper);
            plaatsBeschrijf.setOntwerperAdres(ontwerperAdres);
            plaatsBeschrijf.setOntwerperStad(ontwerperLocatie);
            plaatsBeschrijf.setOpdrachtgever(opdrachtgever);
            plaatsBeschrijf.setOpdrachtLocatie(opdrachtLocatie);

            Observable.just(plaatsBeschrijfDbHelper.updatePlaatsBeschrijf(plaatsBeschrijf))
                      .subscribe(
                              new Observer<Integer>() {
                                  @Override public void onCompleted() {
                                      Toast.makeText(getView().getContext(), "Plaatsbeschrijf saved", Toast.LENGTH_LONG).show();
                                  }

                                  @Override public void onError(final Throwable e) {
                                      e.printStackTrace();
                                  }

                                  @Override public void onNext(final Integer integer) {

                                  }
                              });
        }
    }
}
