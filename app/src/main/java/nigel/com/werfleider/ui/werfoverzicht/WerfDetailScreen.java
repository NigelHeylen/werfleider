package nigel.com.werfleider.ui.werfoverzicht;

import javax.inject.Inject;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.document.DocumentOverviewScreen;
import nigel.com.werfleider.ui.werf.WerfScreen;

/**
 * Created by nigel on 27/12/14.
 */
@Layout(R.layout.werf_detail)
public class WerfDetailScreen implements Blueprint, HasParent<WerfScreen> {

    final Werf werf;

    public WerfDetailScreen(final Werf werf) {
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        //TODO wanneer er meer dan 1 werf is dan moet het id ook bij de naam
        return getClass().getName() + ": " + werf.getId();
    }

    @Override public Object getDaggerModule() {
        return new Module(werf);
    }

    @Override public WerfScreen getParent() {
        return new WerfScreen();
    }


    @dagger.Module(
            injects = WerfDetailView.class,
            addsTo = CorePresenter.Module.class)
    static class Module {

        private final Werf werf;

        public Module(final Werf werf) {

            this.werf = werf;
        }

        @Provides Werf provideWerf(){
            return werf;
        }
    }


    static class Presenter extends ViewPresenter<WerfDetailView> {

        @Inject Flow flow;

        @Inject Werf werf;

        public void goToDocumentView(final DocumentType type) {
            flow.goTo(new DocumentOverviewScreen(werf, type));
        }

    }


}
