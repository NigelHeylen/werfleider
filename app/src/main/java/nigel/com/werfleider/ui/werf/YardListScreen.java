package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.home.HomeScreen;

import static android.view.View.GONE;
import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.ui.werf.YardType.*;
import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.INVITES;

/**
 * Created by nigel on 31/01/15.
 */
@Layout(R.layout.yard_list_view) public class YardListScreen
    implements Blueprint, HasParent<HomeScreen> {

  @Override public String getMortarScopeName() {

    return getClass().getName();
  }

  @Override public Object getDaggerModule() {

    return new Module();
  }

  @Override public HomeScreen getParent() {

    return new HomeScreen();
  }

  @dagger.Module(injects = {
      YardListView.class, YardAdapter.class
  },
      addsTo = CorePresenter.Module.class,
      library = true,
      complete = false) static class Module {

  }

  static class Presenter extends ViewPresenter<YardListView> {

    @Inject ActionBarOwner actionBarOwner;

    @Inject Flow flow;

    final List<Yard> adapterData = newArrayList();

    private YardAdapter adapter;
    private YardType yardType;

    @Override protected void onLoad(final Bundle savedInstanceState) {

      super.onLoad(savedInstanceState);
      if (getView() == null || yardType == null) {
        return;
      }

      initPresenter();
    }

    private void initPresenter() {
      actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, "Yards", null));

      initView();

      loadData(LOCAL);
    }

    private void loadData(final Load load) {

      final ParseQuery<Yard> query = ParseQuery.getQuery(Yard.class);

      if (load == LOCAL) {
        query.fromLocalDatastore();
      }

      if (yardType == MINE) {
        query.whereEqualTo(AUTHOR, ParseUser.getCurrentUser());
      } else {

        //query.include(INVITES);
        query.whereContainedIn(INVITES, newArrayList(ParseUser.getCurrentUser()));
      }

      query.findInBackground((list, e) -> {

        if (e == null) {

          adapterData.clear();
          adapterData.addAll(list);
          adapter.notifyDataSetChanged();

          ParseObject.pinAllInBackground(list);

          //for (Yard yard : list) {
          //
          //  for (ParseUser user : yard.getInvites()) {
          //
          //    System.out.println("user.getEmail() = " + user.getObjectId());
          //  }
          //}

          if (load == LOCAL) {
            loadData(NETWORK);
          }
        } else {
          e.printStackTrace();
        }
      });
    }

    private void initView() {

      getView().setLayoutManager(new LinearLayoutManager(getView().getContext()));

      getView().setAdapter(adapter = new YardAdapter(adapterData, getView().getContext(), yardType));

      if(yardType == INVITED){

        getView().create.setVisibility(GONE);
      }
    }

    public void handleCreate() {

      flow.goTo(new WerfCreateScreen());
    }

    public void setYardType(YardType yardType) {
      this.yardType = yardType;
      initPresenter();
    }
  }
}