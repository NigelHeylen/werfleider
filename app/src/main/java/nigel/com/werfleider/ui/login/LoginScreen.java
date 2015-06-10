package nigel.com.werfleider.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.otto.Bus;

import javax.inject.Inject;
import javax.inject.Singleton;

import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.home.HomeScreen;

/**
 * Created by nigel on 26/03/15.
 */
@Layout(R.layout.log_in_view)
public class LoginScreen implements Blueprint {

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
            addsTo = CorePresenter.Module.class
    )
    static class Module {

    }

    @Singleton
    static class Presenter extends ViewPresenter<LoginView> {

        @Inject Bus bus;

        @Inject Flow flow;

        @Inject Presenter(final Bus bus) {

            bus.register(this);
            this.bus = bus;
        }

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

        }


        @Override protected void onExitScope() {

            super.onExitScope();
            bus.unregister(this);
        }

//        public void handleLogOut() {
//
//            ParseUser.logOutInBackground(
//                    new LogOutCallback() {
//                        @Override public void done(final ParseException e) {
//
//                            if (e == null) {
//                                getView().twitter.setVisibility(
//                                        VISIBLE);
//                                getView().facebook.setVisibility(VISIBLE);
//                                getView().logOut.setVisibility(
//                                        INVISIBLE);
//                                getView().profilePicture.setVisibility(GONE);
//
//                                getView().welcomeText.setText(translations.getTranslation(R.string.tLogIn));
//                            }
//
//                            getView().progressCircle.setVisibility(INVISIBLE);
//                        }
//                    });
//        }

        public void handleLogin(final String email, final String password) {

            ParseUser.logInInBackground(
                    email,
                    password,
                    new LogInCallback() {
                        @Override public void done(final ParseUser user, final ParseException e) {
                             if(e == null){
                                 Toast.makeText(getView().getContext(), "Logged in", Toast.LENGTH_LONG).show();
                                 flow.goTo(new HomeScreen());
                             } else {
                                 Toast.makeText(getView().getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                             }

                            getView().progressCircle.setVisibility(
                                    View.GONE);
                        }
                    });
        }

        public void handleSignup() {

            flow.goTo(new SignupScreen());
        }
    }
}
