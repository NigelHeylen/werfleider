package nigel.com.werfleider.ui.werf;

import android.view.View;

/**
 * Created by nigel on 25/06/16.
 */
public class FloorLocationDeleteListener implements View.OnClickListener {

  private int index = 0;

  final YardFloorLocationScreen.YardFloorLocationPresenter presenter;

  public FloorLocationDeleteListener(YardFloorLocationScreen.YardFloorLocationPresenter presenter) {
    this.presenter = presenter;
  }

  @Override public void onClick(View v) {

    presenter.handleDelete(index);
  }

  public FloorLocationDeleteListener setIndex(int index) {
    this.index = index;
    return this;
  }
}
