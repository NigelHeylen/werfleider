package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.rengwuxian.materialedittext.MaterialEditText;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.model.DocumentType;

import static java.lang.String.format;

/**
 * Created by nigel on 09/01/16.
 */
public class DocumentCreateView extends ScrollView {

  @Inject DocumentCreateScreen.DocumentCreatePresenter presenter;

  @Inject DocumentType type;

  @Inject ActionBarOwner actionBarOwner;

  @Bind(R.id.document_name) MaterialEditText name;

  @OnClick(R.id.document_save) public void save() {

    presenter.save(name.getText().toString());
  }

  public DocumentCreateView(final Context context, final AttributeSet attrs) {

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
      actionBarOwner.setConfig(
          new ActionBarOwner.Config(false, true, format("Nieuw %s document", type.toString()),
              null));
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }
}
