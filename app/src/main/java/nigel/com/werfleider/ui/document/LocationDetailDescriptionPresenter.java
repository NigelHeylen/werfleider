package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import rx.subjects.BehaviorSubject;

/**
 * Created by nigel on 26/12/15.
 */
@Singleton public class LocationDetailDescriptionPresenter extends ReactiveViewPresenter<LocationDetailDescriptionView>{

  @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

  private ParseDocumentImage documentImage;

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    documentImage = documentImageBus.getValue();
    if(documentImage != null){

      bindDocumentImage(documentImage);
    }

    subscribe(documentImageBus.subscribe(this::bindDocumentImage));
  }

  private void bindDocumentImage(final ParseDocumentImage documentImage) {

    this.documentImage = documentImage;
    if(getView() != null) {
      getView().description.setText(documentImage.getDescription());
    }
  }

  public void changeDescription(final String description) {

    documentImage.setDescription(description);
  }
}
