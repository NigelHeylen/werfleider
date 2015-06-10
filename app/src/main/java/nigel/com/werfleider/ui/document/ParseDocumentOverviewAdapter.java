package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseYard;

/**
 * Created by nigel on 14/03/15.
 */
public class ParseDocumentOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;

    private final List<ParseDocument> documents;

    @Inject Flow flow;

    @Inject Picasso pablo;

    @Inject ParseYard yard;

    public ParseDocumentOverviewAdapter(final Context context, final List<ParseDocument> parseDocuments) {

        this.context = context;
        this.documents = parseDocuments;
        Mortar.inject(
                context,
                this);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.document_overview_item,
                parent,
                false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;

        final ParseDocument document = documents.get(position);

        viewHolder.date.setText(new DateTime(document.getCreatedAt()).toString("dd-MM-yyyy"));

        viewHolder.container.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        flow.goTo(new ParseDocumentScreen(yard, document));
                    }
                });

        viewHolder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        document.deleteEventually();
                        documents.remove(position);
                        notifyItemRemoved(position);
                    }
                });
    }

    @Override public int getItemCount() {
        return documents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.document_overview_item_container) CardView container;

        @InjectView(R.id.document_overview_item_image) ImageView image;
        @InjectView(R.id.document_overview_item_delete) ImageView delete;

        @InjectView(R.id.document_overview_item_date) TextView date;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
