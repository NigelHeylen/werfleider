package nigel.com.werfleider.ui.document;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.ui.widget.CustomTextWatcher;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.valueOf;

/**
 * Created by nigel on 24/01/15.
 */
public class DocumentImageListItemAdapter extends RecyclerView.Adapter {

    final Picasso pablo;
    private final EditText description;
    private final MaterialEditText title;

    private final MaterialEditText length;
    private final MaterialEditText width;
    private final MaterialEditText height;

    private int activePosition;

    final DocumentLocation collection;

    public DocumentImageListItemAdapter(
            final DocumentLocation collection,
            final Picasso pablo,
            final MaterialEditText title,
            final EditText description,
            final MaterialEditText length,
            final MaterialEditText width,
            final MaterialEditText height) {
        this.collection = collection;
        this.pablo = pablo;
        this.title = title;
        this.description = description;
        this.length = length;
        this.width = width;
        this.height = height;

        activePosition = 0;

        if (collection.hasImages()) {
            description.setText(getActiveDocumentImage().getDescription());

            title.setText(getActiveDocumentImage().getTitle());

            length.setText(valueOf(getActiveDocumentImage().getLength()));
            width.setText(valueOf(getActiveDocumentImage().getWidth()));
            height.setText(valueOf(getActiveDocumentImage().getHeight()));
        }

        description.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        getActiveDocumentImage().setDescription(s.toString());
                    }
                });

        title.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        getActiveDocumentImage().setTitle(s.toString());
                    }
                });

        length.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        getActiveDocumentImage().setLength(isNullOrEmpty(s.toString()) ? 0 : Double.valueOf(s.toString()));
                    }
                });

        width.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        getActiveDocumentImage().setWidth(isNullOrEmpty(s.toString()) ? 0 : Double.valueOf(s.toString()));
                    }
                });

        height.addTextChangedListener(
                new CustomTextWatcher() {
                    @Override public void afterTextChanged(final Editable s) {
                        getActiveDocumentImage().setHeight(isNullOrEmpty(s.toString()) ? 0 : Double.valueOf(s.toString()));
                    }
                });
    }

    private DocumentImage getActiveDocumentImage() {
        return collection.getImageList().get(activePosition);
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final View view = inflater.inflate(R.layout.document_image_list_item, parent, false);

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

                        title.setText(image.getTitle());

                        description.setText(image.getDescription());

                        length.setText(valueOf(image.getLength()));
                        width.setText(valueOf(image.getWidth()));
                        height.setText(valueOf(image.getHeight()));

                        notifyDataSetChanged();
                    }
                });

    }

    @Override public int getItemCount() {
        return collection.getImageList().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.document_image_list_container) CardView container;

        @InjectView(R.id.document_image_list_image) ImageView image;

        @InjectView(R.id.document_image_list_image_delete) ImageView delete;

        @InjectView(R.id.document_image_list_image_active) ImageView active;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
