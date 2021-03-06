package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManagerEx;
import android.support.v7.widget.OrientationHelperEx;
import android.support.v7.widget.RecyclerViewEx;
import android.view.View;
import com.google.common.collect.Iterables;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.ui.werf.YardDetailScreen;
import nigel.com.werfleider.util.FlowUtils;
import rx.subjects.BehaviorSubject;

import static android.view.View.GONE;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.commons.recyclerview.Action.INSERT;
import static nigel.com.werfleider.util.ParseStringUtils.CREATED_AT;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsedocument_location_detail_view) public class LocationDetailScreen
    implements Blueprint, HasParent<YardDetailScreen> {

  private final Yard yard;

  private final DocumentLocation location;

  public LocationDetailScreen(final Yard yard, final DocumentLocation location) {

    this.yard = yard;
    this.location = location;
  }

  @Override public String getMortarScopeName() {

    return format("%s document: yard id %s, %s, location: %s", getClass().getName(),
        yard.getObjectId(), location.getDocumentType().toString(), location.getTitle());
  }

  @Override public YardDetailScreen getParent() {

    final int tabIndex = Iterables.indexOf(newArrayList(DocumentType.values()),
        type -> type.equals(location.getDocumentType()));
    return new YardDetailScreen(yard, tabIndex + 1);
  }

  @Override public Object getDaggerModule() {

    return new Module(yard, location);
  }

  @dagger.Module(
      injects = {
          LocationDetailView.class, DocumentImageListItemAdapter.class,
          LocationDetailDescriptionView.class, LocationDetailDimensionsView.class,
          LocationDetailInfoView.class, LocationDetailCameraView.class,
          LocationDetailCurrentUserOpmetingAdapter.class,
          LocationDetailCurrentUserRegularAdapter.class, LocationDetailAudioView.class
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final Yard yard;

    private final DocumentLocation location;

    Module(final Yard yard, final DocumentLocation location) {

      this.location = location;
      this.yard = yard;
    }

    @Provides @Singleton Yard provideYard() {

      return yard;
    }

    @Provides @Singleton DocumentLocation provideLocation() {

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

    @Inject DocumentLocation location;

    @Inject Yard yard;

    @Inject ActionBarOwner actionBarOwner;

    @Inject DocumentImageAdapterData adapterData;

    @Inject Flow flow;

    @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

    @Inject ParseErrorHandler parseErrorHandler;

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

      loadData(LOCAL);

      if (!Objects.equals(location.getCreator(), ParseUser.getCurrentUser().getEmail())) {
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
        public ParseDocumentImage lastDocumentImage;

        @Override
        public void onScrollStateChanged(final RecyclerViewEx recyclerViewEx, final int i) {

          if (i == RecyclerViewEx.SCROLL_STATE_IDLE) {
            if (position != layoutManager.findFirstCompletelyVisibleItemPosition()) {
              position = layoutManager.findFirstCompletelyVisibleItemPosition();
              if (position != -1) {
                if (lastDocumentImage != null) {
                  lastDocumentImage.saveEventually(e1 -> {
                    if(e1 != null) parseErrorHandler.handleParseError(e1);
                  });
                }
                lastDocumentImage = adapterData.get(position);
                documentImageBus.onNext(adapterData.get(position));
              }
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

      getView().addImages.setVisibility(GONE);

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

    private void loadData(final Load load) {

      final ParseQuery<ParseDocumentImage> query = ParseQuery.getQuery(ParseDocumentImage.class);

      if (load == LOCAL) {

        query.fromLocalDatastore();
      }

      query.orderByAscending(CREATED_AT)
          .whereEqualTo(LOCATION_ID, location)
          .findInBackground((list, e) -> {

            if (e == null) {

              if (getView() != null) {
                for (ParseDocumentImage image : list) {

                  if (!adapterData.contains(image)) {

                    adapterData.add(image);
                  }
                }
                ParseObject.pinAllInBackground(list);
                adapter.notifyDataSetChanged();

                if (!adapterData.isEmpty()) {
                  documentImageBus.onNext(adapterData.get(0));

                  getView().showContentView();
                } else {

                  if (getView() != null) {

                    getView().showEmptyView();
                  }
                }

                if (load == LOCAL) {

                  loadData(NETWORK);
                }

                if (getView() != null) {

                  getView().imageList.scrollToPosition(0);
                }
              }
            } else {
              parseErrorHandler.handleParseError(e);
            }
          });
    }

    private void initActionBar() {

      actionBarOwner.setConfig(new ActionBarOwner.Config(false, true,
          isNullOrEmpty(location.getTitle()) ? "Location" : location.getTitle(), null));
    }

    public void initAdapter() {

      getView().imageList.setAdapter(
          adapter = new DocumentImageListItemAdapter(getView().getContext()));
      getView().detailViews.setAdapter(getPagerAdapter());
      getView().detailViews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override public void onPageSelected(int position) {

          if (location.getDocumentType() == DocumentType.OPMETINGEN) {

            getView().addImages.setVisibility(position == 3 ? GONE : View.VISIBLE);
          } else {

            getView().addImages.setVisibility(position == 2 ? GONE : View.VISIBLE);
          }
        }

        @Override public void onPageScrollStateChanged(int state) {

        }
      });
    }

    @NonNull private PagerAdapter getPagerAdapter() {
      if (location.getDocumentType() == DocumentType.OPMETINGEN) {
        return new LocationDetailCurrentUserOpmetingAdapter(getView().getContext());
      } else {

        return new LocationDetailCurrentUserRegularAdapter(getView().getContext());
      }
    }

    public void handleEdit() {

      flow.goTo(new ParsePictureGridScreen(location, yard, FlowUtils.getCurrentScreen(flow), parseErrorHandler));
    }

    @Override protected void onExitScope() {
      super.onExitScope();

      for (ParseDocumentImage parseDocumentImage : adapterData.getList()) {

        parseDocumentImage.saveEventually(e1 -> {
          if(e1 != null) parseErrorHandler.handleParseError(e1);
        });
      }
      yard.saveEventually(e1 -> {
        if(e1 != null) parseErrorHandler.handleParseError(e1);
      });
      location.saveEventually(e1 -> {
        if(e1 != null) parseErrorHandler.handleParseError(e1);
      });

    }
  }
}