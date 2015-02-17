package nigel.com.werfleider.ui.werfoverzicht;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.model.DocumentType.PLAATSBESCHRIJF;

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
        ButterKnife.inject(this);
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }

    @OnClick(R.id.werfoverzicht_button_plaatsbeschrijf)
    public void plaatsBeschrijfClick() {
        presenter.goToDocumentView(PLAATSBESCHRIJF);
    }

    @OnClick(R.id.werfoverzicht_button_opmetingen)
    public void plannenClick() {
        presenter.goToDocumentView(OPMETINGEN);
    }


}
