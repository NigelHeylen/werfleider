package nigel.com.werfleider.ui.werf;

import org.joda.time.DateTime;

import javax.inject.Inject;

import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.werf.WerfDbHelper;
import nigel.com.werfleider.model.Werf;
import rx.Observable;
import rx.Observer;
import rx.functions.Action0;

/**
 * Created by nigel on 07/02/15.
 */
@Layout(R.layout.werf_create_view)
public class WerfCreateScreen implements Blueprint, HasParent<WerfScreen> {

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

    @Override public WerfScreen getParent() {
        return new WerfScreen();
    }

    @dagger.Module(
            injects = WerfCreateView.class,
            addsTo =
                    CorePresenter.Module.class,
            includes = WerfScreen.Module.class

    )
    static class Module {
    }

    static class Presenter extends ViewPresenter<WerfCreateView> {

        @Inject WerfDbHelper db;

        @Inject Flow flow;

        public void create(
                final String naam,
                final String number,
                final String opdrachtgever,
                final String opdrachtgeverAdres,
                final String opdrachtgeverStad,
                final String omschrijving,
                final DateTime datumAanvang) {
            Observable
                    .just(new Werf(naam, number, opdrachtgever, opdrachtgeverAdres, opdrachtgeverStad, omschrijving, datumAanvang))
                    .doOnTerminate(
                            new Action0() {
                                @Override public void call() {
                                    db.closeDB();
                                }
                            })
                    .subscribe(
                            new Observer<Werf>() {
                                @Override public void onCompleted() {
                                    flow.goTo(new WerfScreen());
                                }

                                @Override public void onError(final Throwable e) {
                                    e.printStackTrace();
                                }

                                @Override public void onNext(final Werf werf) {
                                    db.createWerf(werf);
                                }
                            });

        }
    }
}
