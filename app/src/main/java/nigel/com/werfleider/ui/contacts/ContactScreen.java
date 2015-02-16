package nigel.com.werfleider.ui.contacts;

import android.os.Bundle;

import javax.inject.Inject;

import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.contact.ContactDbHelper;
import nigel.com.werfleider.model.Profession;
import nigel.com.werfleider.ui.home.HomeScreen;
import nigel.com.werfleider.ui.profession.ProfessionListScreen;

import static java.lang.String.format;
import static nigel.com.werfleider.model.Profession.ARCHITECT;
import static nigel.com.werfleider.model.Profession.BOUWMEESTER;

/**
 * Created by nigel on 11/02/15.
 */
@Layout(R.layout.contacts_view)
public class ContactScreen implements Blueprint, HasParent<HomeScreen> {

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

    @Override public HomeScreen getParent() {
        return new HomeScreen();
    }

    @dagger.Module(
            injects = ContactView.class,
            addsTo = CorePresenter.Module.class
    )
    static class Module {

    }

    static class Presenter extends ViewPresenter<ContactView> {

        @Inject Flow flow;

        @Inject ContactDbHelper contactDbHelper;

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            getView().architectCount.setText(getCountFormat(contactDbHelper.getContactCount(ARCHITECT)));
            getView().bouwmeesterCount.setText(getCountFormat(contactDbHelper.getContactCount(BOUWMEESTER)));
        }

        private String getCountFormat(final int contactCount) {
            return format("%d contacts", contactCount);
        }

        public void goToProfessionDetailView(final Profession profession) {
            flow.goTo(new ProfessionListScreen(profession));
        }
    }
}
