package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerViewEx;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.util.ImageUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by nigel on 24/01/15.
 */
public class DocumentImageListItemAdapter extends RecyclerViewEx.Adapter {

  @Inject Picasso pablo;

  @Inject DocumentImageAdapterData adapterData;

  public DocumentImageListItemAdapter(final Context context) {

    Mortar.inject(context, this);
  }

  @Override
  public RecyclerViewEx.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    final View view = inflater.inflate(R.layout.document_image_list_item, parent, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final RecyclerViewEx.ViewHolder viewHolder, final int position) {

    final ViewHolder holder = (ViewHolder) viewHolder;

    final ParseDocumentImage image = adapterData.get(position);

    if(image.hasImage() && !image.hasImageBytes()) {
      pablo.load(image.hasImage() ? image.getImage().getUrl() : ImageUtils.getOnDiskUrl(image.getImageURL())).fit().centerInside().into(
          holder.image);
    } else {

      if(image.hasImageBytes()){

        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(image.getImageBytes(), 0, image.getImageBytes().length));
      }
    }

    holder.delete.setVisibility(image.getAuthor() != ParseUser.getCurrentUser() ? GONE : VISIBLE);

    holder.delete.setOnClickListener(v -> {

      image.deleteEventually();
      image.unpinInBackground();
      adapterData.remove(image);

      notifyItemRemoved(position);
      notifyDataSetChanged();
    });
  }

  @Override public int getItemCount() {
    return adapterData.size();
  }

  static class ViewHolder extends RecyclerViewEx.ViewHolder {

    @Bind(R.id.document_image_list_image) ImageView image;

    @Bind(R.id.document_image_list_image_delete) ImageView delete;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
