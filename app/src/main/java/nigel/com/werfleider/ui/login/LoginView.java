package nigel.com.werfleider.ui.login;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 26/03/15.
 */
public class LoginView extends RelativeLayout {

    @Inject LoginScreen.Presenter presenter;

    @InjectView(R.id.login_button_login) ButtonFlat logIn;

    @InjectView(R.id.login_button_signup) ButtonFlat signUp;

//    @InjectView(R.id.log_in_log_out_btn) ButtonRectangle logOut;

    @InjectView(R.id.login_email) MaterialEditText email;

    @InjectView(R.id.login_password) MaterialEditText password;

    @InjectView(R.id.log_in_progress) ProgressBarCircularIndeterminate progressCircle;

    public LoginView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
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

    @OnClick(R.id.login_button_login)
    public void facebook() {

        if (email.getText().length() > 0 && password.getText().length() > 0){

            progressCircle.setVisibility(VISIBLE);

            presenter.handleLogin(email.getText().toString(), password.getText().toString());
        } else {

            Toast.makeText(getContext(), "Enter email and password", Toast.LENGTH_LONG).show();
        }

    }

    @OnClick(R.id.login_button_signup)
    public void logOut() {

        presenter.handleSignup();
    }
}
