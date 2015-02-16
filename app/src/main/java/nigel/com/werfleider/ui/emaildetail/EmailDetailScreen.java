package nigel.com.werfleider.ui.emaildetail;

import javax.inject.Inject;

import dagger.Provides;
import flow.Layout;
import mortar.Blueprint;
import mortar.MortarScope;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.MessageDetails;
import nigel.com.werfleider.model.Werf;

/**
 * Created by nigel on 20/01/15.
 */
@Layout(R.layout.email_detail_view)
public class EmailDetailScreen implements Blueprint {

    private final MessageDetails email;
    private final Werf werf;

    public EmailDetailScreen(final MessageDetails email, final Werf werf) {

        this.email = email;
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName() + " email id: " + email.getId();
    }

    @Override public Object getDaggerModule() {
        return new Module(email);
    }

    @dagger.Module(injects = EmailDetailView.class, addsTo = CorePresenter.Module.class)
    static class Module {

        final MessageDetails email;

        private Module(final MessageDetails email) {
            this.email = email;
        }

        @Provides MessageDetails provideEmail() {
            return email;
        }
    }

    static class Presenter extends ViewPresenter<EmailDetailView> {

        @Inject MessageDetails email;

        @Override protected void onEnterScope(final MortarScope scope) {
            if (getView() != null) {
                getView().initialize(email);
            }
        }
    }
}
