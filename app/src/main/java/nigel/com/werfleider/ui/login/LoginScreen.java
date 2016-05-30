package nigel.com.werfleider.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.parse.ParseUser;
import flow.Flow;
import flow.Layout;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.home.HomeScreen;

import static android.content.Context.MODE_PRIVATE;
import static nigel.com.werfleider.MainActivity.EMAIL;
import static nigel.com.werfleider.MainActivity.PASSWORD;
import static nigel.com.werfleider.MainActivity.USER;

/**
 * Created by nigel on 26/03/15.
 */
@Layout(R.layout.log_in_view) public class LoginScreen implements Blueprint {

  public LoginScreen() {

  }

  @Override public String getMortarScopeName() {

    return getClass().getName();
  }

  @Override public Object getDaggerModule() {

    return new Module();
  }

  @dagger.Module(
      injects = {
          LoginView.class
      },
      addsTo = CorePresenter.Module.class) static class Module {

  }

  @Singleton static class Presenter extends ViewPresenter<LoginView> {

    @Inject Flow flow;

    @Inject ActionBarOwner actionBarOwner;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) {
        return;
      }

      actionBarOwner.setConfig(new ActionBarOwner.Config(true, false, "Login", null));
    }

    public void handleLogin(final String email, final String password) {

      ParseUser.logInInBackground(email, password, (user, e) -> {
        if (e == null) {

          final SharedPreferences preferences =
              getView().getContext().getSharedPreferences(USER, MODE_PRIVATE);
          preferences.edit().putString(EMAIL, email).putString(PASSWORD, password).commit();
          flow.goTo(new HomeScreen());
        } else {
          e.printStackTrace();
          Toast.makeText(getView().getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        getView().progressCircle.setVisibility(View.GONE);
      });
    }

    public void handleSignup() {

      flow.goTo(new SignupScreen());
    }
  }
}
