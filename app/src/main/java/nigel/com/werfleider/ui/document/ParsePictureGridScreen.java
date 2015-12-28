package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.core.MainScope;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.widget.HeaderViewRecyclerAdapter;
import nigel.com.werfleider.ui.widget.ImageFileFilter;

import static android.view.View.GONE;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static nigel.com.werfleider.util.ImageUtils.getParseFile;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsepicture_grid)
public class ParsePictureGridScreen implements Blueprint, HasParent<Blueprint> {

    private final ParseDocument document;

    private final ParseDocumentLocation location;

    private final Yard yard;

    public ParsePictureGridScreen(
            final ParseDocument document,
            final ParseDocumentLocation location,
            final Yard yard) {

        this.document = document;

        this.location = location;
        this.yard = yard;
    }

    @Override public String getMortarScopeName() {

        return getClass().getName();
    }

    @Override public Object getDaggerModule() {

        return new Module(
                document,
                location,
                yard);
    }

    @Override public Blueprint getParent() {

        return new LocationDetailScreen(
                document,
                yard,
                location);
    }

    @dagger.Module(
            injects = {
                    ParsePictureGridView.class,
                    ParsePictureGridAdapter.class
            },
            addsTo = CorePresenter.Module.class
    )
    static class Module {

        private final ParseDocument document;

        private final ParseDocumentLocation location;

        private final Yard yard;

        public Module(final ParseDocument document, final ParseDocumentLocation location, final Yard yard) {


            this.document = document;
            this.location = location;
            this.yard = yard;
        }

        @Provides @Singleton ParseDocument provideDocument() {

            return document;
        }

        @Provides @Singleton ParseDocumentLocation provideCollection() {

            return location;
        }

        @Provides @Singleton Yard provideYard() {

            return yard;
        }

        @Provides @Singleton List<DocumentImage> provideImages() {

            return newArrayList();
        }

        @Provides @Singleton List<Integer> provideIndices() {

            return newArrayList();
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<ParsePictureGridView> {

        @Inject ParseDocumentLocation location;

        @Inject ParseDocument document;

        @Inject Yard yard;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject List<DocumentImage> images;

        @Inject List<Integer> indices;

        private MaterialEditText locationTitle;

        private MaterialEditText locationArtNr;

        private HeaderViewRecyclerAdapter adapter;

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);

            if (getView() == null) {
                return;
            }

            getFromSdcard();

            initAdapter();

            initLocationEditText();

            initActionBar();

            initTextFields();

        }

        public void initAdapter() {

            getView().grid.setLayoutManager(createManager());


            adapter = new HeaderViewRecyclerAdapter(
                    new ParsePictureGridAdapter(
                            getView().getContext(),
                            location));

            getView().grid.setAdapter(adapter);
        }


        private void initLocationEditText() {

            final View title = LayoutInflater.from(getView().getContext()).inflate(
                    R.layout.picture_grid_title_item,
                    getView().grid,
                    false);

            locationTitle = ButterKnife.findById(
                    title,
                    R.id.picture_grid_title);

            locationArtNr = ButterKnife.findById(
                    title,
                    R.id.picture_grid_art_nr);

            adapter.addHeaderView(title);

            locationTitle.addTextChangedListener(
                    new TextWatcher() {
                        @Override public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

                        }

                        @Override public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

                        }

                        @Override public void afterTextChanged(final Editable s) {

                            location.setTitle(s.toString());
                        }
                    });

            locationArtNr.addTextChangedListener(
                    new TextWatcher() {
                        @Override public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

                        }

                        @Override public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

                        }

                        @Override public void afterTextChanged(final Editable s) {

                            location.setArtNr(s.toString());
                        }
                    });

        }

        private GridLayoutManager createManager() {

            final GridLayoutManager manager = new GridLayoutManager(
                    getView().getContext(),
                    3);

            manager.setSpanSizeLookup(
                    new GridLayoutManager.SpanSizeLookup() {
                        @Override public int getSpanSize(final int position) {

                            return position == 0 ? 3 : 1;
                        }
                    });
            return manager;
        }

        private void initActionBar() {

            actionBarOwner.setConfig(
                    new ActionBarOwner.Config(
                            false,
                            true,
                            "Picture Grid",
                            null));
        }

        private void saveImages() {

            final Iterable<ParseDocumentImage> transform = Iterables.transform(indices,
                new Function<Integer, ParseDocumentImage>() {
                  @Override public ParseDocumentImage apply(final Integer input) {

                    return new ParseDocumentImage().setAuthor(ParseUser.getCurrentUser())
                        .setLocationId(location)
                        .setImageURL(images.get(input).getImageURL())
                        .setImageTakenDate(images.get(input).getCreatedDate());
                  }
                });

            final List<ParseDocumentImage> images = newArrayList(transform);

            ParseObject.pinAllInBackground(images, new SaveCallback() {
                  @Override public void done(final ParseException e) {

                    if (e != null) {
                      e.printStackTrace();
                    } else {
                      flow.goTo(new LocationDetailScreen(document, yard, location));
                      if (getView() != null) {
                        Toast.makeText(getView().getContext(),
                            format("%s saved", location.getTitle()), Toast.LENGTH_LONG).show();

                        getView().progress.setVisibility(GONE);
                      }
                    }
                  }
                });

            for (final ParseDocumentImage image : images) {

                image.saveEventually(
                        new SaveCallback() {
                            @Override public void done(final ParseException e) {

                                if (e == null) {
                                    final ParseFile file = getParseFile(image.getImageURL());
                                    file.saveInBackground(
                                            new SaveCallback() {
                                                @Override public void done(final ParseException e) {

                                                    if(e == null) {
                                                        image.setImage(file);
                                                        image.saveEventually();
                                                    } else {

                                                        e.printStackTrace();
                                                    }
                                                }
                                            });

                                } else {

                                    e.printStackTrace();
                                }
                            }
                        });
            }


        }

        private void initTextFields() {

            locationTitle.setText(location.getTitle());
            locationArtNr.setText(location.getArtNr());
        }

        public void getFromSdcard() {

            final File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "Camera");

            final File file2 = new File(Environment.getExternalStorageDirectory().getPath());

            addImages(file);
            addImages(file2);
        }

        private void addImages(final File file) {

            if (file.isDirectory()) {
                for (File aFile : file.listFiles(new ImageFileFilter())) {
                    images.add(
                            0,
                            new DocumentImage().setImageURL(aFile.getAbsolutePath()).setCreatedDate(new Date(aFile.lastModified())));


                }
            }
        }

        public void editLocation(final String s) {

        }

        public void handleSave() {

            saveImages();
            location.pinInBackground(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {

                                System.out.println("Presenter.done");
                            } else {
                                System.out.println("e.getMessage() = " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    });
            location.saveInBackground();


        }
    }


}
