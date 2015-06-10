package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;

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
import nigel.com.werfleider.dao.document.DocumentDbHelper;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.WerfInt;
import nigel.com.werfleider.pdf.FileOperations;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by nigel on 25/11/14.
 */
@Layout(R.layout.document_view)
public class DocumentScreen implements Blueprint, HasParent<DocumentOverviewScreen> {

    private final WerfInt  werf;

    private final Document document;

    public DocumentScreen(final WerfInt werf, final Document document) {

        this.werf = werf;
        this.document = document;
    }

    @Override public String getMortarScopeName() {

        return getClass().getName();
    }

    @Override public Object getDaggerModule() {

        return new Module(
                werf,
                document);
    }

    @Override public DocumentOverviewScreen getParent() {

        return new DocumentOverviewScreen(
                werf,
                document.getDocumentType());
    }

    @dagger.Module(injects = {
            DocumentView.class,
            DocumentLocationAdapter.class
    }, addsTo = CorePresenter.Module.class)
    static class Module {

        private final WerfInt werf;

        private final Document document;

        public Module(final WerfInt werf, final Document document) {

            this.werf = werf;
            this.document = document;
        }

        @Provides WerfInt provideWerf() {

            return werf;
        }

        @Provides @Singleton Document providePlaatsBeschrijf(final DocumentDbHelper documentDbHelper) {

            return documentDbHelper.getDocument(document.getId());
        }

        @Provides FileOperations provideFileOperations(final Context context) {

            return new FileOperations(context);
        }
    }

    @Singleton
    static class Presenter extends ViewPresenter<DocumentView> {

        @Inject FileOperations fop;

        @Inject Document document;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject WerfInt werf;

        @Inject DocumentDbHelper documentDbHelper;

        @Inject Context context;

        @Override public void onLoad(Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);
            final DocumentView view = getView();
            if (view == null) {
                return;
            }

            actionBarOwner.setConfig(
                    new ActionBarOwner.Config(
                            false,
                            true,
                            capitalize(document.getDocumentType().name().toLowerCase()),
                            null));

            initTextFields();

        }

        private void initTextFields() {

            getView().initView(document);
        }

        public void newImageCollection() {

            flow.goTo(
                    new PictureGridScreen(
                            document,
                            new DocumentLocation(""),
                            werf));
        }

        public void write() {

            getView().showLoader(true);

            Observable.create(
                    new Observable.OnSubscribe<Boolean>() {

                        @Override public void call(final Subscriber<? super Boolean> subscriber) {

                            final boolean finished = fop.write(
                                    document,
                                    werf);
                            if (finished) {
                                subscriber.onNext(true);
                            } else {
                                subscriber.onError(new IOException("Writing to pdf failed"));
                            }
                            subscriber.onCompleted();

                        }
                    })
                      .subscribeOn(io())
                      .observeOn(mainThread())
                      .subscribe(
                              new Observer<Boolean>() {
                                  @Override public void onCompleted() {
                                      showToast(document.getDocumentType().name().toLowerCase() + ".pdf created");
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

//        public void read(final String filename) {
//            String text = fop.read(filename);
//            if (text != null) {
//                getView().showPdfText(text);
//            } else {
//                getView().showToast("File not Found");
//                getView().showPdfText(null);
//            }
//        }

        public void saveDocument() {

            Observable.just(documentDbHelper.updateDocument(document))
                      .subscribe(
                              new Observer<Integer>() {
                                  @Override public void onCompleted() {
                                      showToast(format("%s saved.", document.getDocumentType().name().toLowerCase()));

                                  }

                                  @Override public void onError(final Throwable e) {
                                      e.printStackTrace();
                                  }

                                  @Override public void onNext(final Integer integer) {

                                  }
                              });
        }



        public void showToast(final String message) {

            Toast.makeText(
                    context,
                    message, Toast.LENGTH_SHORT)
                 .show();
        }
    }
}
