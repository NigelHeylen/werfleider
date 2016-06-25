package nigel.com.werfleider.ui.werf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.List;
import nigel.com.werfleider.R;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 25/06/16.
 */
public class FloorLocationAdapter extends RecyclerView.Adapter<FloorLocationAdapter.ViewHolder> {

  private final FloorLocationDeleteListener onClickListener;
  private List<String> data;

  public FloorLocationAdapter(final FloorLocationDeleteListener onClickListener) {

    this.onClickListener = onClickListener;
    data = newArrayList();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    final View inflate = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.add_sub_location_entry, parent, false);

    return new ViewHolder(inflate);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {

    holder.index.setText(Integer.toString(position));
    holder.text.setText(data.get(position));
    holder.delete.setOnClickListener(v -> {

      onClickListener.setIndex(position);
      onClickListener.onClick(v);
    });
  }

  @Override public int getItemCount() {
    return data.size();
  }

  public void setData(List<String> data) {
    this.data = data;
    notifyDataSetChanged();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.sub_location_index) TextView index;
    @Bind(R.id.sub_location_text) TextView text;
    @Bind(R.id.sub_location_delete) ImageButton delete;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
