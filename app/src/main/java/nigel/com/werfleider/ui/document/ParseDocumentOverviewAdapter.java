package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.Yard;
import org.joda.time.DateTime;

import static android.view.View.VISIBLE;

/**
 * Created by nigel on 14/03/15.
 */
public class ParseDocumentOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final List<ParseDocument> documents;

  @Inject Flow flow;

  @Inject Yard yard;

  public ParseDocumentOverviewAdapter(final Context context,
      final List<ParseDocument> parseDocuments) {

    this.documents = parseDocuments;
    Mortar.inject(context, this);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.document_overview_item, parent, false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

    final ViewHolder viewHolder = (ViewHolder) holder;

    final ParseDocument document = documents.get(position);

    viewHolder.date.setText(new DateTime(document.getCreatedAt()).toString("dd-MM-yyyy"));

    viewHolder.container.setOnClickListener(
        v -> flow.goTo(new ParseDocumentScreen(yard, document)));

    viewHolder.delete.setVisibility(
        yard.getAuthor() == ParseUser.getCurrentUser() ? VISIBLE : View.GONE);

    viewHolder.delete.setOnClickListener(v -> {
      document.deleteEventually();
      documents.remove(position);
      notifyItemRemoved(position);
    });
  }

  @Override public int getItemCount() {
    return documents.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.document_overview_item_container) CardView container;

    @Bind(R.id.document_overview_item_image) ImageView image;
    @Bind(R.id.document_overview_item_delete) ImageView delete;

    @Bind(R.id.document_overview_item_date) TextView date;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
