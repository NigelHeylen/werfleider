package nigel.com.werfleider.ui.location;

import android.os.Bundle;
import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import java.util.ArrayList;
import javax.inject.Inject;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Location;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.ui.werf.YardCreateScreen;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 29/05/16.
 */

@Layout(R.layout.add_locations_view) public class AddLocationsScreen implements Blueprint,
    HasParent<YardCreateScreen> {

  final Yard yard;

  public AddLocationsScreen(Yard yard) {
    this.yard = yard;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module(yard);
  }

  @Override public YardCreateScreen getParent() {
    yard.saveEventually();
    return new YardCreateScreen(yard);
  }

  @dagger.Module(
      injects = {
          AddLocationsView.class, AddLocationsAdapter.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final Yard yard;

    public Module(Yard yard) {
      this.yard = yard;
    }

    @Provides Yard provideYard(){
      return yard;
    }
  }

  static class AddLocationsPresenter extends ReactiveViewPresenter<AddLocationsView> {

    @Inject Yard yard;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

    }

    public void addLocation() {
      final ArrayList<Location> locations = newArrayList(yard.getLocations());
      final Location location = new Location();
      location.pinInBackground();
      locations.add(location);
      yard.setLocations(locations);
      getView().notifyItemAdded();
    }
  }
}
