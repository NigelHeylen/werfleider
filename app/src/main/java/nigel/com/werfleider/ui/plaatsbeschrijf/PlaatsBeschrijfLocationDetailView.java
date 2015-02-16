package nigel.com.werfleider.ui.plaatsbeschrijf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.PlaatsBeschrijf;
import nigel.com.werfleider.model.PlaatsBeschrijfLocatie;
import nigel.com.werfleider.model.StartCameraEvent;
import nigel.com.werfleider.model.Werf;

/**
 * Created by nigel on 17/12/14.
 */
public class PlaatsBeschrijfLocationDetailView extends LinearLayout {
    @Inject PlaatsBeschrijfLocationDetailScreen.Presenter presenter;

    @Inject Bus bus;

    @Inject Flow flow;

    @Inject Picasso pablo;

    @Inject Werf werf;

    private ViewHolder holder;

    public PlaatsBeschrijfLocationDetailView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        holder = new ViewHolder(this);
        presenter.takeView(this);

    }


    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void initAdapter(final PlaatsBeschrijf plaatsBeschrijf, final int position){

        final PlaatsBeschrijfLocatie collection = plaatsBeschrijf.getFotoReeksList().get(position);

        holder.mTitle.setText(collection.getLocation());

        holder
                .mCamera
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override public void onClick(final View v) {
                                bus.post(new StartCameraEvent(position));
                            }
                        });

        holder
                .mEdit
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override public void onClick(final View v) {
                                flow.goTo(new PictureGridScreen(plaatsBeschrijf, collection, werf));
                            }
                        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        holder.imageList.setLayoutManager(linearLayoutManager);
        holder.imageList.setAdapter(new PlaatsBechrijfImageListItemAdapter(collection, pablo, holder.description));

    }

    static class ViewHolder {

        @InjectView(R.id.plaatsbeschrijf_title) TextView mTitle;
        @InjectView(R.id.plaatsbeschrijf_camera) ImageView mCamera;
        @InjectView(R.id.plaatsbeschrijf_edit) ImageView mEdit;

        @InjectView(R.id.plaatsbeschrijf_description) EditText description;

        @InjectView(R.id.plaatsbeschrijf_image_list) RecyclerView imageList;

        public ViewHolder(final View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }
}
