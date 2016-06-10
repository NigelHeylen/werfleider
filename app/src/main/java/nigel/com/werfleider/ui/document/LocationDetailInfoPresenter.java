package nigel.com.werfleider.ui.document;

import android.content.SharedPreferences;
import android.os.Bundle;
import javax.inject.Inject;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import rx.subjects.BehaviorSubject;

import static nigel.com.werfleider.util.StringUtils.EMPTY;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailInfoPresenter extends ReactiveViewPresenter<LocationDetailInfoView>{

  public static final String LAST_FLOOR = "last_floor";
  public static final String LAST_LOCATION = "last_location";

  @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

  @Inject SharedPreferences preferences;

  private ParseDocumentImage documentImage;

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    documentImage = documentImageBus.getValue();
    subscribe(documentImageBus.subscribe(this::bindDocumentImage));
    if(documentImage != null){

      bindDocumentImage(documentImage);
    }

    getView().setLastFloor(preferences.getString(LAST_FLOOR, EMPTY));
    getView().setLastLocation(preferences.getString(LAST_LOCATION, EMPTY));
  }

  private void bindDocumentImage(final ParseDocumentImage documentImage){

    getView().unsubscribeToSubscriptions();

    this.documentImage = documentImage;
    if(getView() == null) return;
    getView().floor.setText(documentImage.getFloor());
    getView().location.setText(documentImage.getLocation());

    getView().subscribeToEditTextSubscriptions();
  }

  public void setFloor(String floor) {
    documentImage.setFloor(floor);

    preferences.edit().putString(LAST_FLOOR, floor).apply();
    getView().setLastFloor(floor);
  }

  public void setLocation(String location) {
    documentImage.setLocation(location);
    preferences.edit().putString(LAST_LOCATION, location).apply();

    getView().setLastLocation(location);
  }
}
