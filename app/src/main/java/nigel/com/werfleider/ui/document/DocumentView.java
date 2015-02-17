package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.otto.Bus;
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

    @Inject Bus bus;

    @Inject Flow flow;


    @InjectView(R.id.plaatsbeschrijf_locations) RecyclerView locations;

    @OnClick(R.id.plaatsbeschrijf_floating_button)
    public void newImageCollection() {
        presenter.newImageCollection();
    }


    @OnClick(R.id.document_button_write)
    public void write() {
        presenter.write();
    }

    public DocumentView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);

    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }

    public void showToast(final String message) {

        Toast.makeText(
                getContext(),
                message, Toast.LENGTH_SHORT)
             .show();
    }

    public void initView(final Document document) {
        initLocations(document);
    }

    private void initLocations(final Document document) {
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        final DocumentLocationAdapter adapter = new DocumentLocationAdapter(document, pablo, flow);

        Mortar.inject(getContext(), adapter);

        locations.setLayoutManager(layoutManager);
        locations.setAdapter(adapter);
    }

    public void saveDocument() {
        presenter.saveDocument();
    }
}
