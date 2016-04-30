package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;

/**
 * Created by nigel on 25/11/14.
 */
public class ParseDocumentView extends FrameLayout {

  @Inject ParseDocumentScreen.Presenter presenter;

  @Bind(R.id.document_locations) RecyclerView locations;

  @Bind(R.id.document_loader) ProgressBarCircularIndeterminate loader;

  @Bind(R.id.document_add_location) AddFloatingActionButton addLocation;

  @Inject ActionBarOwner actionBarOwner;

  @OnClick(R.id.document_add_location) public void newImageCollection() {

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
          new ActionBarOwner.Config(false, true, presenter.document.getDocumentType().toString(),
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
    loader.setVisibility(show ? VISIBLE : GONE);
  }
}
