package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;

import static nigel.com.werfleider.ui.werf.FloorLocation.VERDIEPING;

/**
 * Created by nigel on 25/06/16.
 */
@Layout(R.layout.yard_floor_location_view) public class YardFloorLocationScreen implements Blueprint, HasParent<Blueprint> {

  final Yard yard;

  final Blueprint parent;
  private final FloorLocation floorLocation;

  public YardFloorLocationScreen(Yard yard, Blueprint parent, FloorLocation floorLocation) {
    this.yard = yard;
    this.parent = parent;
    this.floorLocation = floorLocation;
  }

  @Override public String getMortarScopeName() {
    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {
    return new Module(yard, floorLocation);
  }

  @Override public Blueprint getParent() {
    return parent;
  }

  @dagger.Module(injects = YardFLoorLocationView.class, addsTo = CorePresenter.Module.class)
  static class Module {

    private final Yard yard;
    private final FloorLocation floorLocation;

    public Module(Yard yard, FloorLocation floorLocation) {

      this.yard = yard;
      this.floorLocation = floorLocation;
    }

    @Provides Yard provideYard() {
      return yard;
    }

    @Provides FloorLocation floorLocation() {
      return floorLocation;
    }
  }

  static class YardFloorLocationPresenter
      extends ReactiveViewPresenter<YardFLoorLocationView> {

    @Inject Yard yard;

    @Inject FloorLocation floorLocation;

    @Inject ParseErrorHandler parseErrorHandler;

    @Override protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      if(getView() == null) return;

      if(floorLocation == VERDIEPING){

        getView().addData(yard.getFloors());
      } else {

        getView().addData(yard.getLocations());
      }
    }

    @Override protected void onExitScope() {
      super.onExitScope();

      yard.saveEventually(e -> {
        if (e != null) parseErrorHandler.handleParseError(e);
      });
    }

    public void handleAdd(final String text){

      if(floorLocation == VERDIEPING){

        final List<String> floors = yard.getFloors();
        floors.add(text);
        yard.putFloors(floors);
        getView().addData(floors);
      } else {

        final List<String> locations = yard.getLocations();
        locations.add(text);
        yard.putLocations(locations);
        getView().addData(locations);
      }
    }

    public void handleDelete(int index) {

      if(floorLocation == VERDIEPING){

        final List<String> floors = yard.getFloors();
        floors.remove(index);
        yard.putFloors(floors);
        getView().addData(floors);
      } else {

        final List<String> locations = yard.getLocations();
        locations.remove(index);
        yard.putLocations(locations);
        getView().addData(locations);
      }
    }
  }
}
