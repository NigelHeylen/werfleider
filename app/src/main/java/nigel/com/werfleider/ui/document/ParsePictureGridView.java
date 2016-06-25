package nigel.com.werfleider.ui.document;

import android.Manifest;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.tbruyelle.rxpermissions.RxPermissions;
import flow.Flow;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.model.DocumentLocation;

/**
 * Created by nigel on 17/04/15.
 */
public class ParsePictureGridView extends RelativeLayout {

  @Inject ParsePictureGridScreen.Presenter presenter;

  @Inject ActionBarOwner actionBarOwner;

  @Inject Flow flow;

  @Inject DocumentLocation location;

  @Bind(R.id.picture_grid) RecyclerView grid;

  @OnClick(R.id.picture_grid_save) public void save() {

    presenter.handleSave();
  }

  public ParsePictureGridView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();
    ButterKnife.bind(this);
    actionBarOwner.setConfig(
        new ActionBarOwner.Config(true, true, location.getDocumentType().name(), null));
    RxPermissions.getInstance(getContext())
        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
        .filter(aBoolean -> aBoolean)
        .subscribe(aBoolean -> {
          if (aBoolean) {
            presenter.takeView(ParsePictureGridView.this);
          } else {
            flow.goBack();
          }
        });
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }
}
