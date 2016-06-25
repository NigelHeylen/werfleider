package nigel.com.werfleider.ui.location;

import android.os.Bundle;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.document.ParseDocumentScreen;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;

/**
 * Created by nigel on 12/01/16.
 */

@Layout(R.layout.location_edit_view) public class LocationEditScreen implements Blueprint,
    HasParent<ParseDocumentScreen> {

  private final DocumentLocation location;
  private final Document document;
  private final Yard yard;

  public LocationEditScreen(DocumentLocation location, Document document, Yard yard) {

    this.location = location;
    this.document = document;
    this.yard = yard;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module(location);
  }

  @Override public ParseDocumentScreen getParent() {
    return new ParseDocumentScreen(yard, document);
  }

  @dagger.Module(
      injects = {
          LocationEditView.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final DocumentLocation location;

    public Module(DocumentLocation location) {
      this.location = location;
    }

    @Provides DocumentLocation location(){

      return location;
    }
  }

  @Singleton static class LocationEditPresenter extends ReactiveViewPresenter<LocationEditView> {

    @Inject DocumentLocation location;

    @Inject Flow flow;

    @Inject ParseErrorHandler parseErrorHandler;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

      getView().title.setText(location.getTitle());
      getView().artNr.setText(location.getArtNr());
    }

    public void handleSave(String title, String artNr) {

      location.setTitle(title);
      location.setArtNr(artNr);

      location.pinInBackground();

      location.saveEventually(e1 -> {
        if(e1 != null) parseErrorHandler.handleParseError(e1);
      });

      flow.goBack();
    }
  }
}
