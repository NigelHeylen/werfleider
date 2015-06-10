package nigel.com.werfleider.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 11/02/15.
 */
public class HomeView extends RelativeLayout {

    @InjectView(R.id.home_welcome_text) TextView welcome;

    @Inject HomeScreen.Presenter presenter;

    public HomeView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }

    @OnClick(R.id.Home_Button_Contacts)
    public void goToContacts(){
        presenter.goToContactsScreen();
    }

    @OnClick(R.id.Home_Button_Werf)
    public void goToWerven(){
        presenter.goToWerfScreen();
    }

    @OnClick(R.id.home_welcome_log_out)
    public void logOut(){
        presenter.handleLogOut();
    }
}
