package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import com.parse.ParseUser;
import dagger.Provides;
import flow.Flow;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.Yard;

/**
 * Created by nigel on 09/01/16.
 */

@Layout(R.layout.document_create_view) public class DocumentCreateScreen implements Blueprint {

  private final Yard yard;
  private final DocumentType documentType;
  private final ParseDocument document;

  public DocumentCreateScreen(Yard yard, DocumentType documentType) {
    this.yard = yard;
    this.documentType = documentType;
    this.document = null;
  }

  public DocumentCreateScreen(Yard yard, DocumentType documentType, ParseDocument document) {
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

  @dagger.Module(
      injects = {
          DocumentCreateView.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final Yard yard;
    private final DocumentType documentType;
    private final ParseDocument document;

    public Module(Yard yard, DocumentType documentType, ParseDocument document) {

      this.yard = yard;
      this.documentType = documentType;
      this.document = document;
    }

    @Provides Yard provideYard(){

      return yard;
    }

    @Provides DocumentType documentType(){
      return documentType;
    }
    @Provides ParseDocument document(){
      return document;
    }
  }

  static class DocumentCreatePresenter extends ViewPresenter<DocumentCreateView> {

    @Inject Yard yard;

    @Inject DocumentType documentType;

    @Inject Flow flow;

    @Inject ParseDocument document;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

      if(document != null){

        getView().name.setText(document.getName());
      }
    }

    public void save(String name) {

      ParseDocument document;

      if(this.document == null){

        document = new ParseDocument();
      } else {

        document = this.document;
      }

      document.setAuthor(ParseUser.getCurrentUser());
      document.setWerf(yard);
      document.setName(name);
      document.setDocumentType(documentType);

      document.pinInBackground();

      document.saveEventually();
      flow.goBack();
    }
  }
}
