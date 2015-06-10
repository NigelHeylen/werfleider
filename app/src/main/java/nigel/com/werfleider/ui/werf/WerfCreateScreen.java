package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.joda.time.DateTime;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.werf.WerfDbHelper;
import nigel.com.werfleider.dao.werf.WerfDbHelperBean;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.model.Werf;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

import static nigel.com.werfleider.util.ParseStringUtils.NAME;

/**
 * Created by nigel on 07/02/15.
 */
@Layout(R.layout.werf_create_view)
public class WerfCreateScreen implements Blueprint, HasParent<WerfScreen> {

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

    @Override public WerfScreen getParent() {
        return new WerfScreen();
    }

    @dagger.Module(
            injects = WerfCreateView.class,
            addsTo =
                    CorePresenter.Module.class

    )
    static class Module {

        @Provides WerfDbHelper provideWerfHelper(final WerfDbHelperBean werfHelper) {

            return werfHelper;
        }
    }

    static class Presenter extends ViewPresenter<WerfCreateView> {

        @Inject WerfDbHelper db;

        @Inject Flow flow;

        @Inject Context context;

        public void create(
                final String naam,
                final String nummer,
                final String opdrachtAdres,
                final String opdrachtStad,
                final String ontwerper,
                final String ontwerperAdres,
                final String ontwerperStad,
                final String opdrachtgever,
                final String opdrachtgeverAdres,
                final String opdrachtgeverStad,
                final String omschrijving,
                final DateTime datumAanvang) {

            final ParseYard parseYard = new ParseYard();

            parseYard.setNaam(naam)
                    .setNummer(nummer)
                    .setOpdrachtAdres(opdrachtAdres)
                    .setOpdrachtStad(opdrachtStad)
                    .setOntwerper(ontwerper)
                    .setOntwerperAdres(ontwerperAdres)
                    .setOntwerperStad(ontwerperStad)
                    .setOpdrachtgever(opdrachtgever)
                    .setOpdrachtgeverAdres(opdrachtgeverAdres)
                    .setOpdrachtgeverStad(opdrachtgeverStad)
                    .setOmschrijving(omschrijving)
                    .setDatumAanvang(datumAanvang)
                    .setCreator(ParseUser.getCurrentUser().getString(NAME))
                    .setAuthor(ParseUser.getCurrentUser());

            parseYard.saveEventually(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {
                                Toast.makeText(context, "Werf " + naam + " saved.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });


            Observable
                    .just(
                            new Werf(
                                    naam,
                                    nummer,
                                    opdrachtAdres,
                                    opdrachtStad,
                                    ontwerper,
                                    ontwerperStad,
                                    ontwerperAdres,
                                    opdrachtgever,
                                    opdrachtgeverAdres,
                                    opdrachtgeverStad,
                                    omschrijving,
                                    datumAanvang))
                    .doOnTerminate(
                            new Action0() {
                                @Override public void call() {
                                    db.closeDB();
                                }
                            })
                    .subscribe(
                            new Observer<Werf>() {
                                @Override public void onCompleted() {

                                    flow.goTo(new WerfScreen());
                                }

                                @Override public void onError(final Throwable e) {

                                    e.printStackTrace();
                                }

                                @Override public void onNext(final Werf werf) {

//                                    db.createWerf(werf);
                                }
                            });

        }
    }
}
