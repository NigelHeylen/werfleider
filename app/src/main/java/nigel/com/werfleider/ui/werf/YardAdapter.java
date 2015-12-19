package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Yard;

import static java.lang.String.format;

/**
 * Created by nigel on 04/02/15.
 */
public class YardAdapter extends RecyclerView.Adapter<YardAdapter.YardViewHolder> {

  @Inject Flow flow;

  final List<Yard> yards;
  private final YardType yardType;

  public YardAdapter(List<Yard> yards, final Context context, YardType yardType) {
    this.yards = yards;
    this.yardType = yardType;
    Mortar.inject(context, this);
  }

  @Override public YardViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.werf_item, parent, false);

    return new YardViewHolder(view);
  }

  @Override public void onBindViewHolder(final YardViewHolder holder, final int position) {

    final Yard werf = yards.get(position);

    holder.naam.setText(werf.getNaam());
    holder.nr.setText(werf.getNummer());

    holder.container.setOnClickListener(v -> flow.goTo(new YardDetailScreen(werf)));

    holder.delete.setVisibility(yardType == YardType.INVITED ? View.GONE : View.VISIBLE);
    holder.delete.setOnClickListener(v -> {

      yards.remove(werf);
      notifyItemRemoved(position);
    });
  }

  private String getMailFormat(final String type, final int number) {

    return format("%s: %d emails", type, number);
  }

  @Override public int getItemCount() {

    return yards.size();
  }

  static class YardViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.werf_item_container) CardView container;

    @Bind(R.id.werf_item_naam) TextView naam;

    @Bind(R.id.werf_item_nr) TextView nr;

    @Bind(R.id.werf_item_delete) ImageView delete;

    public YardViewHolder(final View itemView) {

      super(itemView);

      ButterKnife.bind(this, itemView);
    }
  }
}
