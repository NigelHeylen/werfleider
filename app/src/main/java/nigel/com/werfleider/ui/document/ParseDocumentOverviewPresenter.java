package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.widget.Toast;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import mortar.ViewPresenter;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.Yard;

import static android.view.View.GONE;
import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_TYPE;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ID;

/**
 * Created by nigel on 17/04/15.
 */
public class ParseDocumentOverviewPresenter extends ViewPresenter<ParseDocumentOverviewView> {

  private DocumentType documentType;

  @Inject Yard yard;

  @Inject Context context;

  @Inject Flow flow;

  final List<ParseDocument> parseDocuments = newArrayList();

  private ParseDocumentOverviewAdapter adapter;

  private void loadData() {

    getView().setAdapter(adapter =
        new ParseDocumentOverviewAdapter(getView().getContext(), parseDocuments, getView()));

    if (yard.getAuthor() != ParseUser.getCurrentUser()) {

      getView().create.setVisibility(GONE);
    }

    loadDocuments(LOCAL);
  }

  private void loadDocuments(final Load load) {

    final ParseQuery<ParseDocument> query = ParseQuery.getQuery(ParseDocument.class);

    if (load == LOCAL) {

      query.fromLocalDatastore();
    }

    query.whereEqualTo(YARD_ID, yard)
        .whereEqualTo(DOCUMENT_TYPE, documentType.name())
        .findInBackground((list, e) -> {

          if (e == null) {

            for (ParseDocument doc : list) {

              if (!parseDocuments.contains(doc)) {

                parseDocuments.add(doc);
              }
            }

            ParseObject.pinAllInBackground(parseDocuments);
            adapter.notifyDataSetChanged();

            if (load == LOCAL) {

              loadDocuments(NETWORK);
            }
          } else {
            e.printStackTrace();
          }

          if (getView() != null) {
            getView().loader.setVisibility(GONE);
          }
        });
  }

  public void handleCreateClick() {

    flow.goTo(new DocumentCreateScreen(yard, documentType));
  }

  public void setDocumentType(final DocumentType documentType) {

    this.documentType = documentType;
    initView();
    loadData();
  }

  private void initView() {

    if (yard.getAuthor() != ParseUser.getCurrentUser()) {

      getView().create.setVisibility(GONE);
    }
  }
}
