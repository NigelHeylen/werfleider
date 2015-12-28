package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 14/12/15.
 */
public class InviteContactsView extends RelativeLayout
    implements SwipeRefreshLayout.OnRefreshListener {

  @Inject InviteContactsScreen.InviteContactsPresenter presenter;

  @Bind(R.id.invite_contacts_recycler_view) RecyclerView list;

  @Bind(R.id.invite_contacts_swipe_to_refresh) SwipeRefreshLayout swipeRefreshLayout;

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

      swipeRefreshLayout.setOnRefreshListener(this);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @Override public void onRefresh() {

    presenter.handleRefresh();
  }
}
