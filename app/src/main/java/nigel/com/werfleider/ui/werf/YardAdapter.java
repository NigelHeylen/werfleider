package nigel.com.werfleider.ui.werf;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseYard;

import static java.lang.String.format;

/**
 * Created by nigel on 04/02/15.
 */
public class YardAdapter extends RecyclerView.Adapter<YardAdapter.YardViewHolder> {

    @Inject Flow flow;

    @Inject List<ParseYard> parseYards;

    @Override public YardViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.werf_item,
                parent,
                false);

        return new YardViewHolder(view);
    }

    @Override public void onBindViewHolder(final YardViewHolder holder, final int position) {

        final ParseYard werf = parseYards.get(position);

        holder.naam.setText(werf.getNaam());
        holder.nr.setText(werf.getNummer());

        holder.container.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {

                        flow.goTo(new YardDetailScreen(werf));
                    }
                });

        holder.delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override public void onClick(final View v) {

//                        werfDbHelper.deleteYard(yard);
                        parseYards.remove(werf);
                        notifyItemRemoved(position);
                    }
                });

    }

    private String getMailFormat(final String type, final int number) {

        return format(
                "%s: %d emails",
                type,
                number);
    }

    @Override public int getItemCount() {

        return parseYards.size();
    }

    static class YardViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.werf_item_container) CardView container;

        @InjectView(R.id.werf_item_naam) TextView naam;

        @InjectView(R.id.werf_item_nr) TextView nr;

        @InjectView(R.id.werf_item_delete) ImageView delete;

        public YardViewHolder(final View itemView) {

            super(itemView);

            ButterKnife.inject(
                    this,
                    itemView);
        }
    }
}
