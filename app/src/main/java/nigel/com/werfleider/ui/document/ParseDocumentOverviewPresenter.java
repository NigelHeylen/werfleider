package nigel.com.werfleider.ui.document;

import android.content.Context;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import flow.Flow;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import mortar.ViewPresenter;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.util.FlowUtils;
import nigel.com.werfleider.util.MeasuringUnit;

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

  @Inject ParseErrorHandler parseErrorHandler;

  final List<DocumentLocation> adapterData = newArrayList();

  private ParseDocumentOverviewAdapter adapter;

  private void loadData() {

    getView().setAdapter(
        adapter = new ParseDocumentOverviewAdapter(getView().getContext(), adapterData, getView()));

    if (!Objects.equals(yard.getCreator(), ParseUser.getCurrentUser().getEmail())) {

      getView().create.setVisibility(GONE);
    }

    loadDocuments(LOCAL);
  }

  private void loadDocuments(final Load load) {

    final ParseQuery<DocumentLocation> query = ParseQuery.getQuery(DocumentLocation.class);

    if (load == LOCAL) {

      query.fromLocalDatastore();
    }

    query.whereEqualTo(YARD_ID, yard)
        .orderByAscending(CREATED_AT)
        .whereEqualTo(DOCUMENT_TYPE, documentType.name())
        .findInBackground((list, e) -> {

          if (e == null) {

            for (DocumentLocation doc : list) {

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
            parseErrorHandler.handleParseError(e);
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

  public void handleCreateClick() {

    final DocumentLocation location = new DocumentLocation();
    location.setWerf(yard)
        .setDocumentType(documentType)
        .setMeasuringUnit(MeasuringUnit.M)
        .setCreator(ParseUser.getCurrentUser().getEmail());

    location.saveInBackground(e1 -> {
      if (e1 != null) {
        parseErrorHandler.handleParseError(e1);
      } else {

        location.pinInBackground(e2 -> {

          if (e2 != null) {
            parseErrorHandler.handleParseError(e2);
          } else {
            flow.goTo(new ParsePictureGridScreen(location, yard, FlowUtils.getCurrentScreen(flow),
                parseErrorHandler));
          }
        });
      }
    });
  }

  public void setDocumentType(final DocumentType documentType) {

    this.documentType = documentType;
    initView();
    loadData();
  }

  private void initView() {

    if (!Objects.equals(yard.getCreator(), ParseUser.getCurrentUser().getEmail())) {

      getView().create.setVisibility(GONE);
    }
  }
}
