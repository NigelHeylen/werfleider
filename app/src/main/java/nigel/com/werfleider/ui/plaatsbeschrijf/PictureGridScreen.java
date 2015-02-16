package nigel.com.werfleider.ui.plaatsbeschrijf;

import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieDbHelper;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfLocatieImageDbHelper;
import nigel.com.werfleider.model.PlaatsBeschrijf;
import nigel.com.werfleider.model.PlaatsBeschrijfImage;
import nigel.com.werfleider.model.PlaatsBeschrijfLocatie;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.widget.ImageFileFilter;
import rx.functions.Action0;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 03/12/14.
 */
@Layout(R.layout.picture_grid)
public class PictureGridScreen implements Blueprint, HasParent<PlaatsBeschrijfLocationDetailScreen> {

    private final PlaatsBeschrijf plaatsBeschrijf;
    private final PlaatsBeschrijfLocatie collection;
    private final Werf werf;

    public PictureGridScreen(
            final PlaatsBeschrijf plaatsBeschrijf,
            final PlaatsBeschrijfLocatie collection,
            final Werf werf) {
        this.plaatsBeschrijf = plaatsBeschrijf;

        this.collection = collection;
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module(plaatsBeschrijf, collection);
    }

    @Override public PlaatsBeschrijfLocationDetailScreen getParent() {
        return new PlaatsBeschrijfLocationDetailScreen(plaatsBeschrijf, plaatsBeschrijf.getFotoReeksList().indexOf(collection), werf);
    }

    @dagger.Module(injects = PictureGridView.class, addsTo = CorePresenter.Module.class)
    static class Module {

        private final PlaatsBeschrijf plaatsBeschrijf;
        private final PlaatsBeschrijfLocatie collection;

        public Module(final PlaatsBeschrijf plaatsBeschrijf, final PlaatsBeschrijfLocatie collection) {

            this.plaatsBeschrijf = plaatsBeschrijf;
            this.collection = collection;
        }

        @Provides PlaatsBeschrijf providePlaatsBeschrijf(){ return plaatsBeschrijf; }

        @Provides PlaatsBeschrijfLocatie provideCollection(){ return collection; }


    }

    public static class Presenter extends ViewPresenter<PictureGridView> {
        @Inject PlaatsBeschrijfLocatie collection;

        @Inject PlaatsBeschrijf plaatsBeschrijf;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject PlaatsBeschrijfLocatieImageDbHelper plaatsBeschrijfLocatieImageDbHelper;

        @Inject PlaatsBeschrijfLocatieDbHelper plaatsBeschrijfLocatieDbHelper;

        private List<PlaatsBeschrijfImage> images;

        private ArrayList<Integer> indices;

        private static final String INDICES = "INDICES";

        public Presenter() {
            images = newArrayList();
            indices = newArrayList();

        }

        private void initActionBar() {
            ActionBarOwner.MenuAction menu = new ActionBarOwner.MenuAction(
                    "Save", new Action0() {
                @Override public void call() {
                    saveImages();
                    saveCollection();
                    flow.goBack();
                }
            });

            actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, "Picture Grid", menu));
        }

        private void saveImages() {
            collection.getImageList().clear();
            for (Integer indice : indices) {
                collection.addToImageList(images.get(indice));
            }
        }

        private void saveCollection() {
            if(!plaatsBeschrijf.getFotoReeksList().contains(collection)) {
                plaatsBeschrijf.add(collection);
                plaatsBeschrijfLocatieDbHelper.createPlaatsBeschrijfLocatie(collection, plaatsBeschrijf.getId());
            } else {
                plaatsBeschrijfLocatieDbHelper.updatePlaatsBeschrijfLocatie(collection);
            }

            plaatsBeschrijfLocatieDbHelper.closeDB();

        }

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            if (savedInstanceState != null && savedInstanceState.containsKey(INDICES)) {

                indices.addAll(savedInstanceState.getIntegerArrayList(INDICES));

            }

            getFromSdcard();

            getView().initAdapter(images, indices, collection.getImageList());

            initActionBar();

            initTitle();

        }

        private void initTitle() {
            getView().setLocation(collection.getLocation());
        }

        @Override protected void onSave(final Bundle outState) {
            outState.putIntegerArrayList(INDICES, indices);
        }

        public void getFromSdcard() {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");

            if (file.isDirectory()) {
                for (File aFile : file.listFiles(new ImageFileFilter())) {
                    images.add(0, new PlaatsBeschrijfImage().setImageURL(aFile.getAbsolutePath()));


                }
            }
        }

        public void editLocation(final String s) {
            collection.setLocation(s);
        }
    }


}
