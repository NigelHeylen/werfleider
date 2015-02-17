package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

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
import nigel.com.werfleider.model.DocumentLocatie;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.pdf.FileOperations;
import nigel.com.werfleider.ui.werfoverzicht.WerfDetailScreen;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

import static java.lang.String.format;

/**
 * Created by nigel on 25/11/14.
 */
@Layout(R.layout.document_view)
public class DocumentScreen implements Blueprint, HasParent<WerfDetailScreen> {

    private final Werf werf;
    private final DocumentType documentType;

    public DocumentScreen(final Werf werf, final DocumentType documentType) {

        this.werf = werf;
        this.documentType = documentType;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module(werf, documentType);
    }

    @Override public WerfDetailScreen getParent() {
        return new WerfDetailScreen(werf);
    }

    @dagger.Module(injects = {
            DocumentView.class,
            DocumentLocationAdapter.class
    }, addsTo = CorePresenter.Module.class)
    static class Module {

        private final Werf werf;
        private final DocumentType documentType;

        public Module(final Werf werf, final DocumentType documentType) {

            this.werf = werf;
            this.documentType = documentType;
        }

        @Provides Werf provideWerf() {
            return werf;
        }

        @Provides @Singleton Document providePlaatsBeschrijf(final DocumentDbHelper documentDbHelper) {
            return documentDbHelper.getDocument(werf.getId(), documentType);
        }

        @Provides FileOperations provideFileOperations(final Context context) {
            return new FileOperations(context);
        }
    }

    @Singleton
    public static class Presenter extends ViewPresenter<DocumentView> {

        @Inject FileOperations fop;

        @Inject Document document;

        @Inject ActionBarOwner actionBarOwner;

        @Inject @MainScope Flow flow;

        @Inject Werf werf;

        @Inject DocumentDbHelper documentDbHelper;

        @Override public void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            final DocumentView view = getView();
            if (view == null) {
                return;
            }


            ActionBarOwner.MenuAction menu =
                    new ActionBarOwner.MenuAction(
                            "Save", new Action0() {
                        @Override public void call() {
                            view.saveDocument();
                        }
                    });
            actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, document.getDocumentType().name().toLowerCase(), menu));

            initTextFields();

        }

        private void initTextFields() {
            getView().initView(document);
        }

        public void newImageCollection() {
            flow.goTo(new PictureGridScreen(document, new DocumentLocatie(""), werf));
        }

        public void write() {
            fop.write(document, werf);
            if (fop.write(document, werf)) {
                getView().showToast(document.getDocumentType().name().toLowerCase() + ".pdf created");
            } else {
                getView().showToast("I/O error");
            }

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
                                      Toast.makeText(getView().getContext(),format("%s saved.", document.getDocumentType().name().toLowerCase()), Toast.LENGTH_LONG).show();
                                  }

                                  @Override public void onError(final Throwable e) {
                                      e.printStackTrace();
                                  }

                                  @Override public void onNext(final Integer integer) {

                                  }
                              });
        }
    }
}
