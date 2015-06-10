package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.pdf.ParseFileOperations;
import nigel.com.werfleider.ui.werf.YardDetailScreen;
import nigel.com.werfleider.util.MeasuringUnit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.model.DocumentType.OPMERKINGEN;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_ID;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsedocument_view)
public class ParseDocumentScreen implements Blueprint, HasParent<YardDetailScreen> {

    private final ParseYard yard;

    private final ParseDocument document;

    public ParseDocumentScreen(final ParseYard yard, final ParseDocument document) {

        this.yard = yard;
        this.document = document;
    }

    @Override public String getMortarScopeName() {

        return getClass().getName();
    }

    @Override public Object getDaggerModule() {

        return new Module(
                yard,
                document);
    }

    @Override public YardDetailScreen getParent() {

        return new YardDetailScreen(
                yard);
    }

    @dagger.Module(injects = {
            ParseDocumentView.class,
            ParseDocumentLocationAdapter.class
    }, addsTo = CorePresenter.Module.class)
    static class Module {

        private final ParseYard yard;

        private final ParseDocument document;

        public Module(final ParseYard yard, final ParseDocument document) {

            this.yard = yard;
            this.document = document;
        }

        @Provides @Singleton ParseYard provideWerf() {

            return yard;
        }

        @Provides @Singleton ParseDocument providePlaatsBeschrijf() {

            return document;
        }

        @Provides ParseFileOperations provideFileOperations(final Context context) {

            return new ParseFileOperations(context);
        }

        @Provides @Singleton List<ParseDocumentLocation> provideLocations() {

            return newArrayList();
        }
    }

    @Singleton
    static class Presenter extends ViewPresenter<ParseDocumentView> {

        @Inject ParseFileOperations fop;

        @Inject ParseDocument document;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject ParseYard yard;

        @Inject Context context;

        @Inject List<ParseDocumentLocation> locations;

        private ParseDocumentLocationAdapter adapter;

        @Override public void onLoad(Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

            actionBarOwner.setConfig(
                    new ActionBarOwner.Config(
                            false,
                            true,
                            document.getDocumentType().toString(),
                            null));

            initView();

            loadData();
        }

        private void loadData() {

            final ParseQuery<ParseDocumentLocation> query = ParseQuery.getQuery(ParseDocumentLocation.class);
            query.whereEqualTo(
                    DOCUMENT_ID,
                    document);

            query.findInBackground(
                    new FindCallback<ParseDocumentLocation>() {
                        @Override public void done(final List<ParseDocumentLocation> list, final ParseException e) {

                            if (e == null) {
                                locations.addAll(list);
                                adapter.notifyDataSetChanged();
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

            getView().locations.setAdapter(
                    adapter =
                            new ParseDocumentLocationAdapter(
                                    getView().getContext()));

            if(document.getDocumentType() == OPMETINGEN) {
                getView().addLocation.setTitle("Add post");
            } else if(document.getDocumentType() == OPMERKINGEN){
                getView().addLocation.setTitle("Add opmerking");
            }
        }

        public void newImageCollection() {


            Toast.makeText(
                    context,
                    "Creating document...",
                    Toast.LENGTH_LONG).show();

            final ParseDocumentLocation location = new ParseDocumentLocation();
            location.setDocumentId(document)
                    .setMeasuringUnit(
                            MeasuringUnit.M)
                    .setAuthor(ParseUser.getCurrentUser());

            location.saveInBackground(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {
                                locations.add(location);
                                adapter.notifyItemInserted(locations.size() - 1);

                                flow.goTo(
                                        new ParsePictureGridScreen(
                                                document,
                                                location,
                                                yard));

                            }
                        }
                    });

        }

        public void write() {

            getView().showLoader(true);

            Observable.create(
                    new Observable.OnSubscribe<Boolean>() {

                        @Override public void call(final Subscriber<? super Boolean> subscriber) {

                            final ParseQuery<ParseDocumentImage> query = ParseQuery.getQuery(ParseDocumentImage.class);
                            query.whereContainedIn(
                                    LOCATION_ID,
                                    locations);

                            query.findInBackground(
                                    new FindCallback<ParseDocumentImage>() {
                                        @Override public void done(final List<ParseDocumentImage> list, final ParseException e) {

                                            if (e == null) {

                                                final Multimap<ParseDocumentLocation, ParseDocumentImage> documentImageMultiMap = ArrayListMultimap.create();

                                                for (ParseDocumentImage parseDocumentImage : list) {

                                                    documentImageMultiMap.put((ParseDocumentLocation) parseDocumentImage.getLocationId(), parseDocumentImage);
                                                }

                                                final boolean finished = fop.write(
                                                        document,
                                                        yard,
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
                    })
                      .subscribeOn(io())
                      .observeOn(mainThread())
                      .subscribe(
                              new Observer<Boolean>() {
                                  @Override public void onCompleted() {

                                      showToast(document.getDocumentType().toString() + ".pdf created");
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

            Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT)
                 .show();
        }
    }
}
