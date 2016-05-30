package nigel.com.werfleider.ui.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxTextView;
import flow.Flow;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Location;
import nigel.com.werfleider.model.Yard;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

import static java.lang.String.format;

/**
 * Created by nigel on 29/05/16.
 */
public class AddLocationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public static final int TITLE = 100;
  public static final int LOCATION = 200;
  private final LayoutInflater inflater;

  @Inject Yard yard;

  @Inject Flow flow;

  final CompositeSubscription subscription = new CompositeSubscription();

  public AddLocationsAdapter(Context context) {
    Mortar.inject(context, this);
    inflater = LayoutInflater.from(context);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TITLE) {
      return new TitleViewHolder(inflater.inflate(R.layout.title, parent, false));
    } else {
      return new LocationViewHolder(inflater.inflate(R.layout.add_location_entry, parent, false));
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    if (holder instanceof TitleViewHolder) {

      ((TitleViewHolder) holder).title.setText(
          format("%s: %d locaties", yard.getNaam(), yard.getLocations().size()));
    } else {

      yard.getLocations().get(position - 1).fetchIfNeededInBackground((object, e) -> {

        final Location location = (Location) object;

        if (e == null) {
          final LocationViewHolder viewHolder = (LocationViewHolder) holder;

          viewHolder.text.setText(location.getName());

          viewHolder.index.setText(String.valueOf(position));

          viewHolder.subscription = RxTextView.textChanges(viewHolder.text)
              .skip(1)
              .map(CharSequence::toString)
              .subscribe(location::setName);

          viewHolder.sublocations.setText(format("%d locaties", location.getSubLocations().size()));

          viewHolder.add.setOnClickListener(v -> {

            flow.goTo(new AddSubLocationScreen(yard, location));
          });
        }
      });
    }
  }

  @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    if (holder instanceof LocationViewHolder) {

      ((LocationViewHolder) holder).unsubscribe();
    }
  }

  @Override public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    if (holder instanceof LocationViewHolder) {

      ((LocationViewHolder) holder).unsubscribe();
    }
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    subscription.unsubscribe();
  }

  @Override public int getItemCount() {

    return yard.getLocations().size() + 1;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return TITLE;
    } else {
      return LOCATION;
    }
  }

  static class TitleViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.title) TextView title;

    public TitleViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  static class LocationViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.location_index) TextView index;

    @Bind(R.id.location_text) EditText text;

    @Bind(R.id.location_add) View add;

    @Bind(R.id.locations_number_sub_locations) TextView sublocations;

    Subscription subscription = Subscriptions.empty();

    public LocationViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void unsubscribe() {
      subscription.unsubscribe();
    }
  }
}
