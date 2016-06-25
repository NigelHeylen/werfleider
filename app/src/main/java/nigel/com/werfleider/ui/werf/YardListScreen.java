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
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Contact;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.home.HomeScreen;

import static android.view.View.GONE;
import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.ui.werf.YardType.INVITED;
import static nigel.com.werfleider.ui.werf.YardType.MINE;
import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.CREATED_AT;
import static nigel.com.werfleider.util.ParseStringUtils.ID;
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

    @Inject Flow flow;

    @Inject ParseErrorHandler parseErrorHandler;

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
      initView();

      loadData(LOCAL);
    }

    private void loadData(final Load load) {

      final ParseQuery<Yard> query = ParseQuery.getQuery(Yard.class);

      if (load == LOCAL) {
        query.fromLocalDatastore();
      }

      if (yardType == MINE) {
        if(load == LOCAL) {
          query.whereContainedIn(AUTHOR, newArrayList(null, ParseUser.getCurrentUser()));
        } else {
          query.whereEqualTo(AUTHOR, ParseUser.getCurrentUser());
        }
      } else {

        //query.include(INVITES);
        final ParseQuery<Contact> contactParseQuery = ParseQuery.getQuery(Contact.class);
        contactParseQuery.whereMatches(ID, ParseUser.getCurrentUser().getObjectId());
        query.whereMatchesQuery(INVITES, contactParseQuery);
      }

      query.orderByAscending(CREATED_AT).findInBackground((list, e) -> {

        if (e == null) {

          for (Yard yard : list) {

            if (!adapterData.contains(yard)) {
              adapterData.add(yard);
            }

          }

          for (Yard yard : list) {
            yard.saveEventually(e1 -> {
              if(e1 != null) parseErrorHandler.handleParseError(e1);
            });
          }
          adapter.notifyDataSetChanged();
          ParseObject.pinAllInBackground(list);

          if (load == LOCAL) {
            loadData(NETWORK);
          }
        } else {
          parseErrorHandler.handleParseError(e);
        }

        if (getView() != null) {

          if(adapterData.isEmpty()){

            getView().showEmptyView();
          } else {

            getView().showContentView();
          }
        }
      });
    }

    private void initView() {

      getView().setLayoutManager(new LinearLayoutManager(getView().getContext()));

      getView().setAdapter(
          adapter = new YardAdapter(adapterData, getView().getContext(), yardType, getView()));

      if (yardType == INVITED) {

        getView().create.setVisibility(GONE);
      }
    }

    public void handleCreate() {

      final Yard yard = new Yard();
      yard.saveEventually(e -> {

        if(e == null){
          yard.pinInBackground();
          yard.setAuthor(ParseUser.getCurrentUser());
          flow.goTo(new YardDetailScreen(yard, yardType));

        } else {
         parseErrorHandler.handleParseError(e);
        }
      });
    }

    public void setYardType(YardType yardType) {
      this.yardType = yardType;
      initPresenter();
    }
  }
}
