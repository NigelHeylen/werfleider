package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailInfoView extends LinearLayout {

  @Inject LocationDetailInfoPresenter presenter;

  @Bind(R.id.document_detail_floor) EditText floor;

  @Bind(R.id.document_detail_location) EditText location;

  @Bind(R.id.document_detail_floor_last) TextView lastFloor;
  @Bind(R.id.document_detail_location_last) TextView lastLocation;

  private Subscription floorSubscription = Subscriptions.empty();
  private Subscription locationSubscription = Subscriptions.empty();

  public LocationDetailInfoView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) {
      Mortar.inject(context, this);
    }
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();

    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);

      subscribeToEditTextSubscriptions();
    }
  }

  protected void subscribeToEditTextSubscriptions() {
    floorSubscription = getEditTextSubscription(floor, presenter::setFloor);
    locationSubscription = getEditTextSubscription(location, presenter::setLocation);
  }

  private Subscription getEditTextSubscription(EditText editText, Action1<String> action) {
    return RxTextView.textChanges(editText)
        .skip(1)
        .debounce(1, TimeUnit.SECONDS)
        .map(CharSequence::toString)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(action);
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
    unsubscribeToSubscriptions();
  }

  protected void unsubscribeToSubscriptions() {
    floorSubscription.unsubscribe();
    locationSubscription.unsubscribe();
  }

  public void setLastFloor(String floor) {

    if(isNullOrEmpty(floor)){
      lastFloor.setVisibility(GONE);
    } else {
      lastFloor.setVisibility(VISIBLE);
      lastFloor.setText(format("Laatste verdieping:\n%s", floor));
      lastFloor.setOnClickListener(v -> this.floor.setText(floor));
    }
  }

  public void setLastLocation(String location) {

    if(isNullOrEmpty(location)){
      lastLocation.setVisibility(GONE);
    } else {
      lastLocation.setVisibility(VISIBLE);
      lastLocation.setText(format("Laatste locatie:\n%s", location));
      lastLocation.setOnClickListener(v -> this.location.setText(location));
    }
  }
}
