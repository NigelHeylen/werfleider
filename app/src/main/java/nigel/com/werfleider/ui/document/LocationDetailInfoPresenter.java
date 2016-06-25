package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import javax.inject.Inject;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import rx.subjects.BehaviorSubject;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailInfoPresenter extends ReactiveViewPresenter<LocationDetailInfoView> {

  @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

  private ParseDocumentImage documentImage;

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    documentImage = documentImageBus.getValue();
    subscribe(documentImageBus.subscribe(this::bindDocumentImage));
    if (documentImage != null) {

      bindDocumentImage(documentImage);
    }
  }

  private void bindDocumentImage(final ParseDocumentImage documentImage) {

    getView().unsubscribeToSubscriptions();

    this.documentImage = documentImage;
    if (getView() != null) {

      System.out.println("documentImage.getFloor() = " + documentImage.getFloor());
      System.out.println("documentImage.getLocation() = " + documentImage.getLocation());
      getView().setFloor(documentImage.getFloor());
      getView().setLocation(documentImage.getLocation());
      getView().subscribeToSpinnerSubscriptions();
    }
  }

  public void setFloor(String floor) {
    System.out.println("floor = " + floor);
    if (documentImage != null) documentImage.setFloor(floor);
  }

  public void setLocation(String location) {
    System.out.println("location = " + location);
    if (documentImage != null) documentImage.setLocation(location);
  }
}
