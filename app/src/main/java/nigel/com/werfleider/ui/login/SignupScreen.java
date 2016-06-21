package nigel.com.werfleider.ui.login;

import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.home.HomeScreen;

import static nigel.com.werfleider.util.ParseStringUtils.COMPANY;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.PROFESSION;

/**
 * Created by nigel on 01/04/15.
 */
@Layout(R.layout.signup_view)
public class SignupScreen implements Blueprint, HasParent<LoginScreen> {


    @Override public String getMortarScopeName() {

        return getClass().getName();
    }

    @Override public Object getDaggerModule() {

        return new Module();
    }

    @Override public LoginScreen getParent() {

        return new LoginScreen();
    }

    @dagger.Module(
            injects = {
                    SignupView.class
            },
            addsTo = CorePresenter.Module.class
    )
    static class Module {

    }

    static class Presenter extends ViewPresenter<SignupView> {

        @Inject Flow flow;

        public void signUp() {

            final SignupView view = getView();

            ParseUser user = new ParseUser();
            user.setUsername(view.email.getEditableText().toString());
            user.setPassword(view.password.getEditableText().toString());
            user.setEmail(view.email.getEditableText().toString().toLowerCase());
            user.put(
                    PROFESSION,
                    view.profession.getEditableText().toString());
            user.put(
                    COMPANY,
                    view.company.getEditableText().toString());
            user.put(
                    NAME,
                    view.name.getEditableText().toString());

            user.signUpInBackground(
                    new SignUpCallback() {
                        @Override public void done(final ParseException e) {
                             if(e == null){
                                 flow.goTo(new HomeScreen());
                             } else {
                                 Toast.makeText(getView().getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                             }
                        }
                    });
        }
    }
}
