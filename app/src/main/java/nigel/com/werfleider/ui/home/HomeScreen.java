package nigel.com.werfleider.ui.home;

import javax.inject.Inject;

import flow.Flow;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.contacts.ContactScreen;
import nigel.com.werfleider.ui.werf.WerfScreen;

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


    @dagger.Module(addsTo = CorePresenter.Module.class, injects = HomeView.class)
    static class Module {

    }

    static class Presenter extends ViewPresenter<HomeView> {

        @Inject Flow flow;


        public void goToContactsScreen() {
            flow.goTo(new ContactScreen());
        }

        public void goToWerfScreen() {
            flow.goTo(new WerfScreen());
        }
    }

}
