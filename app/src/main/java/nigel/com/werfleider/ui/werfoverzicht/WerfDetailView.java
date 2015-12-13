package nigel.com.werfleider.ui.werfoverzicht;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

import static nigel.com.werfleider.model.DocumentType.AS_BUILT;
import static nigel.com.werfleider.model.DocumentType.OPMERKINGEN;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;

/**
 * Created by nigel on 27/12/14.
 */
public class WerfDetailView extends ScrollView {

    @Inject WerfDetailScreen.Presenter presenter;

    public WerfDetailView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.werfoverzicht_button_plaatsbeschrijf)
    public void plaatsBeschrijfClick() {
        presenter.goToDocumentView(AS_BUILT);
    }

    @OnClick(R.id.werfoverzicht_button_opmetingen)
    public void opmetingenClick() {
        presenter.goToDocumentView(OPMETINGEN);
    }

    @OnClick(R.id.werfoverzicht_button_bemerkingen)
    public void plannenClick() {
        presenter.goToDocumentView(OPMERKINGEN);
    }


}
