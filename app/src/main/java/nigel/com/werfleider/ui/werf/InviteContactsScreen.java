package nigel.com.werfleider.ui.werf;

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
import nigel.com.werfleider.model.Yard;
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

  final Yard yard;

  public InviteContactsScreen(Yard yard) {
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

    private final Yard yard;

    public Module(final Yard yard) {
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

    @Provides @Named(INVITES_YARD) Yard parseYard(){

      return yard;
    }

  }

  @Singleton static class InviteContactsPresenter extends ReactiveViewPresenter<InviteContactsView> {

    @Inject public InviteContactsPresenter() {
    }

    public void handleRefresh() {

    }
  }
}
