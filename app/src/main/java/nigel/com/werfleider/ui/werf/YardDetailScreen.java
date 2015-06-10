package nigel.com.werfleider.ui.werf;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.ui.document.DocumentOverviewScreen;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewAdapter;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewView;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.yard_detail)
public class YardDetailScreen implements Blueprint, HasParent<WerfScreen> {

    final ParseYard werf;

    public YardDetailScreen(final ParseYard werf) {

        this.werf = werf;
    }

    @Override public String getMortarScopeName() {

        return getClass().getName() + ": " + werf.getObjectId() + ": " + werf.getNaam();
    }

    @Override public Object getDaggerModule() {

        return new Module(werf);
    }

    @Override public WerfScreen getParent() {

        return new WerfScreen();
    }


    @dagger.Module(
            injects = {
                    YardDetailView.class,
                    YardDetailDocumentAdapter.class,
                    ParseDocumentOverviewView.class,
                    ParseDocumentOverviewAdapter.class
            },
            addsTo = CorePresenter.Module.class)
    static class Module {

        private final ParseYard werf;

        public Module(final ParseYard werf) {

            this.werf = werf;
        }

        @Provides @Singleton ParseYard provideWerf() {

            return werf;
        }
    }

    @Singleton
    static class Presenter extends ViewPresenter<YardDetailView> {

        @Inject Flow flow;

        @Inject ParseYard werf;

        public void goToDocumentView(final DocumentType type) {

            flow.goTo(
                    new DocumentOverviewScreen(
                            werf,
                            type));
        }

    }


}
