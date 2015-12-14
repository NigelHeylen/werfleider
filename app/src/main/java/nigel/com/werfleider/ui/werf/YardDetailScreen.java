package nigel.com.werfleider.ui.werf;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.ui.document.DocumentOverviewScreen;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewAdapter;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewView;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.yard_detail) public class YardDetailScreen
    implements Blueprint, HasParent<WerfScreen> {

  final ParseYard yard;

  public YardDetailScreen(final ParseYard yard) {

    this.yard = yard;
  }

  @Override public String getMortarScopeName() {

    return getClass().getName() + ": " + yard.getObjectId() + ": " + yard.getNaam();
  }

  @Override public Object getDaggerModule() {

    return newArrayList(new Module(yard), new InviteContactsScreen.Module(yard));
  }

  @Override public WerfScreen getParent() {

    return new WerfScreen();
  }

  @dagger.Module(
      injects = {
          YardDetailView.class, YardDetailDocumentAdapter.class, ParseDocumentOverviewView.class,
          ParseDocumentOverviewAdapter.class
      }, addsTo = CorePresenter.Module.class)
  static class Module {

    private final ParseYard werf;

    public Module(final ParseYard werf) {

      this.werf = werf;
    }

    @Provides @Singleton ParseYard provideWerf() {

      return werf;
    }
  }

  @Singleton static class Presenter extends ViewPresenter<YardDetailView> {

    @Inject Flow flow;

    @Inject ParseYard werf;

    public void goToDocumentView(final DocumentType type) {

      flow.goTo(new DocumentOverviewScreen(werf, type));
    }
  }
}
