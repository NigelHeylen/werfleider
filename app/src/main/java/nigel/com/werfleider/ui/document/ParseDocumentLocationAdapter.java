package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.mrengineer13.snackbar.SnackBar;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.location.LocationEditScreen;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by nigel on 24/01/15.
 */
public class ParseDocumentLocationAdapter
    extends RecyclerView.Adapter<ParseDocumentLocationAdapter.ViewHolder> {

  private final Context context;
  private final View parent;

  @Inject Document document;

  @Inject Picasso pablo;

  @Inject Flow flow;

  @Inject Yard yard;

  @Inject List<DocumentLocation> locations;

  public ParseDocumentLocationAdapter(final Context context, View parent) {
    this.context = context;
    this.parent = parent;

    Mortar.inject(context, this);
  }

  @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.document_location_item, parent, false);

    return new ViewHolder(itemView);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {

    final DocumentLocation location = locations.get(position);

    holder.title.setText(location.getTitle());

    holder.artNr.setText(location.getArtNr());

    holder.mContainer.setOnClickListener(
        v -> flow.goTo(new LocationDetailScreen(document, yard, location)));

    holder.delete.setVisibility(
        location.getAuthor() != ParseUser.getCurrentUser() ? GONE : VISIBLE);

    holder.edit.setVisibility(location.getAuthor() != ParseUser.getCurrentUser() ? GONE : VISIBLE);

    holder.edit.setOnClickListener(
        v -> flow.goTo(new LocationEditScreen(location, document, yard)));

    holder.delete.setOnClickListener(v -> new SnackBar.Builder(context, parent).withMessage(
        "Are you sure you want to delete this location?")
        .withActionMessage("Delete")
        .withTextColorId(R.color.green)
        .withOnClickListener(token -> {
          location.unpinInBackground();
          location.deleteEventually();

          locations.remove(position);
          notifyItemRemoved(position);
          notifyDataSetChanged();
        })
        .show());
  }

  @Override public int getItemCount() {

    return locations.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.document_location_item_name) TextView title;

    @Bind(R.id.document_location_item_art_nr) TextView artNr;

    @Bind(R.id.document_location_item_container) RelativeLayout mContainer;

    @Bind(R.id.document_location_item_delete) ImageView delete;
    @Bind(R.id.document_location_item_edit) ImageView edit;

    public ViewHolder(final View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
