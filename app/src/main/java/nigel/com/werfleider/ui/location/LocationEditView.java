package nigel.com.werfleider.ui.location;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.rengwuxian.materialedittext.MaterialEditText;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 12/01/16.
 */
public class LocationEditView extends LinearLayout {

  @Inject LocationEditScreen.LocationEditPresenter presenter;

  @Bind(R.id.location_title) MaterialEditText title;

  @Bind(R.id.location_art_nr) MaterialEditText artNr;

  public LocationEditView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) {
      Mortar.inject(context, this);
    }
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();

    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.location_save) public void save(){

    presenter.handleSave(title.getText().toString(), artNr.getText().toString());
  }
}
