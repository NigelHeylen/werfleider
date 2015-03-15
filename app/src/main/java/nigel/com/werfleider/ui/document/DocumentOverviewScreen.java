package nigel.com.werfleider.ui.document;

import android.os.Bundle;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.document.DocumentDbHelper;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.werfoverzicht.WerfDetailScreen;

/**
 * Created by nigel on 14/03/15.
 */
@Layout(R.layout.document_overview_view)
public class DocumentOverviewScreen implements Blueprint, HasParent<WerfDetailScreen> {

    private final Werf werf;
    private final DocumentType documentType;

    public DocumentOverviewScreen(final Werf werf, final DocumentType documentType) {
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

    @dagger.Module(
            injects = {
                    DocumentOverviewView.class,
                    DocumentOverviewAdapter.class
            },
            addsTo = CorePresenter.Module.class
    )
    static class Module{

        private final Werf werf;
        private final DocumentType documentType;

        public Module(final Werf werf, final DocumentType documentType) {
            this.werf = werf;
            this.documentType = documentType;
        }

        @Provides Werf provideWerf(){
            return werf;
        }

        @Provides DocumentType provideDocumentType(){
            return documentType;
        }
    }

    static class Presenter extends ViewPresenter<DocumentOverviewView>{

        @Inject DocumentType documentType;

        @Inject Werf werf;

        @Inject DocumentDbHelper documentDbHelper;

        private DocumentOverviewAdapter adapter;

        private List<Document> documents;

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if(getView() == null) return;

            documents = getDocuments();

            adapter = new DocumentOverviewAdapter(documents, getView().getContext());

            getView().setAdapter(adapter);
        }

        private List<Document> getDocuments() {
            return documentDbHelper.getDocuments(werf, documentType);
        }

        public void handleCreateClick() {
            final Document document = documentDbHelper.createDocument(documentType, werf.getId());

            documents.add(document);
            adapter.notifyItemInserted(documents.size() - 1);

        }
    }
}
