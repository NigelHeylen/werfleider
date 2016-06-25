package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 25/06/16.
 */
public class YardFLoorLocationView extends LinearLayout {

  @Inject YardFloorLocationScreen.YardFloorLocationPresenter presenter;

  @Inject FloorLocation floorLocation;

  @Bind(R.id.floor_location_new_edit_text) EditText newEditText;
  @Bind(R.id.floor_location_recyclerview) RecyclerView recyclerView;
  private FloorLocationAdapter adapter;

  public YardFLoorLocationView(Context context, AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (!isInEditMode()) {
      ButterKnife.bind(this);
      newEditText.setHint(floorLocation.name().toLowerCase());
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setAdapter(
          adapter = new FloorLocationAdapter(new FloorLocationDeleteListener(presenter)));
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  public void addData(List<String> data) {

    System.out.println("data = " + data);
    adapter.setData(data);
  }

  @OnClick(R.id.floor_location_new_edit_button) public void addNew(){

    if(!newEditText.getText().toString().isEmpty()){

      presenter.handleAdd(newEditText.getText().toString());
      newEditText.setText("");
    }
  }
}
