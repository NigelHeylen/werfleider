package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Yard;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.util.StringUtils.EMPTY;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailInfoView extends LinearLayout {

  @Inject LocationDetailInfoPresenter presenter;

  @Inject Yard yard;

  @Bind(R.id.document_detail_floor) Spinner floor;

  @Bind(R.id.document_detail_location) Spinner location;

  private Subscription floorSubscription = Subscriptions.empty();
  private Subscription locationSubscription = Subscriptions.empty();
  private ArrayAdapter<String> floorAdapter;
  private ArrayAdapter<String> locationAdapter;

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

      final List<String> floors = newArrayList(yard.getFloors());
      if (!floors.contains(EMPTY)) floors.add(0, EMPTY);
      floor.setAdapter(floorAdapter =
          new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, floors));

      final List<String> locations = newArrayList(yard.getLocations());
      if (!locations.contains(EMPTY)) locations.add(0, EMPTY);
      location.setAdapter(locationAdapter =
          new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
              locations));

      subscribeToSpinnerSubscriptions();
    }
  }

  protected void subscribeToSpinnerSubscriptions() {
    floorSubscription = RxAdapterView.itemSelections(floor)
        .skip(1)
        .map(floorAdapter::getItem)
        .subscribe(presenter::setFloor);
    locationSubscription = RxAdapterView.itemSelections(location)
        .skip(1)
        .map(locationAdapter::getItem)
        .subscribe(presenter::setLocation);
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

  public void setFloor(String floor) {

    final int index = yard.getFloors().indexOf(floor);
    System.out.println(index + " LocationDetailInfoView.setFloor " + floor);
    this.floor.setSelection(Math.max(0, index + 1));
  }

  public void setLocation(String location) {

    final int index = yard.getLocations().indexOf(location);
    System.out.println(index + " LocationDetailInfoView.setLocation " + location);
    this.location.setSelection(Math.max(0, index + 1));
  }
}
