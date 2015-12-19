package nigel.com.werfleider.ui.werf;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.model.WerfInt;

import static java.lang.String.format;

/**
 * Created by nigel on 04/02/15.
 */
public class WerfAdapter extends RecyclerView.Adapter<WerfAdapter.WerfViewHolder> {

  @Inject Flow flow;

  @Inject List<WerfInt> parseWerfs;

  @Override public WerfViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.werf_item, parent, false);

    return new WerfViewHolder(view);
  }

  @Override public void onBindViewHolder(final WerfViewHolder holder, final int position) {

    final WerfInt werf = parseWerfs.get(position);

    holder.naam.setText((werf instanceof Yard ? "Parse " : "") + werf.getNaam());
    holder.nr.setText(werf.getNummer());

    holder.container.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(final View v) {

            flow.goTo(new YardDetailScreen((Yard) werf));
          }
        });

    holder.delete.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(final View v) {

            //                        werfDbHelper.deleteWerf(werf);
            parseWerfs.remove(werf);
            notifyItemRemoved(position);
          }
        });
  }

  private String getMailFormat(final String type, final int number) {

    return format("%s: %d emails", type, number);
  }

  @Override public int getItemCount() {

    return parseWerfs.size();
  }

  static class WerfViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.werf_item_container) CardView container;

    @Bind(R.id.werf_item_naam) TextView naam;

    @Bind(R.id.werf_item_nr) TextView nr;

    @Bind(R.id.werf_item_delete) ImageView delete;

    public WerfViewHolder(final View itemView) {

      super(itemView);

      ButterKnife.bind(this, itemView);
    }
  }
}
