package nigel.com.werfleider.ui.contact;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 13/12/15.
 */
public class ContactsView extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {

  @Inject ContactsScreen.ContactsPresenter presenter;

  @Bind(R.id.contacts_swipe_to_refrsh) SwipeRefreshLayout swipeRefreshLayout;

  @Bind(R.id.contacts_list) RecyclerView list;

  public ContactsView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) {
      Mortar.inject(context, this);
    }
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();

    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);

      swipeRefreshLayout.setOnRefreshListener(this);
      swipeRefreshLayout.setRefreshing(true);
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

  public void setContacts(final Contacts contacts) {
    presenter.setContacts(contacts);
  }
}
