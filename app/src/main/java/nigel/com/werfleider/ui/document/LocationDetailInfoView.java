package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import com.rengwuxian.materialedittext.MaterialEditText;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailInfoView extends LinearLayout {

  @Inject LocationDetailInfoPresenter presenter;

  @Bind(R.id.document_detail_floor) MaterialEditText floor;

  @Bind(R.id.document_detail_location) MaterialEditText location;

  public LocationDetailInfoView(final Context context, final AttributeSet attrs) {

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


  @OnTextChanged(R.id.document_detail_location)
  public void changeLocation(){

    presenter.changeLocation(location.getText().toString());
  }

  @OnTextChanged(R.id.document_detail_floor)
  public void changeFloor(){

    presenter.changeFloor(floor.getText().toString());
  }
}
