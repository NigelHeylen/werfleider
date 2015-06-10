package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.model.WerfInt;
import nigel.com.werfleider.ui.werfoverzicht.WerfDetailScreen;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.util.ParseStringUtils.CREATOR_ID;

/**
 * Created by nigel on 14/03/15.
 */
@Layout(R.layout.document_overview_view)
public class DocumentOverviewScreen implements Blueprint, HasParent<WerfDetailScreen> {

    private final WerfInt werf;

    private final DocumentType documentType;

    public DocumentOverviewScreen(final WerfInt werf, final DocumentType documentType) {

        this.werf = werf;
        this.documentType = documentType;
    }

    @Override public String getMortarScopeName() {

        return getClass().getName();
    }

    @Override public Object getDaggerModule() {

        return new Module(
                werf,
                documentType);
    }

    @Override public WerfDetailScreen getParent() {

        return new WerfDetailScreen(werf);
    }

    @dagger.Module(
            injects = {
                    DocumentOverviewView.class,
                    DocumentOverviewAdapter.class,
            },
            addsTo = CorePresenter.Module.class
    )
    static class Module {

        private final WerfInt werf;

        private final DocumentType documentType;

        public Module(final WerfInt werf, final DocumentType documentType) {

            this.werf = werf;
            this.documentType = documentType;
        }

        @Provides WerfInt provideWerf() {

            return werf;
        }

        @Provides DocumentType provideDocumentType() {

            return documentType;
        }

        @Provides @Singleton List<ParseDocument> provideDocuments() {

            return newArrayList();
        }
    }

    static class Presenter extends ViewPresenter<DocumentOverviewView> {

        @Inject DocumentType documentType;

        @Inject WerfInt werf;

        @Inject DocumentDbHelper documentDbHelper;

        @Inject Context context;

        @Inject List<ParseDocument> parseDocuments;

        private ParseDocumentOverviewAdapter adapter;

//        private DocumentOverviewAdapter adapter;

//        private List<Document> documents;

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

            adapter = new ParseDocumentOverviewAdapter(getView().getContext(),
                                                       parseDocuments);

            getDocuments();

//            documents = getDocuments();
//
//            adapter = new DocumentOverviewAdapter(
//                    documents,
//                    getView().getContext());

            getView().setAdapter(adapter);
        }

        private List<Document> getDocuments() {

            final ParseQuery<ParseDocument> query = ParseQuery.getQuery(ParseDocument.class);
            query.whereEqualTo(
                    CREATOR_ID,
                    ParseUser.getCurrentUser().getObjectId());
            query.findInBackground(
                    new FindCallback<ParseDocument>() {
                        @Override public void done(final List<ParseDocument> list, final ParseException e) {

                            if (e == null) {
                                parseDocuments.addAll(list);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

            return newArrayList();
//                    documentDbHelper.getDocuments(
//                    yard,
//                    documentType);
        }

        public void handleCreateClick() {

            Toast.makeText(
                    context,
                    "Creating document...",
                    Toast.LENGTH_LONG).show();

//            final Document document = documentDbHelper.createDocument(
//                    documentType,
//                    Long.parseLong(yard.getId()));

            final ParseDocument parseDocument = new ParseDocument();
            parseDocument.setWerf((ParseYard) werf)
                         .setAuthor(ParseUser.getCurrentUser())
                         .setDocumentType(documentType);

//            parseDocument.setWerf(String.valueOf(yard.getId()))
//                         .setDocumentType(documentType);

            parseDocument.saveEventually(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {
                                parseDocuments.add(parseDocument);
                                Toast.makeText(
                                        context,
                                        "Document " + parseDocument.getDocumentType().toString() + " saved.",
                                        Toast.LENGTH_LONG).show();

                                adapter.notifyItemInserted(parseDocuments.size() - 1);

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

//            documents.add(document);

        }
    }
}
