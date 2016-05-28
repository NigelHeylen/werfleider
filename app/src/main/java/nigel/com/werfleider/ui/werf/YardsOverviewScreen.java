package nigel.com.werfleider.ui.werf;

import dagger.Provides;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.home.HomeScreen;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 14/12/15.
 */

@Layout(R.layout.yards_overview_view) public class YardsOverviewScreen implements Blueprint,
    HasParent<HomeScreen> {

  final int tab;

  public YardsOverviewScreen() {
    tab = 0;
  }

  public YardsOverviewScreen(int tab) {
    this.tab = tab;
  }

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return newArrayList(new Module(tab), new YardListScreen.Module());
  }

  @Override public HomeScreen getParent() {
    return new HomeScreen();
  }

  @dagger.Module(
      injects = {
          YardsOverviewView.class
      },
      includes = {
      },
      addsTo = CorePresenter.Module.class) static class Module {

    private final int tab;

    public Module(int tab) {
      this.tab = tab;
    }

    @Provides int tab(){
      return tab;
    }
  }
}
