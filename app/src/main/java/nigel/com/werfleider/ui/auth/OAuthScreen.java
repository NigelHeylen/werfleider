package nigel.com.werfleider.ui.auth;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.IOException;

import javax.inject.Inject;

import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.werfoverzicht.WerfDetailScreen;
import rx.Observable;
import rx.functions.Action1;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by nigel on 28/12/14.
 */
@Layout(R.layout.auth_dialog)
public class OAuthScreen implements Blueprint, HasParent<WerfDetailScreen> {

    private final Werf werf;

    public OAuthScreen(final Werf werf) {
        this.werf = werf;
    }

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

    @Override public WerfDetailScreen getParent() {
        return new WerfDetailScreen(werf);
    }


    @dagger.Module(injects = OAuthView.class, addsTo = CorePresenter.Module.class)
    static class Module {

    }


    static class Presenter extends ViewPresenter<OAuthView> {

        final Flow flow;

        private GmailApiUtil gmailApiUtil;

        final SharedPreferences preferences;

        final String ACCOUNT_TYPE_GOOGLE = "com.google";
        final String[] FEATURES_MAIL = {
                "service_mail"
        };

        static final String TAG = "TestApp";

        @Inject Presenter(
                final Flow flow,
                final SharedPreferences preferences) {
            this.flow = flow;

            this.preferences = preferences;
        }

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() == null) {
                return;
            }

            try {
                gmailApiUtil = new GmailApiUtil(getView().getContext());
//                getView().loadUrl(gmailApiUtil.getAuthorizationUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        public void saveAuthCode(final String code) {


            Observable.just(code)
                      .observeOn(mainThread())
                      .subscribe(
                              new Action1<String>() {
                                  @Override public void call(final String s) {
                                      preferences.edit().putString("GMAIL_CODE", code).apply();
                                      System.out.println("code = " + code);
//                                      flow.goTo(new EmailOverzichtScreen(new Werf()));

                                          }
                              });
        }
    }
}
