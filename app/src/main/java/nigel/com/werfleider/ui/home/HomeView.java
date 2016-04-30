package nigel.com.werfleider.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;

/**
 * Created by nigel on 11/02/15.
 */
public class HomeView extends RelativeLayout {

  @Bind(R.id.home_welcome_text) TextView welcome;

  @Inject HomeScreen.Presenter presenter;

  @Inject ActionBarOwner actionBarOwner;

  public HomeView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);

      actionBarOwner.setConfig(
          new ActionBarOwner.Config(true, false, getContext().getString(R.string.app_name),
              new ActionBarOwner.MenuAction(getContext().getString(R.string.tLogOut),
                  () -> presenter.handleLogOut(), R.drawable.ic_logout)));
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.Home_Button_Contacts) public void goToContacts() {
    presenter.goToContactsScreen();
  }

  @OnClick(R.id.Home_Button_Werf) public void goToWerven() {
    presenter.goToWerfScreen();
  }

}
