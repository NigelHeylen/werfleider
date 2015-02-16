package nigel.com.werfleider.ui.profession;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.contact.ContactDbHelper;
import nigel.com.werfleider.model.Contact;
import nigel.com.werfleider.model.Profession;
import nigel.com.werfleider.ui.contacts.ContactScreen;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 11/02/15.
 */
@Layout(R.layout.profession_list_view)
public class ProfessionListScreen implements Blueprint, HasParent<ContactScreen> {

    private final Profession profession;

    public ProfessionListScreen(final Profession profession) {
        this.profession = profession;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module(profession);
    }

    @Override public ContactScreen getParent() {
        return new ContactScreen();
    }

    @dagger.Module(
            injects = ProfessionListView.class,
            addsTo = CorePresenter.Module.class
    )
    static class Module {

        private final Profession profession;

        public Module(final Profession profession) {

            this.profession = profession;
        }

        @Provides Profession provideProfession() {
            return profession;
        }
    }

    static class Presenter extends ViewPresenter<ProfessionListView> {

        @Inject Profession profession;

        @Inject ContactDbHelper contactDbHelper;

        private ContactListAdapter adapter;

        private List<Contact> adapterData = newArrayList();

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

            initView();

            Observable.just(
                    contactDbHelper.getAllContactsByProfession(profession)
                           )
                      .doOnTerminate(
                              new Action0() {
                                  @Override public void call() {
                                      contactDbHelper.closeDB();
                                  }
                              })
                      .subscribe(
                              new Observer<List<Contact>>() {
                                  @Override public void onCompleted() {

                                  }

                                  @Override public void onError(final Throwable e) {
                                      e.printStackTrace();
                                  }

                                  @Override public void onNext(final List<Contact> contacts) {
                                      adapterData.addAll(contacts);
                                      adapter.notifyDataSetChanged();
                                  }
                              });

        }

        private void initView() {
            getView().viewTitle.setText(profession.name().toLowerCase());
            getView().contactList.setLayoutManager(new LinearLayoutManager(getView().getContext()));
            getView().contactList.setAdapter(adapter = new ContactListAdapter(adapterData));
        }


        public void addUser(final String userName, final String userEmail) {
            final Contact contact =
                    new Contact()
                            .setNaam(userName)
                            .setEmail(userEmail)
                            .setProfession(profession);

            if (contactDbHelper.createContact(contact) == -1) {
                getView().showToast("Email already exists.");
            } else {
                getView().showToast("Success, contact created.");

                adapterData.add(contact);
                adapter.notifyItemInserted(adapterData.size() - 1);
            }
        }
    }
}
