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
import com.github.mrengineer13.snackbar.SnackBar;
import com.parse.ParseUser;
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

  @Inject Context context;
  private final List<ParseDocument> documents;
  private final ParseDocumentOverviewView parent;

  @Inject Flow flow;

  @Inject Yard yard;

  public ParseDocumentOverviewAdapter(final Context context,
      final List<ParseDocument> parseDocuments, ParseDocumentOverviewView parent) {

    this.documents = parseDocuments;
    this.parent = parent;
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
    viewHolder.name.setText(document.getName());

    viewHolder.container.setOnClickListener(
        v -> flow.goTo(new ParseDocumentScreen(yard, document)));

    viewHolder.delete.setVisibility(
        yard.getAuthor() == ParseUser.getCurrentUser() ? VISIBLE : View.GONE);
    viewHolder.edit.setVisibility(
        yard.getAuthor() == ParseUser.getCurrentUser() ? VISIBLE : View.GONE);

    viewHolder.edit.setOnClickListener(
        v -> flow.goTo(new DocumentCreateScreen(yard, document.getDocumentType(), document)));

    viewHolder.delete.setOnClickListener(v -> new SnackBar.Builder(context, parent).withMessage(
        "Are you sure you want to delete this document?")
        .withActionMessage("Delete")
        .withTextColorId(R.color.green)
        .withOnClickListener(token -> document.deleteEventually(e -> {
          if (e == null) {
            documents.remove(position);
            notifyItemRemoved(position);
          }
        }))
        .show());
  }

  @Override public int getItemCount() {
    return documents.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.document_overview_item_container) CardView container;

    @Bind(R.id.document_overview_item_delete) ImageView delete;
    @Bind(R.id.document_overview_item_edit) ImageView edit;

    @Bind(R.id.document_overview_item_date) TextView date;
    @Bind(R.id.document_overview_item_name) TextView name;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
