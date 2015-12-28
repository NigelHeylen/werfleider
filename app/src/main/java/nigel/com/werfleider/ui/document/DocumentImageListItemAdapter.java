package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerViewEx;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.util.ImageUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by nigel on 24/01/15.
 */
public class DocumentImageListItemAdapter extends RecyclerViewEx.Adapter {

  private final ProgressBarCircularIndeterminate progressBar;

  @Inject Picasso pablo;

  @Inject DocumentImageAdapterData adapterData;

  public DocumentImageListItemAdapter(final Context context,
      final ProgressBarCircularIndeterminate progressBar) {

    this.progressBar = progressBar;

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

    pablo.load(
        image.hasImage() ? image.getImage().getUrl() : ImageUtils.getOnDiskUrl(image.getImageURL()))
        .fit()
        .centerInside()
        .into(holder.image);

    holder.delete.setVisibility(image.getAuthor() != ParseUser.getCurrentUser() ? GONE : VISIBLE);

    holder.delete.setOnClickListener(v -> {

      progressBar.setVisibility(VISIBLE);
      if (isNullOrEmpty(image.getObjectId())) {

        adapterData.remove(position);
        notifyItemRemoved(position);
        progressBar.setVisibility(GONE);
      } else {
        image.deleteInBackground(new DeleteCallback() {
          @Override public void done(final ParseException e) {

            if (e != null) {
              e.printStackTrace();
            } else {
              adapterData.remove(position);
              notifyItemRemoved(position);
            }

            progressBar.setVisibility(GONE);
          }
        });
      }
    });
  }

  @Override public int getItemCount() {
    return adapterData.size();
  }

  static class ViewHolder extends RecyclerViewEx.ViewHolder {

    @Bind(R.id.document_image_list_container) CardView container;

    @Bind(R.id.document_image_list_image) ImageView image;

    @Bind(R.id.document_image_list_image_delete) ImageView delete;

    @Bind(R.id.document_image_list_image_active) ImageView active;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}