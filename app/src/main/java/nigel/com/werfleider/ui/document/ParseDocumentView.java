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
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.ParseDocument;

import static java.lang.String.format;

/**
 * Created by nigel on 25/11/14.
 */
public class ParseDocumentView extends FrameLayout {

  @Inject ParseDocumentScreen.Presenter presenter;

  @Inject ParseDocument document;

  @Bind(R.id.document_locations) RecyclerView locations;

  @Bind(R.id.loading_view) ProgressBarCircularIndeterminate loadingView;

  @Bind(R.id.create_view) AddFloatingActionButton createView;

  @Bind(R.id.empty_view) TextView emptyView;

  @Inject ActionBarOwner actionBarOwner;

  @OnClick(R.id.create_view) public void newImageCollection() {

    presenter.newImageCollection();
  }

  public ParseDocumentView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();
    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);

      actionBarOwner.setConfig(
          new ActionBarOwner.Config(false, true, format("%s: %s",document.getDocumentType().toString(), document.getName()),
              new ActionBarOwner.MenuAction(getContext().getString(R.string.tGeneratePdf),
                  () -> presenter.generatePdf(), R.drawable.ic_pdf)));

      locations.setLayoutManager(new LinearLayoutManager(getContext()));
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  public void showLoader(boolean show) {
    loadingView.setVisibility(show ? VISIBLE : GONE);
  }

  public void showContentView() {

    loadingView.setVisibility(GONE);
    emptyView.setVisibility(GONE);
    locations.setVisibility(VISIBLE);
  }

  public void showEmptyView() {

    loadingView.setVisibility(GONE);
    locations.setVisibility(GONE);
    emptyView.setVisibility(VISIBLE);
  }

  public void initEmptyView(DocumentType documentType) {

    emptyView.setText(documentType.getTextRes());
    emptyView.setCompoundDrawablesWithIntrinsicBounds(null,
        getContext().getDrawable(documentType.getDrawableRes()), null, null);
  }
}
