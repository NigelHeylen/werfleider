package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import com.google.common.collect.Iterables;
import com.parse.ParseUser;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.werf.YardDetailScreen;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 09/01/16.
 */

@Layout(R.layout.document_create_view) public class DocumentCreateScreen
    implements Blueprint, HasParent<YardDetailScreen> {

  private final Yard yard;
  private final DocumentType documentType;
  private final Document document;

  public DocumentCreateScreen(Yard yard, DocumentType documentType) {
    this.yard = yard;
    this.documentType = documentType;
    this.document = null;
  }

  public DocumentCreateScreen(Yard yard, DocumentType documentType, Document document) {
    this.yard = yard;
    this.documentType = documentType;
    this.document = document;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module(yard, documentType, document);
  }

  @Override public YardDetailScreen getParent() {
    return new YardDetailScreen(yard,
        Iterables.indexOf(newArrayList(DocumentType.values()), type -> type.equals(documentType)));
  }

  @dagger.Module(
      injects = {
          DocumentCreateView.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final Yard yard;
    private final DocumentType documentType;
    private final Document document;

    public Module(Yard yard, DocumentType documentType, Document document) {

      this.yard = yard;
      this.documentType = documentType;
      this.document = document;
    }

    @Provides Yard provideYard() {

      return yard;
    }

    @Provides DocumentType documentType() {
      return documentType;
    }

    @Provides Document document() {
      return document;
    }
  }

  static class DocumentCreatePresenter extends ViewPresenter<DocumentCreateView> {

    @Inject Yard yard;

    @Inject DocumentType documentType;

    @Inject Flow flow;

    @Inject Document document;

    @Inject ParseErrorHandler parseErrorHandler;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

      if (document != null) {

        getView().name.setText(document.getName());
      }
    }

    public void save(String name) {

      Document document;

      if (this.document == null) {

        document = new Document();
      } else {

        document = this.document;
      }

      document.setCreator(ParseUser.getCurrentUser().getEmail());
      document.setWerf(yard);
      document.setName(name);
      document.setDocumentType(documentType);

      document.pinInBackground();

      document.saveEventually(e1 -> {
        if(e1 != null) parseErrorHandler.handleParseError(e1);
      });
      flow.goBack();
    }
  }
}
