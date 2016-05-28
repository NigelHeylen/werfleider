package nigel.com.werfleider.ui.werf;

import com.google.common.collect.Iterables;
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
  final YardType yardType;

  final int tab;

  public YardDetailScreen(final Yard yard, int tab) {

    this.yard = yard;
    this.tab = tab;
    this.yardType = null;
  }

  public YardDetailScreen(Yard werf, YardType yardType) {
    yard = werf;
    this.yardType = yardType;

    tab = 0;
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

    return newArrayList(new Module(yard, tab), new InviteContactsScreen.Module(yard));
  }

  @Override public YardsOverviewScreen getParent() {

    return new YardsOverviewScreen(Iterables.indexOf(newArrayList(YardType.values()), type -> type.equals(yardType)));
  }

  @dagger.Module(
      injects = {
          YardDetailView.class, YardDetailDocumentAdapter.class, ParseDocumentOverviewView.class,
          ParseDocumentOverviewAdapter.class
      }, addsTo = CorePresenter.Module.class) static class Module {

    private final Yard werf;
    private final int tab;

    public Module(final Yard werf, int tab) {

      this.werf = werf;
      this.tab = tab;
    }

    @Provides @Singleton Yard provideWerf() {

      return werf;
    }

    @Provides int tabIndex(){
      return tab;
    }
  }
}
