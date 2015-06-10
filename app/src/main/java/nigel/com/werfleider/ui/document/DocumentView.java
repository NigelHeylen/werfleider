package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Document;

/**
 * Created by nigel on 25/11/14.
 */
public class DocumentView extends RelativeLayout {

    @Inject DocumentScreen.Presenter presenter;

    @Inject Picasso pablo;

    @Inject Flow flow;

    @InjectView(R.id.document_locations) RecyclerView locations;

    @InjectView(R.id.document_loader) ProgressBarCircularIndeterminate loader;

    @OnClick(R.id.document_add_location)
    public void newImageCollection() {

        presenter.newImageCollection();
    }


    @OnClick(R.id.document_create_pdf)
    public void createPdf() {

        presenter.write();
    }

    public DocumentView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
    }

    @Override protected void onFinishInflate() {

        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);

        locations.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }

    public void showLoader(boolean show){
        loader.setVisibility(show ? VISIBLE : GONE);
    }

    public void initView(final Document document) {
        initLocations(document);
    }

    private void initLocations(final Document document) {

        final DocumentLocationAdapter adapter = new DocumentLocationAdapter(document);

        Mortar.inject(
                getContext(),
                adapter);

        locations.setAdapter(adapter);
    }

}
