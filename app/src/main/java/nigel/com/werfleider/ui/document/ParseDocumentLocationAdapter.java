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
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.Yard;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by nigel on 24/01/15.
 */
public class ParseDocumentLocationAdapter
    extends RecyclerView.Adapter<ParseDocumentLocationAdapter.ViewHolder> {

  @Inject ParseDocument document;

  @Inject Picasso pablo;

  @Inject Flow flow;

  @Inject Yard yard;

  @Inject List<ParseDocumentLocation> locations;

  public ParseDocumentLocationAdapter(final Context context) {

    Mortar.inject(context, this);
  }

  @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.document_location_item, parent, false);

    return new ViewHolder(itemView);
  }

  @Override public void onBindViewHolder(final ViewHolder holder, final int position) {

    final ParseDocumentLocation location = locations.get(position);

    holder.title.setText(location.getTitle());

    holder.artNr.setText(location.getArtNr());

    holder.mContainer.setOnClickListener(
        v -> flow.goTo(new LocationDetailScreen(document, yard, location)));

    holder.delete.setVisibility(
        location.getAuthor() != ParseUser.getCurrentUser() ? GONE : VISIBLE);

    holder.delete.setOnClickListener(v -> {
      location.deleteEventually(e -> {

        if (e == null) {
          System.out.println("ParseDocumentLocationAdapter.done");
        } else {
          e.printStackTrace();
        }
      });

      locations.remove(position);
      notifyItemRemoved(position);
    });
  }

  @Override public int getItemCount() {

    return locations.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.document_location_item_name) TextView title;

    @Bind(R.id.document_location_item_art_nr) TextView artNr;

    @Bind(R.id.document_location_item_container) RelativeLayout mContainer;

    @Bind(R.id.document_location_item_delete) ImageView delete;

    public ViewHolder(final View itemView) {

      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
