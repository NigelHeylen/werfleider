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
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Location;
import nigel.com.werfleider.model.SubLocation;
import nigel.com.werfleider.ui.location.AddLocationsAdapter.TitleViewHolder;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by nigel on 30/05/16.
 */
public class AddSubLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

  public static final int TITLE = 100;
  public static final int LOCATION = 200;
  private final LayoutInflater inflater;

  @Inject Location location;

  final CompositeSubscription subscription = new CompositeSubscription();

  public AddSubLocationAdapter(Context context) {
    Mortar.inject(context, this);
    inflater = LayoutInflater.from(context);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == TITLE) {
      return new TitleViewHolder(inflater.inflate(R.layout.title, parent, false));
    } else {
      return new SubLocationViewHolder(inflater.inflate(R.layout.add_sub_location_entry, parent, false));
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    if (holder instanceof TitleViewHolder) {

      ((TitleViewHolder) holder).title.setText("Locatie: " + location.getName());
    } else {

      location.getSubLocations().get(position - 1).fetchIfNeededInBackground((object, e) -> {

        final SubLocation subLocation = (SubLocation) object;

        if(e == null){
          final SubLocationViewHolder viewHolder = (SubLocationViewHolder) holder;

          viewHolder.index.setText(String.valueOf(position));

          viewHolder.text.setText(subLocation.getName());

          viewHolder.subscription = RxTextView.textChanges(viewHolder.text)
              .skip(1)
              .map(CharSequence::toString)
              .subscribe(subLocation::setName);

        }
      });
    }
  }

  @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    if(holder instanceof SubLocationViewHolder){

      ((SubLocationViewHolder) holder).unsubscribe();
    }
  }

  @Override public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    if(holder instanceof SubLocationViewHolder){

      ((SubLocationViewHolder) holder).unsubscribe();
    }
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    subscription.unsubscribe();
  }

  @Override public int getItemCount() {

    return location.getSubLocations().size() + 1;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return TITLE;
    } else {
      return LOCATION;
    }
  }


  static class SubLocationViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.sub_location_text) EditText text;
    @Bind(R.id.sub_location_index) TextView index;

    private Subscription subscription = Subscriptions.empty();

    public SubLocationViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    public void unsubscribe(){
      subscription.unsubscribe();
    }
  }
}
