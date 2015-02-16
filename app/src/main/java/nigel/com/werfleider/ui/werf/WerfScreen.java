package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import javax.inject.Inject;

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
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.service.werf.GetWerf;
import nigel.com.werfleider.service.werf.GetWerfMock;
import nigel.com.werfleider.ui.home.HomeScreen;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

import static com.google.common.collect.Lists.newArrayList;

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

        @Provides GetWerf provideGetWerf(final GetWerfMock getWerfMock) {
            return getWerfMock;
        }

        @Provides WerfDbHelper provideWerfHelper(final WerfDbHelperBean werfHelper){
            return werfHelper;
        }
    }

    static class Presenter extends ViewPresenter<WerfView> {

        @Inject WerfDbHelper db;

        @Inject GetWerf getWerf;

        @Inject ActionBarOwner actionBarOwner;

        @Inject Flow flow;

        private WerfAdapter adapter;

        private List<Werf> data;

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

            ActionBarOwner.MenuAction menu =
                    new ActionBarOwner.MenuAction(
                            "Create", new Action0() {
                        @Override public void call() {
                            flow.goTo(new WerfCreateScreen());
                        }
                    });
            actionBarOwner.setConfig(new ActionBarOwner.Config(false, true, "WerfScreen", menu));

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
                                      data.addAll(werfs);
                                      adapter.notifyDataSetChanged();
                                  }
                              });
        }

        private void initView() {
            getView().setLayoutManager(new LinearLayoutManager(getView().getContext()));

            adapter = new WerfAdapter(data = newArrayList());

            Mortar.inject(getView().getContext(), adapter);

            getView().setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
    }
}
