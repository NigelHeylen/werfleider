package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
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
public class LocationDetailDescriptionView extends MaterialEditText {

  @Inject LocationDetailDescriptionPresenter presenter;

  @Bind(R.id.document_description) MaterialEditText description;

  public LocationDetailDescriptionView(final Context context, final AttributeSet attrs) {

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


  @OnTextChanged(R.id.document_description)
  public void changeDescription(){

    presenter.changeDescription(description.getText().toString());
  }
}
