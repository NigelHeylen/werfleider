package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.parse.ParseUser;
import dagger.Provides;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.util.ParseStringUtils;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 14/12/15.
 */

@Layout(R.layout.invite_contacts_view) public class InviteContactsScreen implements Blueprint {

  public static final String CONTACTS = "contacts";
  public static final String INVITES = "invites";
  public static final String INVITES_YARD = "invites";

  final ParseYard yard;

  public InviteContactsScreen(ParseYard yard) {
    this.yard = yard;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return new Module(yard);
  }

  @dagger.Module(
      injects = {
          InviteContactsView.class, InvitesRecyclerAdapter.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final ParseYard yard;

    public Module(final ParseYard yard) {
      this.yard = yard;
    }

    @Provides @Singleton @Named(CONTACTS) List<ParseUser> provideContacts(){

      final List<ParseUser> contacts =
          ParseUser.getCurrentUser().getList(ParseStringUtils.CONTACTS);

      return contacts != null ? contacts : newArrayList();
    }


    @Provides @Singleton @Named(INVITES) List<ParseUser> provideInvites(){
      final List<ParseUser> invites =
          yard.getList(ParseStringUtils.INVITES);

      return invites != null ? invites : newArrayList();
    }

    @Provides @Named(INVITES_YARD) ParseYard parseYard(){

      return yard;
    }

  }

  static class InviteContactsPresenter extends ReactiveViewPresenter<InviteContactsView> {

    @Inject @Named(CONTACTS) List<ParseUser> contacts;
    @Inject @Named(INVITES) List<ParseUser> invites;

    @Inject @Named(INVITES_YARD) ParseYard yard;

    private InvitesRecyclerAdapter adapter;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

      initView();
    }

    private void initView() {

      getView().list.setLayoutManager(new LinearLayoutManager(getView().getContext()));
      getView().list.setAdapter(adapter = new InvitesRecyclerAdapter(getView().getContext()));
    }

    public void handleRefresh() {


    }
  }
}
