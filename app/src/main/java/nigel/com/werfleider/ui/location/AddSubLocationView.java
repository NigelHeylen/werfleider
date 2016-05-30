package nigel.com.werfleider.ui.location;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 30/05/16.
 */
public class AddSubLocationView extends FrameLayout {

  @Inject AddSubLocationScreen.AddSubLocationPresenter presenter;

  @Bind(R.id.sub_locations_recyclerview) RecyclerView recyclerView;
  private AddSubLocationAdapter adapter;

  public AddSubLocationView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if(!isInEditMode()){
      ButterKnife.bind(this);
      presenter.takeView(this);

      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      recyclerView.setAdapter(adapter = new AddSubLocationAdapter(getContext()));
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.create_view) public void addLocation(){

    presenter.addSubLocation();
  }

  public void notifyItemAdded() {

    adapter.notifyItemInserted(adapter.getItemCount());
  }
}
