package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.dao.document.DocumentDbHelper;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.Werf;

/**
 * Created by nigel on 14/03/15.
 */
public class DocumentOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Document> documents;
    private final Context context;

    @Inject Flow flow;

    @Inject Picasso pablo;

    @Inject Werf werf;

    @Inject DocumentDbHelper documentDbHelper;

    public DocumentOverviewAdapter(final List<Document> documents, final Context context) {
        this.documents = documents;
        this.context = context;
        Mortar.inject(context, this);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_overview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        final Document document = documents.get(position);

        if (!document.getFotoReeksList().isEmpty()) {
            pablo
                    .load(document.getFotoReeksList().get(0).getImageList().get(0).getOnDiskUrl())
                    .fit()
                    .centerInside()
                    .into(viewHolder.image);
        }

        viewHolder.date.setText(document.getCreatedAt().toString("dd-MM-yyyy"));

        viewHolder.container.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        flow.goTo(new DocumentScreen(werf, document));
                    }
                });

        viewHolder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                         if(documentDbHelper.deleteDocument(document.getId()) == 1){
                             documents.remove(position);
                             notifyItemRemoved(position);

                             Toast.makeText(context, "Document deleted", Toast.LENGTH_LONG).show();
                         } else {
                             Toast.makeText(context, "Something went wrong. Document not deleted.", Toast.LENGTH_LONG).show();
                         }
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
