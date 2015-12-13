package nigel.com.werfleider.ui.contact;

import android.os.Bundle;
import dagger.Provides;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;

/**
 * Created by nigel on 13/12/15.
 */

@Layout(R.layout.contacts_view) public class ContactsScreen implements Blueprint {

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module();
  }

  @dagger.Module(
      injects = {
          ContactsView.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    @Provides GetContacts provideGetContacts(final GetContactsRx getContactsRx){
      return getContactsRx;
    }
  }

  static class ContactsPresenter extends ViewPresenter<ContactsView> {

    @Inject
    public ContactsPresenter() {
    }

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;


    }
  }
}
