package nigel.com.werfleider.ui.auth;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 28/12/14.
 */
public class OAuthView extends LinearLayout {

    @Inject OAuthScreen.Presenter presenter;

    @InjectView(R.id.auth_webview) WebView webView;

    public OAuthView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        presenter.takeView(this);

    }

    private final static String JAVASCRIPT = "javascript:myJavascriptInterface.getValue(document.getElementById('code').value);";


    public void loadUrl(final String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavascriptInterface(presenter), "myJavascriptInterface");
        webView.setWebViewClient(
                new WebViewClient() {
                    @Override public void onPageFinished(final WebView view, final String url) {
                        super.onPageFinished(view, url);
                        webView.loadUrl(JAVASCRIPT);
                    }


                });

        webView.loadUrl(url);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }


    private class MyJavascriptInterface {

        final OAuthScreen.Presenter presenter;

        private MyJavascriptInterface(final OAuthScreen.Presenter presenter) {
            this.presenter = presenter;
        }

        @JavascriptInterface
        @SuppressWarnings("unused")
        public void getValue(String data) {
            presenter.saveAuthCode(data);
        }
    }
}
