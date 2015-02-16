package nigel.com.werfleider.ui.plaatsbeschrijf;

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
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieDbHelper;
import nigel.com.werfleider.model.CreateImage;
import nigel.com.werfleider.model.PlaatsBeschrijf;
import nigel.com.werfleider.model.PlaatsBeschrijfImage;
import nigel.com.werfleider.model.PlaatsBeschrijfLocatie;
import nigel.com.werfleider.model.Werf;
import rx.functions.Action0;

import static java.lang.String.format;

/**
 * Created by nigel on 17/12/14.
 */
@Layout(R.layout.plaatsbeschrijf_location_detail_view)
public class PlaatsBeschrijfLocationDetailScreen implements Blueprint, HasParent<PlaatsBeschrijfScreen> {

    private final PlaatsBeschrijf plaatsBeschrijf;
    private final int locationIndex;
    private final Werf werf;

    public PlaatsBeschrijfLocationDetailScreen(final PlaatsBeschrijf plaatsBeschrijf, final int locationIndex, final Werf werf) {
        this.plaatsBeschrijf = plaatsBeschrijf;
        this.locationIndex = locationIndex;
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return format("%s plaatsbeschrijf: %s, location: %s",
                      getClass().getName(),
                      plaatsBeschrijf.getTitle(),
                      plaatsBeschrijf.getFotoReeksList().get(locationIndex).getLocation());
    }

    @Override public PlaatsBeschrijfScreen getParent() {
        return new PlaatsBeschrijfScreen(werf);
    }

    @Override public Object getDaggerModule() {
        return new Module(plaatsBeschrijf, locationIndex, werf);
    }

    @dagger.Module(injects = PlaatsBeschrijfLocationDetailView.class, addsTo = CorePresenter.Module.class)
    static class Module {

        final PlaatsBeschrijf plaatsBeschrijf;

        final int locationIndex;
        private final Werf werf;

        Module(final PlaatsBeschrijf plaatsBeschrijf, final int locationIndex, final Werf werf) {
            this.plaatsBeschrijf = plaatsBeschrijf;
            this.locationIndex = locationIndex;
            this.werf = werf;
        }

        @Provides Werf provideWerf(){
            return werf;
        }

        @Provides @Singleton PlaatsBeschrijf providePlaatsBeschrijf() {
            return plaatsBeschrijf;
        }

        @Provides int provideLocation(){
            return locationIndex;
        }

    }


    public static class Presenter extends ViewPresenter<PlaatsBeschrijfLocationDetailView> {

        @Inject PlaatsBeschrijf plaatsBeschrijf;

        @Inject int locationIndex;

        @Inject ActionBarOwner actionBarOwner;

        @Inject PlaatsBeschrijfLocatieDbHelper plaatsBeschrijfLocatieDbHelper;

        @Inject
        public Presenter(final Bus bus) {
            bus.register(this);
        }

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            PlaatsBeschrijfLocationDetailView view = getView();
            if (view == null) {
                return;
            }

            initActionBar();

            view.initAdapter(plaatsBeschrijf, locationIndex);
        }


        private void initActionBar() {
            ActionBarOwner.MenuAction menu = new ActionBarOwner.MenuAction(
                    "Save", new Action0() {
                @Override public void call() {
                    plaatsBeschrijfLocatieDbHelper
                            .updatePlaatsBeschrijfLocatie(getPlaatsBeschrijfLocatie());


                }
            });

            actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, plaatsBeschrijf.getFotoReeksList().get(locationIndex).getLocation(), menu));
        }

        private PlaatsBeschrijfLocatie getPlaatsBeschrijfLocatie() {
            return plaatsBeschrijf.getFotoReeksList().get(locationIndex);
        }

        @Subscribe
        public void reactToImageCreated(final CreateImage createImage) {
            plaatsBeschrijf
                    .getFotoReeksList()
                    .get(createImage.getImageLocationIndex())
                    .addToImageList(
                            new PlaatsBeschrijfImage()
                                    .setImageURL(createImage.getCurrentUri()));

        }
    }
}
