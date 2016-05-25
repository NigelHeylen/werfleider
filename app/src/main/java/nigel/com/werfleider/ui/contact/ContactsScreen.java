package nigel.com.werfleider.ui.contact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.parse.ParseUser;
import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Contact;
import nigel.com.werfleider.service.contacts.GetContacts;
import nigel.com.werfleider.service.contacts.GetContactsRx;
import nigel.com.werfleider.ui.home.HomeScreen;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.util.ParseStringUtils;
import rx.subjects.PublishSubject;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.ui.contact.Contacts.ALL_USERS;
import static nigel.com.werfleider.ui.contact.ContactsScreen.Module.USER_DATA;

/**
 * Created by nigel on 13/12/15.
 */

@Layout(R.layout.contacts_view) public class ContactsScreen
    implements Blueprint, HasParent<HomeScreen> {

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
          ContactsView.class, ContactsAdapter.class
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

    @Provides @Singleton @Named(USER_CONTACTS) List<Contact> provideContacts() {

      final List<Contact> contacts =
          ParseUser.getCurrentUser().getList(ParseStringUtils.CONTACTS);

      return contacts != null ? contacts : newArrayList();
    }

    @Provides @Singleton PublishSubject<SocialAction> provideSocialActionBus() {

      return PublishSubject.create();
    }
  }

  static class ContactsPresenter extends ReactiveViewPresenter<ContactsView> {

    @Inject GetContacts getContacts;

    @Inject @Named(USER_DATA) List<ParseUser> adapterData;

    @Inject PublishSubject<SocialAction> socialActionBus;

    private ContactsAdapter adapter;

    private Contacts contacts;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null || contacts == null) return;

      initView();

      loadData();
    }

    private void initView() {

      getView().list.setLayoutManager(new LinearLayoutManager(getView().getContext()));
      getView().list.setAdapter(adapter = new ContactsAdapter(getView().getContext(), contacts));
    }

    public void handleRefresh() {

      loadData();
    }

    private void loadData() {

      subscribe(getContacts.getUsers().subscribe(this::bindRows, this::handleError));
    }

    private void handleError(Throwable throwable) {

      throwable.printStackTrace();
      finishRefreshing();
    }

    private void bindRows(final List<ParseUser> users) {

      if (!adapterData.isEmpty()) {

        adapterData.clear();
      }

      adapter.notifyDataSetChanged();
      adapterData.addAll(users);
      finishRefreshing();
    }

    public void setContacts(Contacts contacts) {
      this.contacts = contacts;
      initView();

      if (contacts.equals(ALL_USERS)) {

        loadData();
      }

      subscribe(socialActionBus.subscribe(action -> {
        adapter.notifyDataSetChanged();
      }));
    }

    private void finishRefreshing() {
      if(getView() != null) {
        getView().swipeRefreshLayout.setRefreshing(false);
      }
    }
  }
}
