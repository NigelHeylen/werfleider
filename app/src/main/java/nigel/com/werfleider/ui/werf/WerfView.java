package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 31/01/15.
 */
public class WerfView extends RelativeLayout {

    @InjectView(R.id.werf_list) RecyclerView werfList;

    @InjectView(R.id.werf_loader) ProgressBarCircularIndeterminate loader;

    @Inject WerfScreen.Presenter presenter;

    public WerfView(final Context context, final AttributeSet attrs) {

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

    @OnClick(R.id.werf_create)
    public void createWerf() {

        presenter.handleCreate();
    }

    public void setLayoutManager(final LinearLayoutManager linearLayoutManager) {
        werfList.setLayoutManager(linearLayoutManager);
    }

    public void setAdapter(final WerfAdapter adapter) {
        werfList.setAdapter(adapter);
    }
}
