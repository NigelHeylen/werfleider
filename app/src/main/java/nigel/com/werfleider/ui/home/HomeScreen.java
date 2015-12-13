package nigel.com.werfleider.ui.home;

import android.os.Bundle;
import android.widget.Toast;
import com.parse.ParseUser;
import flow.Flow;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.contact.ContactsScreen;
import nigel.com.werfleider.ui.login.LoginScreen;
import nigel.com.werfleider.ui.werf.WerfScreen;

import static java.lang.String.format;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;

/**
 * Created by nigel on 15/02/15.
 */
@Layout(R.layout.home_view)
public class HomeScreen implements Blueprint {

    @Override public String getMortarScopeName() {

        return getClass().getName();
    }

    @Override public Object getDaggerModule() {

        return new Module();
    }


    @dagger.Module(addsTo = CorePresenter.Module.class,
                   injects = HomeView.class)
    static class Module {

    }

    static class Presenter extends ViewPresenter<HomeView> {

        @Inject Flow flow;

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);

            if (ParseUser.getCurrentUser() != null) {
                getView().welcome.setText(
                        format(
                                "Welcome %s",
                                ParseUser.getCurrentUser().get(NAME)));
            }
        }

        public void goToContactsScreen() {

          flow.goTo(new ContactsScreen());

        }

        public void goToWerfScreen() {

            flow.goTo(new WerfScreen());
        }

        public void handleLogOut() {

            ParseUser.logOutInBackground(e -> {
              if (e == null) {
                Toast.makeText(getView().getContext(), "Logged out", Toast.LENGTH_LONG)
                    .show();

                flow.goTo(new LoginScreen());
              } else {
                Toast.makeText(getView().getContext(), e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
              }
            });

        }
    }

}
