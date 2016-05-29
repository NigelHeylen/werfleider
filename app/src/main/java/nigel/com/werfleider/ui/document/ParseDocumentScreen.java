package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.commons.recyclerview.DividerItemDecoration;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.core.MainScope;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.pdf.MeasurementsFileOperations;
import nigel.com.werfleider.pdf.ParseFileOperations;
import nigel.com.werfleider.ui.werf.YardDetailScreen;
import nigel.com.werfleider.util.MeasuringUnit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static android.view.View.GONE;
import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.util.ParseStringUtils.CREATED_AT;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_ID;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsedocument_view) public class ParseDocumentScreen
    implements Blueprint, HasParent<YardDetailScreen> {

  private final Yard yard;

  private final Document document;

  public ParseDocumentScreen(final Yard yard, final Document document) {

    this.yard = yard;
    this.document = document;
  }

  @Override public String getMortarScopeName() {

    return getClass().getName();
  }

  @Override public Object getDaggerModule() {

    return new Module(yard, document);
  }

  @Override public YardDetailScreen getParent() {

    return new YardDetailScreen(yard, Iterables.indexOf(newArrayList(DocumentType.values()),
        type -> type.equals(document.getDocumentType())));
  }

  @dagger.Module(injects = {
      ParseDocumentView.class, ParseDocumentLocationAdapter.class
  }, addsTo = CorePresenter.Module.class) static class Module {

    private final Yard yard;

    private final Document document;

    public Module(final Yard yard, final Document document) {

      this.yard = yard;
      this.document = document;
    }

    @Provides @Singleton Yard provideWerf() {

      return yard;
    }

    @Provides @Singleton Document providePlaatsBeschrijf() {

      return document;
    }

    @Provides ParseFileOperations provideFileOperations(final Context context) {

      return new ParseFileOperations(context);
    }

    @Provides MeasurementsFileOperations provideMeasurementsFileOperations(final Context context) {

      return new MeasurementsFileOperations(context);
    }

    @Provides @Singleton List<DocumentLocation> provideLocations() {

      return newArrayList();
    }
  }

  @Singleton static class Presenter extends ViewPresenter<ParseDocumentView> {

    @Inject ParseFileOperations fop;

    @Inject MeasurementsFileOperations measurementsFileOperations;

    @Inject Document document;

    @Inject @MainScope Flow flow;

    @Inject Yard yard;

    @Inject Context context;

    @Inject List<DocumentLocation> adapterData;

    private ParseDocumentLocationAdapter adapter;

    @Override public void onLoad(Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) {
        return;
      }

      initView();

      loadData(LOCAL);
    }

    private void loadData(final Load load) {

      final ParseQuery<DocumentLocation> query =
          ParseQuery.getQuery(DocumentLocation.class);

      if (load == LOCAL) {

        query.fromLocalDatastore();
      }

      query.orderByAscending(CREATED_AT)
          .whereEqualTo(DOCUMENT_ID, document)
          .findInBackground((list, e) -> {

            if (e == null) {

              for (DocumentLocation location : list) {

                if (!adapterData.contains(location)) {

                  adapterData.add(location);
                }
              }

              ParseObject.pinAllInBackground(list);
              adapter.notifyDataSetChanged();

              if (load == LOCAL) {

                loadData(NETWORK);
              }
            } else {
              e.printStackTrace();
            }

            if (getView() != null) {

              if (adapterData.isEmpty()) {

                getView().showEmptyView();
              } else {

                getView().showContentView();
              }
            }
          });
    }

    private void initView() {

      getView().locations.addItemDecoration(
          new DividerItemDecoration(getView().getContext(), R.drawable.divider));
      getView().locations.setAdapter(
          adapter = new ParseDocumentLocationAdapter(getView().getContext(), getView()));

      getView().initEmptyView(document.getDocumentType());

      if (yard.getAuthor() != ParseUser.getCurrentUser()) {

        getView().loadingView.setVisibility(GONE);
      }
    }

    public void newImageCollection() {

      Toast.makeText(context, "Creating location...", Toast.LENGTH_LONG).show();

      final DocumentLocation location = new DocumentLocation();
      location.setDocumentId(document)
          .setMeasuringUnit(MeasuringUnit.M)
          .setAuthor(ParseUser.getCurrentUser());

      flow.goTo(new ParsePictureGridScreen(document, location, yard));
    }

    public void generatePdf() {

      getView().showLoader(true);

      Observable.create(new Observable.OnSubscribe<Boolean>() {

        @Override public void call(final Subscriber<? super Boolean> subscriber) {

          ParseQuery.getQuery(ParseDocumentImage.class)
              .fromLocalDatastore()
              .orderByAscending(CREATED_AT)
              .whereContainedIn(LOCATION_ID, adapterData)
              .findInBackground((list, e) -> {

                if (e == null) {

                  final Multimap<DocumentLocation, ParseDocumentImage> documentImageMultiMap =
                      ArrayListMultimap.create();

                  for (ParseDocumentImage parseDocumentImage : list) {

                    documentImageMultiMap.put(
                        (DocumentLocation) parseDocumentImage.getLocationId(),
                        parseDocumentImage);
                  }

                  final boolean finished = fop.write(document, yard, documentImageMultiMap);
                  if (finished) {
                    subscriber.onNext(true);
                  } else {
                    subscriber.onError(new IOException("Writing to pdf failed"));
                  }
                  subscriber.onCompleted();
                } else {
                  e.printStackTrace();
                  subscriber.onError(e);
                }

                subscriber.onNext(true);
              });
        }
      }).subscribeOn(io()).observeOn(mainThread()).subscribe(new Observer<Boolean>() {
        @Override public void onCompleted() {

          showToast(document.getDocumentType().toString() + ".pdf created");

          if (document.getDocumentType() == OPMETINGEN) {
            writeMeasurementsDocument();
          } else {
            if (getView() != null) {
              getView().showLoader(false);
            }
          }
        }

        @Override public void onError(final Throwable e) {

          e.printStackTrace();
          showToast("Something went wrong");
          if (getView() != null) {
            getView().showLoader(false);
          }
        }

        @Override public void onNext(final Boolean aBoolean) {

        }
      });
    }

    private void writeMeasurementsDocument() {

      Observable.create(new Observable.OnSubscribe<Boolean>() {

        @Override public void call(final Subscriber<? super Boolean> subscriber) {

          ParseQuery.getQuery(ParseDocumentImage.class)
              .whereContainedIn(LOCATION_ID, adapterData)
              .fromLocalDatastore()
              .orderByAscending(CREATED_AT)
              .findInBackground(new FindCallback<ParseDocumentImage>() {
                @Override
                public void done(final List<ParseDocumentImage> list, final ParseException e) {

                  if (e == null) {

                    final Multimap<DocumentLocation, ParseDocumentImage>
                        documentImageMultiMap = ArrayListMultimap.create();

                    for (ParseDocumentImage parseDocumentImage : list) {

                      documentImageMultiMap.put(
                          (DocumentLocation) parseDocumentImage.getLocationId(),
                          parseDocumentImage);
                    }

                    final boolean finished =
                        measurementsFileOperations.writeDocument(yard, document,
                            documentImageMultiMap);
                    if (finished) {
                      subscriber.onNext(true);
                    } else {
                      subscriber.onError(new IOException("Writing to pdf failed"));
                    }
                    subscriber.onCompleted();
                  } else {
                    e.printStackTrace();
                    subscriber.onError(e);
                  }

                  subscriber.onNext(true);
                }
              });
        }
      }).subscribeOn(io()).observeOn(mainThread()).subscribe(new Observer<Boolean>() {
        @Override public void onCompleted() {

          showToast("measurements document created");
          if (getView() != null) {
            getView().showLoader(false);
          }
        }

        @Override public void onError(final Throwable e) {

          e.printStackTrace();
          showToast("Something went wrong");
          if (getView() != null) {
            getView().showLoader(false);
          }
        }

        @Override public void onNext(final Boolean aBoolean) {

        }
      });
    }

    public void showToast(final String message) {

      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
  }
}
