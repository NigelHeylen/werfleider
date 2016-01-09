package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManagerEx;
import android.support.v7.widget.OrientationHelperEx;
import android.support.v7.widget.RecyclerViewEx;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import rx.subjects.BehaviorSubject;

import static android.view.View.GONE;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.commons.recyclerview.Action.INSERT;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsedocument_location_detail_view) public class LocationDetailScreen
    implements Blueprint, HasParent<ParseDocumentScreen> {

  private final ParseDocument document;

  private final Yard yard;

  private final ParseDocumentLocation location;

  public LocationDetailScreen(final ParseDocument document, final Yard yard,
      final ParseDocumentLocation location) {

    this.document = document;
    this.yard = yard;
    this.location = location;
  }

  @Override public String getMortarScopeName() {

    return format("%s document: yard id %s, %s, location: %s", getClass().getName(),
        yard.getObjectId(), document.getDocumentType().toString(), location.getTitle());
  }

  @Override public ParseDocumentScreen getParent() {

    return new ParseDocumentScreen(yard, document);
  }

  @Override public Object getDaggerModule() {

    return new Module(document, yard, location);
  }

  @dagger.Module(
      injects = {
          LocationDetailView.class, DocumentImageListItemAdapter.class,
          LocationDetailDescriptionView.class, LocationDetailDimensionsView.class,
          LocationDetailInfoView.class, LocationDetailCameraView.class,
          LocationDetailPagerAdapter.class, LocationDetailAudioView.class
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final ParseDocument document;

    private final Yard yard;

    private final ParseDocumentLocation location;

    Module(final ParseDocument document, final Yard yard, final ParseDocumentLocation location) {

      this.document = document;
      this.location = location;
      this.yard = yard;
    }

    @Provides @Singleton Yard provideYard() {

      return yard;
    }

    @Provides @Singleton ParseDocument provideDocument() {

      return document;
    }

    @Provides @Singleton ParseDocumentLocation provideLocation() {

      return location;
    }

    @Provides @Singleton DocumentImageAdapterData provideImageList(
        final DocumentImageAdapterDataBean adapterDataBean) {

      return adapterDataBean;
    }

    @Provides @Singleton BehaviorSubject<ParseDocumentImage> provideDocumentImageBus() {

      return BehaviorSubject.create();
    }
  }

  @Singleton public static class Presenter extends ReactiveViewPresenter<LocationDetailView> {

    @Inject ParseDocument document;

    @Inject ParseDocumentLocation location;

    @Inject Yard yard;

    @Inject ActionBarOwner actionBarOwner;

    @Inject DocumentImageAdapterData adapterData;

    @Inject Flow flow;

    @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

    private int position;

    private DocumentImageListItemAdapter adapter;

    private LinearLayoutManagerEx layoutManager;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);

      if (getView() == null) {
        return;
      }

      initActionBar();

      initView();

      initSubscriptions();

      if (adapterData.isEmpty()) {
        getImages(LOCAL);
      }

      if (location.getAuthor() != ParseUser.getCurrentUser()) {
        disableViews();
      }
    }

    private void initSubscriptions() {

      subscribe(adapterData.getActionBus()
          .filter(action -> action.action == INSERT)
          .map(action -> action.documentImage)
          .subscribe(image -> {
            adapter.notifyItemInserted(adapterData.size());
            getView().imageList.smoothScrollToPosition(adapterData.size());
          }));
    }

    private void initView() {

      getView().imageList.setLayoutManager(layoutManager =
          new LinearLayoutManagerEx(getView().getContext(), OrientationHelperEx.HORIZONTAL, false));

      getView().imageList.setOnScrollListener(new RecyclerViewEx.OnScrollListener() {
        @Override
        public void onScrollStateChanged(final RecyclerViewEx recyclerViewEx, final int i) {

          if (i == RecyclerViewEx.SCROLL_STATE_IDLE) {
            if (position != layoutManager.findFirstCompletelyVisibleItemPosition()) {
              position = layoutManager.findFirstCompletelyVisibleItemPosition();
              documentImageBus.onNext(adapterData.get(position));
            }
          }
        }

        @Override
        public void onScrolled(final RecyclerViewEx recyclerViewEx, final int i, final int i1) {

        }
      });

      initAdapter();
      getView().detailTabs.setViewPager(getView().detailViews);
    }

    private void disableViews() {

      getView().actionsMenu.setVisibility(GONE);

      //for (MaterialEditText editText : getView().editTexts) {
      //
      //  disableEditText(editText);
      //}

      //disableEditText(getView().description);
      //disableEditText(getView().quantity);
      //disableEditText(getView().ms);
      //disableEditText(getView().floor);
      //disableEditText(getView().location);
      //getView().measuringUnits.setEnabled(false);
    }

    private void disableEditText(MaterialEditText editText) {
      editText.setEnabled(false);
      //editText.setInputType(TYPE_NULL);
    }

    private void getImages(final Load load) {

      final ParseQuery<ParseDocumentImage> query = ParseQuery.getQuery(ParseDocumentImage.class);

      if (load == LOCAL) {

        query.fromLocalDatastore();
      }

      query.whereEqualTo(LOCATION_ID, location).findInBackground((list, e) -> {

        if (e == null) {

          adapterData.clear();
          adapterData.addAll(list);
          if (!list.isEmpty()) {
            documentImageBus.onNext(list.get(0));
          }

          if (load == LOCAL) {

            getImages(NETWORK);
          }

          adapter.notifyDataSetChanged();
        } else {
          e.printStackTrace();
        }
        if (getView() != null) {
          getView().progressBar.setVisibility(GONE);
        }
      });
    }

    private void initActionBar() {

      actionBarOwner.setConfig(new ActionBarOwner.Config(false, true,
          isNullOrEmpty(location.getTitle()) ? "Location" : location.getTitle(), null));
    }

    public void handleSave() {

      for (ParseDocumentImage parseDocumentImage : adapterData.getList()) {
        parseDocumentImage.saveEventually();
      }

      getView().progressBar.setVisibility(GONE);
    }

    public void initAdapter() {

      getView().imageList.setAdapter(adapter =
          new DocumentImageListItemAdapter(getView().getContext(), getView().progressBar));
      getView().detailViews.setAdapter(new LocationDetailPagerAdapter(getView().getContext()));
    }

    public void handleEdit() {

      flow.goTo(new ParsePictureGridScreen(document, location, yard));
    }
  }
}