package nigel.com.werfleider.ui.location;

import android.os.Bundle;
import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Location;
import nigel.com.werfleider.model.SubLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 30/05/16.
 */

@Layout(R.layout.add_sublocation_view) public class AddSubLocationScreen implements Blueprint,
    HasParent<AddLocationsScreen> {

  private final Yard yard;
  private final Location location;

  public AddSubLocationScreen(Yard yard, Location location) {
    this.yard = yard;

    this.location = location;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module(location);
  }

  @Override public AddLocationsScreen getParent() {
    return new AddLocationsScreen(yard);
  }

  @dagger.Module(
      injects = {
          AddSubLocationView.class, AddSubLocationAdapter.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final Location location;

    public Module(Location location) {
      this.location = location;
    }

    @Provides Location location(){
      return location;
    }
  }

  static class AddSubLocationPresenter extends ReactiveViewPresenter<AddSubLocationView> {

    @Inject Location location;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;
    }

    public void addSubLocation() {
      final List<SubLocation> subLocations = newArrayList(location.getSubLocations());
      final SubLocation subLocation = new SubLocation();
      subLocation.pinInBackground();
      subLocations.add(subLocation);
      location.setSubLocations(subLocations);
      getView().notifyItemAdded();
    }
  }
}
