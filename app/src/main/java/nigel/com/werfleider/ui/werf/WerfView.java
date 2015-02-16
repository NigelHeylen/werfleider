package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import javax.inject.Inject;

import butterknife.ButterKnife;
import mortar.Mortar;

/**
 * Created by nigel on 31/01/15.
 */
public class WerfView extends RecyclerView {

    @Inject WerfScreen.Presenter presenter;

    public WerfView(final Context context, final AttributeSet attrs) {
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
}
