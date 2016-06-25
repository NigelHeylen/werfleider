package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import android.widget.Spinner;
import butterknife.Bind;
import butterknife.OnTextChanged;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.List;
import javax.inject.Inject;

import butterknife.ButterKnife;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailDimensionsView extends RelativeLayout {

  @Inject LocationDetailDimensionsPresenter presenter;

  @Bind(R.id.document_length) MaterialEditText length;

  @Bind(R.id.document_height) MaterialEditText height;

  @Bind(R.id.document_width) MaterialEditText width;

  @Bind(R.id.document_quantity) MaterialEditText quantity;

  @Bind(R.id.document_ms) MaterialEditText ms;

  @Bind(R.id.document_measuring_units) Spinner measuringUnits;

  @Bind({ R.id.document_length, R.id.document_width, R.id.document_height}) List<MaterialEditText>
      editTexts;

  public LocationDetailDimensionsView(final Context context, final AttributeSet attrs) {

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

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if(!isInEditMode()){
      ButterKnife.bind(this);
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }


  @OnTextChanged(R.id.document_width)
  public void changeWidth(){

    presenter.changeWidth(width.getText().toString());
  }

  @OnTextChanged(R.id.document_height)
  public void changeHeight(){

    presenter.changeHeight(height.getText().toString());
  }

  @OnTextChanged(R.id.document_length)
  public void changeLength(){

    presenter.changeLength(length.getText().toString());
  }

  @OnTextChanged(R.id.document_quantity)
  public void changeQuantity(){

    presenter.changeQuantity(quantity.getText().toString());
  }

  @OnTextChanged(R.id.document_ms)
  public void changeMS(){

    presenter.changeMS(ms.getText().toString());
  }
}
