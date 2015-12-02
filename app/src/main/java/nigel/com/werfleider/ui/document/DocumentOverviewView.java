package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 14/03/15.
 */
public class DocumentOverviewView extends RelativeLayout {

    @Inject DocumentOverviewScreen.Presenter presenter;

    @InjectView(R.id.document_overview_list) RecyclerView documentList;

    @OnClick(R.id.document_overview_create_button)
    public void onClick(){
        presenter.handleCreateClick();
    }

    public DocumentOverviewView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        presenter.takeView(this);

        documentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);

        ButterKnife.reset(this);
    }

    public void setAdapter(final RecyclerView.Adapter adapter) {
        documentList.setAdapter(adapter);
    }
}
