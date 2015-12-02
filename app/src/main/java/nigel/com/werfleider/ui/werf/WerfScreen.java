package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.Mortar;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.ui.home.HomeScreen;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
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
            YardAdapter.class
    },
            addsTo = CorePresenter.Module.class,
            library = true,
            complete = false
    )
    static class Module {

        @Provides @Singleton List<ParseYard> provideParseWerfs() {

            return newArrayList();
        }
    }

    @Singleton
    static class Presenter extends ViewPresenter<WerfView> {

        @Inject ActionBarOwner actionBarOwner;

        @Inject Flow flow;

        @Inject List<ParseYard> adapterData;

        private YardAdapter adapter;

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

            loadData(LOCAL);

        }

        private void loadData(final Load load) {

            final ParseQuery<ParseYard> query = ParseQuery.getQuery(ParseYard.class);

            if (load == LOCAL) {
                query.fromLocalDatastore();
            }

            query
                    .whereEqualTo(
                            AUTHOR,
                            ParseUser.getCurrentUser())
                    .findInBackground(
                            new FindCallback<ParseYard>() {
                                @Override public void done(final List<ParseYard> list, final ParseException e) {

                                    if (e == null) {

                                        for (ParseYard yard : list) {

                                            if (!adapterData.contains(yard)) {
                                                adapterData.add(yard);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();

                                        ParseObject.pinAllInBackground(list);

                                        if (load == LOCAL) {
                                            loadData(NETWORK);
                                        }

                                    } else {
                                        e.printStackTrace();
                                    }
                                    if (getView() != null) {
                                        getView().loader.setVisibility(
                                                View.GONE);
                                    }
                                }
                            });
        }

        private void initView() {

            getView().setLayoutManager(new LinearLayoutManager(getView().getContext()));

            adapter = new YardAdapter();

            Mortar.inject(
                    getView().getContext(),
                    adapter);

            getView().setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }

        public void handleCreate() {

            flow.goTo(new WerfCreateScreen());

        }

        @Override protected void onExitScope() {

            super.onExitScope();
        }
    }


}
