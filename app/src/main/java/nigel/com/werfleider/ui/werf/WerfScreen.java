package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.werf.WerfDbHelper;
import nigel.com.werfleider.dao.werf.WerfDbHelperBean;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.model.WerfInt;
import nigel.com.werfleider.ui.home.HomeScreen;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;

/**
 * Created by nigel on 31/01/15.
 */
@Layout(R.layout.werf_view)
public class WerfScreen implements Blueprint, HasParent<HomeScreen> {

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
            WerfView.class,
            WerfAdapter.class
    },
            addsTo = CorePresenter.Module.class,
            library = true,
            complete = false
    )
    static class Module {

        @Provides WerfDbHelper provideWerfHelper(final WerfDbHelperBean werfHelper) {

            return werfHelper;
        }

        @Provides @Singleton List<WerfInt> provideParseWerfs() {

            return newArrayList();
        }
    }

    @Singleton
    static class Presenter extends ViewPresenter<WerfView> {

        @Inject WerfDbHelper db;

        @Inject ActionBarOwner actionBarOwner;

        @Inject Flow flow;

        @Inject List<WerfInt> parseWerfs;

        private WerfAdapter adapter;

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

            actionBarOwner.setConfig(
                    new ActionBarOwner.Config(
                            false,
                            true,
                            "WerfScreen",
                            null));

            initView();

            loadData();

        }

        private void loadData() {

            Observable.just(db.getAllWerfs())
                      .doOnTerminate(
                              new Action0() {
                                  @Override public void call() {

                                      db.closeDB();
                                  }
                              })
                      .subscribe(
                              new Action1<List<Werf>>() {
                                  @Override public void call(final List<Werf> werfs) {

//                                      parseWerfs.addAll(werfs);
                                      adapter.notifyDataSetChanged();
                                  }
                              });

            ParseQuery<ParseYard> query = ParseQuery.getQuery(ParseYard.class);
            query.whereEqualTo(
                    AUTHOR,
                    ParseUser.getCurrentUser());
            query.findInBackground(
                    new FindCallback<ParseYard>() {
                        @Override public void done(final List<ParseYard> list, final ParseException e) {

                            if (e == null) {
                                parseWerfs.addAll(list);
                                adapter.notifyDataSetChanged();
                            } else{
                                e.printStackTrace();
                            }
                            if(getView()!= null) {
                                getView().loader.setVisibility(
                                        View.GONE);
                            }
                        }
                    });
        }

        private void initView() {

            getView().setLayoutManager(new LinearLayoutManager(getView().getContext()));

            adapter = new WerfAdapter();

            Mortar.inject(
                    getView().getContext(),
                    adapter);

            getView().setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }

        public void handleCreate() {

            flow.goTo(new WerfCreateScreen());

        }
    }
}
