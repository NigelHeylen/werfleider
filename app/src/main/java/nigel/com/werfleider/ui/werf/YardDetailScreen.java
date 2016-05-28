package nigel.com.werfleider.ui.werf;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import javax.inject.Singleton;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewAdapter;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewView;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.yard_detail) public class YardDetailScreen
    implements Blueprint, HasParent<YardsOverviewScreen> {

  final Yard yard;

  public YardDetailScreen(final Yard yard) {

    this.yard = yard;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName()
        + "{yard_id: "
        + yard.getObjectId()
        + ", yard_name: "
        + yard.getNaam()
        + "}";
  }

  @Override public Object getDaggerModule() {

    return newArrayList(new Module(yard), new InviteContactsScreen.Module(yard));
  }

  @Override public YardsOverviewScreen getParent() {

    return new YardsOverviewScreen();
  }

  @dagger.Module(
      injects = {
          YardDetailView.class, YardDetailDocumentAdapter.class, ParseDocumentOverviewView.class,
          ParseDocumentOverviewAdapter.class
      }, addsTo = CorePresenter.Module.class) static class Module {

    private final Yard werf;

    public Module(final Yard werf) {

      this.werf = werf;
    }

    @Provides @Singleton Yard provideWerf() {

      return werf;
    }
  }
}
