package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
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
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.commons.recyclerview.DividerItemDecoration;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.core.MainScope;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
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
import static nigel.com.werfleider.model.DocumentType.OPMERKINGEN;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
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

  private final ParseDocument document;

  public ParseDocumentScreen(final Yard yard, final ParseDocument document) {

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

    return new YardDetailScreen(yard);
  }

  @dagger.Module(injects = {
      ParseDocumentView.class, ParseDocumentLocationAdapter.class
  }, addsTo = CorePresenter.Module.class) static class Module {

    private final Yard yard;

    private final ParseDocument document;

    public Module(final Yard yard, final ParseDocument document) {

      this.yard = yard;
      this.document = document;
    }

    @Provides @Singleton Yard provideWerf() {

      return yard;
    }

    @Provides @Singleton ParseDocument providePlaatsBeschrijf() {

      return document;
    }

    @Provides ParseFileOperations provideFileOperations(final Context context) {

      return new ParseFileOperations(context);
    }

    @Provides MeasurementsFileOperations provideMeasurementsFileOperations(final Context context) {

      return new MeasurementsFileOperations(context);
    }

    @Provides @Singleton List<ParseDocumentLocation> provideLocations() {

      return newArrayList();
    }
  }

  @Singleton static class Presenter extends ViewPresenter<ParseDocumentView> {

    @Inject ParseFileOperations fop;

    @Inject MeasurementsFileOperations measurementsFileOperations;

    @Inject ParseDocument document;

    @Inject ActionBarOwner actionBarOwner;

    @Inject @MainScope Flow flow;

    @Inject Yard yard;

    @Inject Context context;

    @Inject List<ParseDocumentLocation> locations;

    private ParseDocumentLocationAdapter adapter;

    @Override public void onLoad(Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) {
        return;
      }

      actionBarOwner.setConfig(
          new ActionBarOwner.Config(false, true, document.getDocumentType().toString(), null));

      initView();

      loadData(LOCAL);
    }

    private void loadData(final Load load) {

      final ParseQuery<ParseDocumentLocation> query =
          ParseQuery.getQuery(ParseDocumentLocation.class);

      if (load == LOCAL) {

        query.fromLocalDatastore();
      }

      query.whereEqualTo(DOCUMENT_ID, document)
          .findInBackground(new FindCallback<ParseDocumentLocation>() {
                @Override
                public void done(final List<ParseDocumentLocation> list, final ParseException e) {

                  if (e == null) {

                    for (ParseDocumentLocation location : list) {

                      if (!locations.contains(location)) {

                        locations.add(location);
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
                    getView().showLoader(false);
                  }
                }
              });
    }

    private void initView() {

      getView().locations.addItemDecoration(
          new DividerItemDecoration(getView().getContext(), R.drawable.divider));
      getView().locations.setAdapter(
          adapter = new ParseDocumentLocationAdapter(getView().getContext()));

      if (document.getDocumentType() == OPMETINGEN) {
        getView().addLocation.setTitle("Add post");
      } else if (document.getDocumentType() == OPMERKINGEN) {
        getView().addLocation.setTitle("Add opmerking");
      }

      if (yard.getAuthor() != ParseUser.getCurrentUser()) {

        getView().addLocation.setVisibility(GONE);
      }
    }

    public void newImageCollection() {

      Toast.makeText(context, "Creating document...", Toast.LENGTH_LONG).show();

      final ParseDocumentLocation location = new ParseDocumentLocation();
      location.setDocumentId(document)
          .setMeasuringUnit(MeasuringUnit.M)
          .setAuthor(ParseUser.getCurrentUser());

      location.pinInBackground(new SaveCallback() {
            @Override public void done(final ParseException e) {

              if (e == null) {
                locations.add(location);
                adapter.notifyItemInserted(locations.size() - 1);

                flow.goTo(new ParsePictureGridScreen(document, location, yard));
              }
            }
          });

      location.saveEventually();
    }

    public void write() {

      getView().showLoader(true);

      Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override public void call(final Subscriber<? super Boolean> subscriber) {

              ParseQuery.getQuery(ParseDocumentImage.class)
                  .whereContainedIn(LOCATION_ID, locations)
                  .fromLocalDatastore()
                  .findInBackground(new FindCallback<ParseDocumentImage>() {
                        @Override public void done(final List<ParseDocumentImage> list,
                            final ParseException e) {

                          if (e == null) {

                            final Multimap<ParseDocumentLocation, ParseDocumentImage>
                                documentImageMultiMap = ArrayListMultimap.create();

                            for (ParseDocumentImage parseDocumentImage : list) {

                              documentImageMultiMap.put(
                                  (ParseDocumentLocation) parseDocumentImage.getLocationId(),
                                  parseDocumentImage);
                            }

                            final boolean finished =
                                fop.write(document, yard, documentImageMultiMap);
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
                  .whereContainedIn(LOCATION_ID, locations)
                  .fromLocalDatastore()
                  .findInBackground(new FindCallback<ParseDocumentImage>() {
                        @Override public void done(final List<ParseDocumentImage> list,
                            final ParseException e) {

                          if (e == null) {

                            final Multimap<ParseDocumentLocation, ParseDocumentImage>
                                documentImageMultiMap = ArrayListMultimap.create();

                            for (ParseDocumentImage parseDocumentImage : list) {

                              documentImageMultiMap.put(
                                  (ParseDocumentLocation) parseDocumentImage.getLocationId(),
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
