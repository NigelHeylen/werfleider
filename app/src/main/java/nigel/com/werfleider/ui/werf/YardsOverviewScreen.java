package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.ui.home.HomeScreen;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 14/12/15.
 */

@Layout(R.layout.yards_overview_view) public class YardsOverviewScreen implements Blueprint,
    HasParent<HomeScreen> {

  @Override public String getMortarScopeName() {

    return getClass().getSimpleName();
  }

  @Override public Object getDaggerModule() {

    return newArrayList(new Module(), new YardListScreen.Module());
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

  }

  static class YardsOverviewPresenter extends ViewPresenter<YardsOverviewView> {

    private YardsOverViewAdapter adapter;

    @Inject public YardsOverviewPresenter() {
    }

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null) return;

      initView();
    }

    private void initView() {

      getView().pager.setAdapter(adapter = new YardsOverViewAdapter(getView().getContext()));
    }
  }
}
