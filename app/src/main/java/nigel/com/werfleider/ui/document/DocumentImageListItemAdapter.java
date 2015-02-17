package nigel.com.werfleider.ui.document;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocatie;
import nigel.com.werfleider.ui.widget.CustomTextWatcher;

/**
 * Created by nigel on 24/01/15.
 */
public class DocumentImageListItemAdapter extends RecyclerView.Adapter {

    final Picasso pablo;
    final EditText description;
    private int activePosition;

    final DocumentLocatie collection;

    public DocumentImageListItemAdapter(
            final DocumentLocatie collection,
            final Picasso pablo,
            final EditText description) {
        this.collection = collection;
        this.pablo = pablo;
        this.description = description;

        activePosition = 0;

        if (collection.hasImages()) {
            description.setText(collection.getImageList().get(activePosition).getDescription());
        }

        description.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        collection.getImageList().get(activePosition).setDescription(s.toString());
                    }
                });
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final View view = inflater.inflate(R.layout.plaatsbeschrijf_image_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;

        final DocumentImage image = collection.getImageList().get(position);

        holder.active.setVisibility(activePosition == position ? View.VISIBLE : View.INVISIBLE);

        pablo
                .load(image.getOnDiskUrl())
                .fit()
                .centerInside()
                .into(holder.image);

        holder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        collection.getImageList().remove(position);
                        notifyDataSetChanged();
                    }
                });

        holder.container.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        activePosition = position;
                        description.setText(image.getDescription());
                        notifyDataSetChanged();
                    }
                });

    }

    @Override public int getItemCount() {
        return collection.getImageList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.plaatsbeschrijf_image_list_container) CardView container;

        @InjectView(R.id.plaatsbeschrijf_image_list_image) ImageView image;

        @InjectView(R.id.plaatsbeschrijf_image_list_image_delete) ImageView delete;

        @InjectView(R.id.plaatsbeschrijf_image_list_image_active) ImageView active;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
