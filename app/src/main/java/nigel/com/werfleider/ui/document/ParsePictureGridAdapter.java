package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.squareup.picasso.Picasso;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.DocumentLocation;

/**
 * Created by nigel on 03/12/14.
 */
public class ParsePictureGridAdapter extends RecyclerView.Adapter<ParsePictureGridAdapter.PictureGridViewHolder> {

    @Inject List<DocumentImage> imageList;

    @Inject List<Integer> indices;

    @Inject Picasso pablo;

    public ParsePictureGridAdapter(final Context context, final DocumentLocation documentLocation) {

        Mortar.inject(
                context,
                this);
    }

    private int getIndex(final List<DocumentImage> imageList, final ParseDocumentImage documentImage) {

        return Iterables.indexOf(imageList, new Predicate<DocumentImage>() {
                @Override public boolean apply(final DocumentImage input) {

                    return input.getImageURL().equals(documentImage.getImageURL());
                }
            });
    }


    @Override public PictureGridViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {

        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_grid_item, viewGroup, false);

        return new PictureGridViewHolder(itemView);
    }

    @Override public void onBindViewHolder(final PictureGridViewHolder holder, final int index) {

        pablo
                .load(imageList.get(index).getOnDiskUrl())
                .fit()
                .centerInside()
                .into(holder.mGridImage);

        if(indices.contains(index)){
            holder.checkmark.setVisibility(View.VISIBLE);

        } else {
            holder.checkmark.setVisibility(View.INVISIBLE);

        }

        holder.container.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {
                        if(!indices.contains(index)) {
                            holder.checkmark.setVisibility(View.VISIBLE);
                            indices.add(index);
                        } else {
                            holder.checkmark.setVisibility(View.INVISIBLE);
                            indices.remove((Integer) index);
                        }
                    }
                });

    }

    @Override public int getItemCount() {
        return imageList.size();
    }


    static class PictureGridViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.image_grid_item_container) LinearLayout container;
        @Bind(R.id.image_grid_item_image) ImageView mGridImage;
        @Bind(R.id.image_grid_item_checkmark) ImageView checkmark;

        public PictureGridViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
