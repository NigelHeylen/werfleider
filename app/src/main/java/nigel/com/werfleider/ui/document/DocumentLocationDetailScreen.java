package nigel.com.werfleider.ui.document;

import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.document.DocumentLocatieDbHelper;
import nigel.com.werfleider.model.CreateImage;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocatie;
import nigel.com.werfleider.model.Werf;
import rx.functions.Action0;

import static java.lang.String.format;

/**
 * Created by nigel on 17/12/14.
 */
@Layout(R.layout.plaatsbeschrijf_location_detail_view)
public class DocumentLocationDetailScreen implements Blueprint, HasParent<DocumentScreen> {

    private final Document document;
    private final int locationIndex;
    private final Werf werf;

    public DocumentLocationDetailScreen(final Document document, final int locationIndex, final Werf werf) {
        this.document = document;
        this.locationIndex = locationIndex;
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return format("%s document: werf id %s, %s, location: %s",
                      getClass().getName(),
                      werf.getId(),
                      document.getDocumentType().name().toLowerCase(),
                      document.getFotoReeksList().get(locationIndex).getLocation());
    }

    @Override public DocumentScreen getParent() {
        return new DocumentScreen(werf, document.getDocumentType());
    }

    @Override public Object getDaggerModule() {
        return new Module(document, locationIndex, werf);
    }

    @dagger.Module(injects = DocumentLocationDetailView.class, addsTo = CorePresenter.Module.class)
    static class Module {

        final Document document;

        final int locationIndex;
        private final Werf werf;

        Module(final Document document, final int locationIndex, final Werf werf) {
            this.document = document;
            this.locationIndex = locationIndex;
            this.werf = werf;
        }

        @Provides Werf provideWerf(){
            return werf;
        }

        @Provides @Singleton Document providePlaatsBeschrijf() {
            return document;
        }

        @Provides int provideLocation(){
            return locationIndex;
        }

    }


    public static class Presenter extends ViewPresenter<DocumentLocationDetailView> {

        @Inject Document document;

        @Inject int locationIndex;

        @Inject ActionBarOwner actionBarOwner;

        @Inject DocumentLocatieDbHelper documentLocatieDbHelper;

        @Inject
        public Presenter(final Bus bus) {
            bus.register(this);
        }

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            DocumentLocationDetailView view = getView();
            if (view == null) {
                return;
            }

            initActionBar();

            view.initAdapter(document, locationIndex);
        }


        private void initActionBar() {
            ActionBarOwner.MenuAction menu = new ActionBarOwner.MenuAction(
                    "Save", new Action0() {
                @Override public void call() {
                    documentLocatieDbHelper
                            .updatePlaatsBeschrijfLocatie(getPlaatsBeschrijfLocatie());


                }
            });

            actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, document.getFotoReeksList().get(locationIndex).getLocation(), menu));
        }

        private DocumentLocatie getPlaatsBeschrijfLocatie() {
            return document.getFotoReeksList().get(locationIndex);
        }

        @Subscribe
        public void reactToImageCreated(final CreateImage createImage) {
            document
                    .getFotoReeksList()
                    .get(createImage.getImageLocationIndex())
                    .addToImageList(
                            new DocumentImage()
                                    .setImageURL(createImage.getCurrentUri()));

        }
    }
}
