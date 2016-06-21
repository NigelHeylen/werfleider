package nigel.com.werfleider.ui.document;

import android.Manifest;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.github.mrengineer13.snackbar.SnackBar;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tbruyelle.rxpermissions.RxPermissions;
import flow.Flow;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.pdf.MeasurementsFileOperations;
import nigel.com.werfleider.pdf.ParseFileOperations;
import nigel.com.werfleider.util.FlowUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static android.view.View.VISIBLE;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.util.ParseStringUtils.CREATED_AT;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by nigel on 14/03/15.
 */
public class ParseDocumentOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final List<DocumentLocation> documents;
  private final ParseDocumentOverviewView parent;

  @Inject Context context;

  @Inject Flow flow;

  @Inject Yard yard;

  @Inject ParseFileOperations fop;

  @Inject MeasurementsFileOperations measurementsFileOperations;

  final CompositeSubscription subscription = new CompositeSubscription();

  public ParseDocumentOverviewAdapter(final Context context, final List<DocumentLocation> documents,
      ParseDocumentOverviewView parent) {

    this.documents = documents;
    this.parent = parent;
    Mortar.inject(context, this);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

    final View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.document_overview_item, parent, false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

    final ViewHolder viewHolder = (ViewHolder) holder;

    final DocumentLocation document = documents.get(position);

    //viewHolder.date.setText(new DateTime(document.getCreatedAt()).toString("dd-MM-yyyy"));
    viewHolder.date.setText(document.getArtNr());
    viewHolder.name.setText(document.getTitle());

    viewHolder.container.setOnClickListener(
        v -> flow.goTo(new LocationDetailScreen(yard, document)));

    viewHolder.delete.setVisibility(
        yard.getAuthor() == ParseUser.getCurrentUser() ? VISIBLE : View.GONE);
    viewHolder.edit.setVisibility(
        yard.getAuthor() == ParseUser.getCurrentUser() ? VISIBLE : View.GONE);
    viewHolder.pdf.setVisibility(
        yard.getAuthor() == ParseUser.getCurrentUser() ? VISIBLE : View.GONE);

    viewHolder.edit.setOnClickListener(v -> flow.goTo(
        new ParsePictureGridScreen(document, yard, FlowUtils.getCurrentScreen(flow))));
    viewHolder.pdf.setOnClickListener(v ->

        RxPermissions.getInstance(context)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe(aBoolean -> {
              if(aBoolean){
                subscription.add(generatePdf(yard, document));
              }
            }));

    viewHolder.delete.setOnClickListener(v -> new SnackBar.Builder(context, parent).withMessage(
        "Are you sure you want to delete this document?")
        .withActionMessage("Delete")
        .withTextColorId(R.color.green)
        .withOnClickListener(token -> {
          document.deleteEventually();
          document.unpinInBackground();

          documents.remove(position);
          notifyItemRemoved(position);
          notifyDataSetChanged();
        })
        .show());
  }

  @Override public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    subscription.unsubscribe();
  }

  public Subscription generatePdf(final Yard yard, final DocumentLocation location) {

    Toast.makeText(context, location.getDocumentType().toString() + " maken...", Toast.LENGTH_LONG).show();

    return Observable.create(new Observable.OnSubscribe<Boolean>() {

      @Override public void call(final Subscriber<? super Boolean> subscriber) {

        ParseQuery.getQuery(ParseDocumentImage.class)
            .fromLocalDatastore()
            .orderByAscending(CREATED_AT)
            .whereEqualTo(LOCATION_ID, location)
            .findInBackground((list, e) -> {

              if (e == null) {

                final Multimap<String, ParseDocumentImage> documentImageMultiMap =
                    ArrayListMultimap.create();

                for (ParseDocumentImage parseDocumentImage : list) {

                  documentImageMultiMap.put(
                      parseDocumentImage.getFloor(),
                      parseDocumentImage);
                }

                final boolean finished = fop.write(yard, location, documentImageMultiMap);
                if (finished) {
                  subscriber.onNext(true);
                } else {
                  subscriber.onError(new IOException("Het maken van de pdf is gefaald."));
                }
                subscriber.onCompleted();
              } else {
                e.printStackTrace();
                subscriber.onError(e);
              }

              subscriber.onNext(true);
            });
      }
    }).subscribeOn(io()).observeOn(mainThread()).subscribe(new Observer<Boolean>() {
      @Override public void onCompleted() {

        Toast.makeText(context, location.getDocumentType().toString() + ".pdf gemaakt", Toast.LENGTH_LONG).show();

        if (location.getDocumentType() == OPMETINGEN) {
          writeMeasurementsDocument(location);
        }
      }

      @Override public void onError(final Throwable e) {

        e.printStackTrace();
        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
      }

      @Override public void onNext(final Boolean aBoolean) {

      }
    });
  }

  private void writeMeasurementsDocument(final DocumentLocation location) {

    Toast.makeText(context, location.getDocumentType().toString() + " maken...", Toast.LENGTH_LONG).show();

    Observable.create(new Observable.OnSubscribe<Boolean>() {

      @Override public void call(final Subscriber<? super Boolean> subscriber) {

        ParseQuery.getQuery(ParseDocumentImage.class)
            .whereEqualTo(LOCATION_ID, location)
            .fromLocalDatastore()
            .orderByAscending(CREATED_AT)
            .findInBackground(new FindCallback<ParseDocumentImage>() {
              @Override
              public void done(final List<ParseDocumentImage> list, final ParseException e) {

                if (e == null) {


                  final Multimap<String, ParseDocumentImage> documentImageMultiMap =
                      ArrayListMultimap.create();

                  for (ParseDocumentImage parseDocumentImage : list) {

                    documentImageMultiMap.put(
                        parseDocumentImage.getFloor(),
                        parseDocumentImage);
                  }

                  final boolean finished =
                      measurementsFileOperations.writeDocument(yard, location,
                          documentImageMultiMap);
                  if (finished) {
                    subscriber.onNext(true);
                  } else {
                    subscriber.onError(new IOException("Het maken van een pdf is gefaald."));
                  }
                  subscriber.onCompleted();
                } else {
                  e.printStackTrace();
                  subscriber.onError(e);
                }

                subscriber.onNext(true);
              }
            });
      }
    }).subscribeOn(io()).observeOn(mainThread()).subscribe(new Observer<Boolean>() {
      @Override public void onCompleted() {

        Toast.makeText(context, "measurements document created", Toast.LENGTH_LONG).show();
      }

      @Override public void onError(final Throwable e) {

        e.printStackTrace();
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
      }

      @Override public void onNext(final Boolean aBoolean) {

      }
    });
  }

  @Override public int getItemCount() {
    return documents.size();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.document_overview_item_container) CardView container;

    @Bind(R.id.document_overview_item_delete) ImageView delete;
    @Bind(R.id.document_overview_item_edit) ImageView edit;
    @Bind(R.id.document_overview_item_pdf) ImageView pdf;

    @Bind(R.id.document_overview_item_date) TextView date;
    @Bind(R.id.document_overview_item_name) TextView name;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
