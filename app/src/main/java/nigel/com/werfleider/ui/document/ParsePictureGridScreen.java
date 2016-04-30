package nigel.com.werfleider.ui.document;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.ButterKnife;
import com.google.common.collect.Iterables;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.core.MainScope;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.ui.widget.HeaderViewRecyclerAdapter;
import nigel.com.werfleider.util.ImageUtils;
import rx.Observable;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsepicture_grid) public class ParsePictureGridScreen
    implements Blueprint, HasParent<Blueprint> {

  private final ParseDocument document;

  private final ParseDocumentLocation location;

  private final Yard yard;

  public ParsePictureGridScreen(final ParseDocument document, final ParseDocumentLocation location,
      final Yard yard) {

    this.document = document;

    this.location = location;
    this.yard = yard;
  }

  @Override public String getMortarScopeName() {

    return getClass().getName();
  }

  @Override public Object getDaggerModule() {

    return new Module(document, location, yard);
  }

  @Override public Blueprint getParent() {

    return new LocationDetailScreen(document, yard, location);
  }

  @dagger.Module(
      injects = {
          ParsePictureGridView.class, ParsePictureGridAdapter.class
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final ParseDocument document;

    private final ParseDocumentLocation location;

    private final Yard yard;

    public Module(final ParseDocument document, final ParseDocumentLocation location,
        final Yard yard) {

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

  @Singleton public static class Presenter extends ReactiveViewPresenter<ParsePictureGridView> {

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
          new ParsePictureGridAdapter(getView().getContext(), location));

      getView().grid.setAdapter(adapter);
    }

    private void initLocationEditText() {

      final View title = LayoutInflater.from(getView().getContext())
          .inflate(R.layout.picture_grid_title_item, getView().grid, false);

      locationTitle = ButterKnife.findById(title, R.id.picture_grid_title);

      locationArtNr = ButterKnife.findById(title, R.id.picture_grid_art_nr);

      adapter.addHeaderView(title);

      locationTitle.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count,
            final int after) {

        }

        @Override public void onTextChanged(final CharSequence s, final int start, final int before,
            final int count) {

        }

        @Override public void afterTextChanged(final Editable s) {

          location.setTitle(s.toString());
        }
      });

      locationArtNr.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count,
            final int after) {

        }

        @Override public void onTextChanged(final CharSequence s, final int start, final int before,
            final int count) {

        }

        @Override public void afterTextChanged(final Editable s) {

          location.setArtNr(s.toString());
        }
      });
    }

    private GridLayoutManager createManager() {

      final GridLayoutManager manager = new GridLayoutManager(getView().getContext(), 3);

      manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(final int position) {

          return position == 0 ? 3 : 1;
        }
      });
      return manager;
    }

    private void initActionBar() {

      actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, "Picture Grid", null));
    }

    private void saveImages() {

      final Iterable<ParseDocumentImage> documentImages = Iterables.transform(indices,
          index -> new ParseDocumentImage().setAuthor(ParseUser.getCurrentUser())
              .setLocationId(location)
              .setImageURL(images.get(index).getImageURL())
              .setImageBytes(ImageUtils.getBytesFromFilePath(images.get(index).getImageURL()))
              .setImageTakenDate(images.get(index).getCreatedDate()));
      for (ParseDocumentImage documentImage : documentImages) {

        documentImage.saveEventually();
        documentImage.pinInBackground();
      }
    }

    private void initTextFields() {

      locationTitle.setText(location.getTitle());
      locationArtNr.setText(location.getArtNr());
    }

    public void getFromSdcard() {

      final String[] columns = {
          MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA,
          MediaStore.Files.FileColumns.DATE_ADDED, MediaStore.Files.FileColumns.MEDIA_TYPE,
          MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.TITLE
      };

      final String selection = format("%s = %s", MediaStore.Files.FileColumns.MEDIA_TYPE,
          MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

      final Cursor imageCursor =
          ContentResolverCompat.query(getView().getContext().getContentResolver(),
              MediaStore.Files.getContentUri("external"), columns, selection, null,
              MediaStore.Files.FileColumns.DATE_ADDED + " DESC", null);

      subscribe(Observable.just(imageCursor)
          .doOnTerminate(imageCursor::close)
          .filter(cursor -> cursor.getCount() > 0)
          .map(cursor -> {

            final List<DocumentImage> documentImages = newArrayList();

            while (cursor.moveToNext()) {

              int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

              int dataAddedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

              final DocumentImage item =
                  new DocumentImage().setImageURL(cursor.getString(dataColumnIndex))
                      .setCreatedDate(new Date(cursor.getLong(dataAddedIndex)));

              documentImages.add(item);
            }

            return documentImages;
          })
          .subscribeOn(io())
          .observeOn(mainThread())
          .subscribe(this::bindItems, Throwable::printStackTrace));
    }

    private void bindItems(List<DocumentImage> documentImages) {
      images.clear();
      images.addAll(documentImages);
      adapter.notifyDataSetChanged();
    }

    public void handleSave() {

      location.saveEventually();
      saveImages();
      flow.goTo(new LocationDetailScreen(document, yard, location));
    }
  }
}
