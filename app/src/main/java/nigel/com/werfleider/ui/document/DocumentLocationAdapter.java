package nigel.com.werfleider.ui.document;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentLocatie;
import nigel.com.werfleider.model.Werf;
import nigel.com.werfleider.ui.widget.CustomTextWatcher;

import static java.lang.String.format;

/**
 * Created by nigel on 24/01/15.
 */
public class DocumentLocationAdapter extends RecyclerView.Adapter<DocumentLocationAdapter.ViewHolder> {

    final Document document;
    @Inject Picasso pablo;
    @Inject Flow flow;
    @Inject Werf werf;

    public DocumentLocationAdapter(
            final Document document,
            final Picasso pablo,
            final Flow flow) {
        this.document = document;
        this.pablo = pablo;
        this.flow = flow;
    }

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.plaatsbeschrijf_location_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {

        final DocumentLocatie collection = document.getFotoReeksList().get(position);

        holder.mName.setText(collection.getLocation());
        holder.mName.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        collection.setLocation(s.toString());
                    }
                });

        holder.mImages.setText(format("%d images", collection.getImageList().size()));


        if(collection.hasImages()) {
            pablo
                    .load(collection.getImageList().get(0).getOnDiskUrl())
                    .fit()
                    .centerInside()
                    .into(holder.mImage);
        }

        holder.mContainer.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        flow.goTo(new DocumentLocationDetailScreen(document, position, werf));
                    }
                });


    }

    @Override public int getItemCount() {
        return document.getFotoReeksList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.plaatsbeschrijf_location_item_name) EditText mName;
        @InjectView(R.id.plaatsbeschrijf_location_item_images) TextView mImages;
        @InjectView(R.id.plaatsbeschrijf_location_item_image) ImageView mImage;
        @InjectView(R.id.plaatsbeschrijf_location_item_container) CardView mContainer;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
