package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 31/01/15.
 */
public class YardListView extends FrameLayout {

  @Bind(R.id.yard_list) RecyclerView werfList;

  @Bind(R.id.create_view) AddFloatingActionButton create;

  @Bind(R.id.loading_view) ProgressBarCircularIndeterminate loadingView;

  @Bind(R.id.empty_view) TextView emptyView;

  @BindString(R.string.tNoInvitedYards) String tNoInvitedYards;

  @Inject YardListScreen.Presenter presenter;

  public YardListView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();
    if(!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);

    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.create_view) public void createWerf() {

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

    if(yardType == YardType.INVITED){

      emptyView.setText(tNoInvitedYards);
    }
  }

  public void showContentView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(GONE);
    werfList.setVisibility(VISIBLE);
  }

  public void showEmptyView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(VISIBLE);
    werfList.setVisibility(GONE);
  }
}
