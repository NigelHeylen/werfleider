package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import javax.inject.Inject;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import rx.subjects.BehaviorSubject;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailInfoPresenter extends ReactiveViewPresenter<LocationDetailInfoView>{

  @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

  private ParseDocumentImage documentImage;

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    documentImage = documentImageBus.getValue();
    subscribe(documentImageBus.subscribe(this::bindDocumentImage));
    if(documentImage != null){

      bindDocumentImage(documentImage);
    }
  }

  private void bindDocumentImage(final ParseDocumentImage documentImage){

    this.documentImage = documentImage;
    if(getView() == null) return;
    getView().floor.setText(documentImage.getFloor());
    getView().location.setText(documentImage.getLocation());
  }
  public void changeLocation(final String locationString) {

    documentImage.setLocation(locationString);
  }

  public void changeFloor(final String floor) {

    documentImage.setFloor(floor);
  }
}
