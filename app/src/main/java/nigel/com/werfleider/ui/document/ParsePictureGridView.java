package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 17/04/15.
 */
public class ParsePictureGridView extends RelativeLayout {

    @Inject ParsePictureGridScreen.Presenter presenter;

    @Inject Picasso pablo;

    @InjectView(R.id.picture_grid) RecyclerView grid;

    @InjectView(R.id.picture_grid_progress) ProgressBarCircularIndeterminate progress;

    @OnClick(R.id.picture_grid_save)
    public void save() {

        progress.setVisibility(VISIBLE);
        presenter.handleSave();
    }

    public ParsePictureGridView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
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
