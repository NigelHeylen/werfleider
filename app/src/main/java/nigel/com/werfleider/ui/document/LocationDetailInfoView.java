package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import java.util.ArrayList;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Location;
import nigel.com.werfleider.model.SubLocation;
import nigel.com.werfleider.model.Yard;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

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

      final ArrayList<String> locations = newArrayList(
          filter(transform(yard.getLocations(), Location::getName), s -> !isNullOrEmpty(s)));

      floor.setAdapter(floorAdapter =
          new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,
              android.R.id.text1, locations));

      final Observable<Integer> selection = RxAdapterView.itemSelections(floor).skip(1);

      selection.subscribe(index -> {

        presenter.setFloor(floorAdapter.getItem(index));

        setLocationAdapter(index);

        locationSubscription = RxAdapterView.itemSelections(location).skip(1)
            .map(locationAdapter::getItem)
            .subscribe(presenter::setLocation);
      });

      presenter.takeView(this);
    }
  }

  private void setLocationAdapter(Integer position) {
    floorSubscription = Observable.just(position)
        .map(index -> yard.getLocations().get(index))
        .map(Location::getSubLocations)
        .map(list -> newArrayList(
            filter(transform(list, SubLocation::getName), s -> !isNullOrEmpty(s))))
        .subscribe(list -> {

          location.setAdapter(locationAdapter =
              new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line,
                  android.R.id.text1, list));
        });
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
    floorSubscription.unsubscribe();
    locationSubscription.unsubscribe();
  }
}
