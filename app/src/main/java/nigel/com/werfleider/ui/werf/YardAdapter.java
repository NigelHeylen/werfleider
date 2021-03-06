package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.mrengineer13.snackbar.SnackBar;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import flow.Flow;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.model.Yard;

/**
 * Created by nigel on 04/02/15.
 */
public class YardAdapter extends RecyclerView.Adapter<YardAdapter.YardViewHolder> {

  @Inject Flow flow;

  @Inject Context context;

  @Inject ParseErrorHandler parseErrorHandler;

  final List<Yard> yards;
  private final YardType yardType;
  private final YardListView parent;

  public YardAdapter(List<Yard> yards, final Context context, YardType yardType, YardListView parent) {
    this.yards = yards;
    this.yardType = yardType;
    this.parent = parent;
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

    holder.container.setOnClickListener(v -> flow.goTo(new YardDetailScreen(werf, yardType)));

    holder.delete.setVisibility(yardType == YardType.INVITED ? View.GONE : View.VISIBLE);

    holder.delete.setOnClickListener(
        v -> new SnackBar.Builder(context, parent).withMessage(
            "Are you sure you want to delete this yard?")
            .withActionMessage("Delete")
            .withTextColorId(R.color.green)
            .withOnClickListener(token -> {

              werf.deleteEventually(new DeleteCallback() {
                @Override public void done(ParseException e) {

                  if(e == null){
                    werf.unpinInBackground();
                    yards.remove(werf);

                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                  } else {
                    parseErrorHandler.handleParseError(e);
                  }
                }
              });
            })
            .show());
  }

  @Override public int getItemCount() {

    return yards.size();
  }

  static class YardViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.werf_item_container) ViewGroup container;

    @Bind(R.id.werf_item_naam) TextView naam;

    @Bind(R.id.werf_item_nr) TextView nr;

    @Bind(R.id.werf_item_delete) ImageView delete;

    public YardViewHolder(final View itemView) {

      super(itemView);

      ButterKnife.bind(this, itemView);
    }
  }
}
