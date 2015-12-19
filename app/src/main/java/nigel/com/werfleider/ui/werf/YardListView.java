package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 31/01/15.
 */
public class YardListView extends RelativeLayout {

  @Bind(R.id.yard_list) RecyclerView werfList;

  @Bind(R.id.yard_create) AddFloatingActionButton create;

  @Inject YardListScreen.Presenter presenter;

  public YardListView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();
    ButterKnife.bind(this);
    presenter.takeView(this);
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.yard_create) public void createWerf() {

    presenter.handleCreate();
  }

  public void setLayoutManager(final LinearLayoutManager linearLayoutManager) {
    werfList.setLayoutManager(linearLayoutManager);
  }

  public void setAdapter(final YardAdapter adapter) {
    werfList.setAdapter(adapter);
  }

  public void setYardType(YardType yardType) {
    presenter.setYardType(yardType);
  }
}
