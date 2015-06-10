package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.ParseYard;

import static java.lang.String.format;

/**
 * Created by nigel on 24/01/15.
 */
public class ParseDocumentLocationAdapter extends RecyclerView.Adapter<ParseDocumentLocationAdapter.ViewHolder> {

    @Inject ParseDocument document;

    @Inject Picasso pablo;

    @Inject Flow flow;

    @Inject ParseYard yard;

    @Inject List<ParseDocumentLocation> locations;

    public ParseDocumentLocationAdapter(final Context context) {

        Mortar.inject(
                context,
                this);
    }

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.document_location_item,
                parent,
                false);

        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ParseDocumentLocation location = locations.get(position);


        holder.mName.setText(location.getTitle());

        holder.mImages.setText(
                format(
                        "%d images",
                        0));

        holder.mContainer.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {

                        flow.goTo(
                                new ParseDocumentLocationDetailScreen(
                                        document,
                                        yard,
                                        location));
                    }
                });

        holder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        location.deleteEventually(
                                new DeleteCallback() {
                                    @Override public void done(final ParseException e) {

                                        if(e == null) {
                                            System.out.println("ParseDocumentLocationAdapter.done");
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        locations.remove(position);
                        notifyItemRemoved(position);
                    }
                });

    }

    @Override public int getItemCount() {

        return locations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.document_location_item_name)      TextView  mName;

        @InjectView(R.id.document_location_item_images)    TextView  mImages;

        @InjectView(R.id.document_location_item_image)     ImageView mImage;

        @InjectView(R.id.document_location_item_container) CardView  mContainer;

        @InjectView(R.id.document_location_item_delete) ImageView delete;

        public ViewHolder(final View itemView) {

            super(itemView);
            ButterKnife.inject(
                    this,
                    itemView);
        }
    }
}
