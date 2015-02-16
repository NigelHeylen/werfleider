package nigel.com.werfleider.ui.emaildetail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.MessageDetails;

/**
 * Created by nigel on 20/01/15.
 */
public class EmailDetailView extends ScrollView {
    @Inject EmailDetailScreen.Presenter presenter;

    private ViewHolder holder;

    public EmailDetailView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        holder = new ViewHolder(this);
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void initialize(final MessageDetails email) {

//        holder.mFrom.setText(email.getSender().isPresent() ? email.getSenderShortString() : "None");
        holder.mTitle.setText(email.getSubject());
        holder.mDate.setText(email.getSentDate().isPresent() ? email.getSentDate().get().toString("dd MMM") : "None");

        holder.mContent.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        holder.mContent.loadData(email.getText(), "text/html", "UTF-8");
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'email_detail_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {

        @InjectView(R.id.email_detail_from) TextView mFrom;
        @InjectView(R.id.email_detail_date) TextView mDate;
        @InjectView(R.id.email_detail_title) TextView mTitle;
        @InjectView(R.id.email_detail_container) RelativeLayout mContainer;
        @InjectView(R.id.email_detail_content) WebView mContent;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
