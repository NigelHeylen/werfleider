package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 25/11/14.
 */
public class ParseDocumentView extends RelativeLayout {

    @Inject ParseDocumentScreen.Presenter presenter;

    @Bind(R.id.document_locations) RecyclerView locations;

    @Bind(R.id.document_loader) ProgressBarCircularIndeterminate loader;

    @Bind(R.id.document_add_location) AddFloatingActionButton addLocation;

    @OnClick(R.id.document_add_location)
    public void newImageCollection() {

        presenter.newImageCollection();
    }


    @OnClick(R.id.document_create_pdf)
    public void createPdf() {

        presenter.write();
    }

    public ParseDocumentView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
    }

    @Override protected void onFinishInflate() {

        super.onFinishInflate();
        ButterKnife.bind(this);
        presenter.takeView(this);

        locations.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.unbind(this);
    }

    public void showLoader(boolean show){
        loader.setVisibility(show ? VISIBLE : GONE);
    }

}
