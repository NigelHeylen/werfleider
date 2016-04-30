package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 14/12/15.
 */
public class InviteContactsView extends FrameLayout {

  @Inject InviteContactsScreen.InviteContactsPresenter presenter;

  @Bind(R.id.invite_contacts_recycler_view) RecyclerView list;

  @Bind(R.id.loading_view) ProgressBarCircularIndeterminate loadingView;

  @Bind(R.id.empty_view) TextView emptyView;


  public InviteContactsView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) {
      Mortar.inject(context, this);
    }
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();

    if (!isInEditMode()) {
      ButterKnife.bind(this);
      list.setLayoutManager(new LinearLayoutManager(getContext()));
      list.setAdapter(new InvitesRecyclerAdapter(getContext()));
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  public void showContentView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(GONE);
    list.setVisibility(VISIBLE);
  }

  public void showEmptyView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(VISIBLE);
    list.setVisibility(GONE);
  }

}
