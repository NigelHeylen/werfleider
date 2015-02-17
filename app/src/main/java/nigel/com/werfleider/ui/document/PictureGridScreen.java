package nigel.com.werfleider.ui.document;

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
import nigel.com.werfleider.dao.document.DocumentLocatieDbHelper;
import nigel.com.werfleider.dao.document.DocumentLocatieImageDbHelper;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocatie;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.widget.ImageFileFilter;
import rx.functions.Action0;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 03/12/14.
 */
@Layout(R.layout.picture_grid)
public class PictureGridScreen implements Blueprint, HasParent<DocumentLocationDetailScreen> {

    private final Document document;
    private final DocumentLocatie collection;
    private final Werf werf;

    public PictureGridScreen(
            final Document document,
            final DocumentLocatie collection,
            final Werf werf) {
        this.document = document;

        this.collection = collection;
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module(document, collection);
    }

    @Override public DocumentLocationDetailScreen getParent() {
        return new DocumentLocationDetailScreen(document, document.getFotoReeksList().indexOf(collection), werf);
    }

    @dagger.Module(injects = PictureGridView.class, addsTo = CorePresenter.Module.class)
    static class Module {

        private final Document document;
        private final DocumentLocatie collection;

        public Module(final Document document, final DocumentLocatie collection) {

            this.document = document;
            this.collection = collection;
        }

        @Provides Document providePlaatsBeschrijf(){ return document; }

        @Provides DocumentLocatie provideCollection(){ return collection; }


    }

    public static class Presenter extends ViewPresenter<PictureGridView> {
        @Inject DocumentLocatie collection;

        @Inject Document document;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject DocumentLocatieImageDbHelper documentLocatieImageDbHelper;

        @Inject DocumentLocatieDbHelper documentLocatieDbHelper;

        private List<DocumentImage> images;

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
            if(!document.getFotoReeksList().contains(collection)) {
                document.add(collection);
                documentLocatieDbHelper.createDocumentLocatie(collection, document.getId());
            } else {
                documentLocatieDbHelper.updatePlaatsBeschrijfLocatie(collection);
            }

            documentLocatieDbHelper.closeDB();

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
                    images.add(0, new DocumentImage().setImageURL(aFile.getAbsolutePath()));


                }
            }
        }

        public void editLocation(final String s) {
            collection.setLocation(s);
        }
    }


}
