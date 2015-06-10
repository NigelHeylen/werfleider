package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import javax.inject.Inject;

import mortar.ViewPresenter;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseYard;

import static android.view.View.GONE;
import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_TYPE;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ID;

/**
 * Created by nigel on 17/04/15.
 */
public class ParseDocumentOverviewPresenter extends ViewPresenter<ParseDocumentOverviewView> {

    private DocumentType documentType;

    @Inject ParseYard yard;

    @Inject Context context;

    private List<ParseDocument> parseDocuments;

    private ParseDocumentOverviewAdapter adapter;

    private void loadData() {

        parseDocuments = newArrayList();
        getView().setAdapter(
                adapter = new ParseDocumentOverviewAdapter(
                        getView().getContext(),
                        parseDocuments));

        loadDocuments();

    }

    private void loadDocuments() {

        final ParseQuery<ParseDocument> query = ParseQuery.getQuery(ParseDocument.class);
        query.whereEqualTo(
                YARD_ID,
                yard);
        query.whereEqualTo(
                DOCUMENT_TYPE,
                documentType.name()
                          );
        query.findInBackground(
                new FindCallback<ParseDocument>() {
                    @Override public void done(final List<ParseDocument> list, final ParseException e) {

                        if (e == null) {
                            System.out.println("ParseDocumentOverviewPresenter.done " + documentType + " " + list.size());
                            parseDocuments.addAll(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            e.printStackTrace();
                        }

                        if(getView() != null) {
                            getView().loader.setVisibility(
                                    GONE);
                        }
                    }
                });

    }

    public void handleCreateClick() {

        Toast.makeText(
                context,
                "Creating document...",
                Toast.LENGTH_LONG).show();

        final ParseDocument parseDocument = new ParseDocument();
        parseDocument.setWerf(yard)
                     .setAuthor(ParseUser.getCurrentUser())
                     .setDocumentType(documentType);

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

    }

    public void setDocumentType(final DocumentType documentType) {

        this.documentType = documentType;
        loadData();
    }
}
