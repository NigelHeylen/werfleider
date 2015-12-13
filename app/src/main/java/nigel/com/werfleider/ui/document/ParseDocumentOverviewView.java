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
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentType;

/**
 * Created by nigel on 17/04/15.
 */
public class ParseDocumentOverviewView extends RelativeLayout {

    @Inject ParseDocumentOverviewPresenter presenter;

    @Bind(R.id.document_overview_list) RecyclerView documentList;

    @Bind(R.id.document_overview_loader) ProgressBarCircularIndeterminate loader;

    @OnClick(R.id.document_overview_create_button)
    public void onClick(){
        presenter.handleCreateClick();
    }

    public ParseDocumentOverviewView(final Context context, final AttributeSet attrs) {
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

        documentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);

        ButterKnife.unbind(this);
    }

    public void setAdapter(final RecyclerView.Adapter adapter) {
        documentList.setAdapter(adapter);
    }

    public void setDocumentType(final DocumentType documentType) {

        presenter.setDocumentType(documentType);

    }
}