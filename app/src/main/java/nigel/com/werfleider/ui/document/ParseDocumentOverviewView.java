package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentType;

/**
 * Created by nigel on 17/04/15.
 */
public class ParseDocumentOverviewView extends FrameLayout {

  @Inject ParseDocumentOverviewPresenter presenter;

  @Bind(R.id.document_overview_list) RecyclerView documentList;

  @Bind(R.id.loading_view) ProgressBarCircularIndeterminate loadingView;

  @Bind(R.id.empty_view) TextView emptyView;

  @Bind(R.id.create_view) AddFloatingActionButton create;

  @OnClick(R.id.create_view) public void onClick() {
    presenter.handleCreateClick();
  }

  public ParseDocumentOverviewView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if(!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);

      documentList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
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

  public void showContentView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(GONE);
    documentList.setVisibility(VISIBLE);
  }

  public void showEmptyView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(VISIBLE);
    documentList.setVisibility(GONE);
  }
}