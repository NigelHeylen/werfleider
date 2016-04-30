package nigel.com.werfleider.ui.document;

import android.content.Context;
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
import static nigel.com.werfleider.util.ParseStringUtils.CREATED_AT;
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

  final List<ParseDocument> adapterData = newArrayList();

  private ParseDocumentOverviewAdapter adapter;

  private void loadData() {

    getView().setAdapter(adapter =
        new ParseDocumentOverviewAdapter(getView().getContext(), adapterData, getView()));

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

    query.whereEqualTo(YARD_ID, yard).orderByAscending(CREATED_AT)
        .whereEqualTo(DOCUMENT_TYPE, documentType.name())
        .findInBackground((list, e) -> {

          if (e == null) {

            for (ParseDocument doc : list) {

              if (!adapterData.contains(doc)) {

                adapterData.add(doc);
              }
            }

            adapter.notifyDataSetChanged();

            ParseObject.pinAllInBackground(list);

            if (load == LOCAL) {

              loadDocuments(NETWORK);
            }
          } else {
            e.printStackTrace();
          }

          if (getView() != null) {

            if(adapterData.isEmpty()){

              getView().showEmptyView();
            } else {

              getView().showContentView();
            }
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
