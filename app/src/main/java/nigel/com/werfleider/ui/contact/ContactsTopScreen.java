package nigel.com.werfleider.ui.contact;

import android.os.Bundle;
import com.parse.ParseUser;
import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.service.contacts.GetContacts;
import nigel.com.werfleider.service.contacts.GetContactsRx;
import nigel.com.werfleider.ui.home.HomeScreen;
import nigel.com.werfleider.util.ParseStringUtils;
import rx.subjects.PublishSubject;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 13/12/15.
 */

@Layout(R.layout.contacts_top_view) public class ContactsTopScreen implements Blueprint, HasParent<HomeScreen> {

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module();
  }

  @Override public HomeScreen getParent() {
    return new HomeScreen();
  }

  @dagger.Module(
      injects = {
          ContactsView.class, ContactsAdapter.class, ContactsPagerAdapter.class,
          ContactsTopView.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    public static final String USER_CONTACTS = "user_contacts";
    public static final String USER_DATA = "user_data";

    @Provides GetContacts provideGetContacts(final GetContactsRx getContactsRx) {
      return getContactsRx;
    }

    @Provides @Singleton @Named(USER_DATA) List<ParseUser> provideAdapterData() {

      return newArrayList();
    }

    @Provides @Singleton @Named(USER_CONTACTS) List<ParseUser> provideContacts() {

      final List<ParseUser> contacts =
          ParseUser.getCurrentUser().getList(ParseStringUtils.CONTACTS);

      return contacts != null ? contacts : newArrayList();
    }

    @Provides @Singleton PublishSubject<SocialAction> provideSocialActionBus() {

      return PublishSubject.create();
    }
  }

  static class ContactsTopPresenter extends ViewPresenter<ContactsTopView> {

    private ContactsPagerAdapter adapter;

    @Inject public ContactsTopPresenter() {
    }

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

      initView();
    }

    private void initView() {
      getView().pager.setAdapter(adapter = new ContactsPagerAdapter(getView().getContext()));
    }
  }
}
