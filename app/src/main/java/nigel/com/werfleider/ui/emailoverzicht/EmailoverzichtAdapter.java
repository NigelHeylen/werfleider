package nigel.com.werfleider.ui.emailoverzicht;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.MessageDetails;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.emaildetail.EmailDetailScreen;

/**
 * Created by nigel on 28/12/14.
 */
public class EmailoverzichtAdapter extends RecyclerView.Adapter<EmailoverzichtAdapter.ViewHolder> {

    private final List<MessageDetails> messages;

    @Inject Flow flow;
    @Inject Werf werf;

    public EmailoverzichtAdapter(final List<MessageDetails> messages) {
        this.messages = messages;
    }

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.emailoverzicht_single, parent, false);
        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final EmailoverzichtAdapter.ViewHolder holder, final int position) {
        final MessageDetails email = messages.get(position);

//        holder.mFrom.setText(email.getSender().isPresent() ? email.getSenderShortString() : "None");
        holder.mTitle.setText(email.getSubject());
        holder.mSnippet.setText(email.getSnippet());
        holder.mDate.setText(email.getSentDate().isPresent() ? email.getSentDate().get().toString("dd MMM") : "None");

        holder.container.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        flow.goTo(new EmailDetailScreen(email, werf));
                    }
                });
    }

    @Override public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.emailoverzicht_single_container) RelativeLayout container;
        @InjectView(R.id.emailoverzicht_single_from) TextView mFrom;
        @InjectView(R.id.emailoverzicht_single_title) TextView mTitle;
        @InjectView(R.id.emailoverzicht_single_snippet) TextView mSnippet;
        @InjectView(R.id.emailoverzicht_single_date) TextView mDate;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
