package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.astuetz.PagerSlidingTabStrip;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 14/12/15.
 */
public class YardsOverviewView extends LinearLayout {

  @Inject YardsOverviewScreen.YardsOverviewPresenter presenter;

  @Bind(R.id.yards_tabs) PagerSlidingTabStrip tabStrip;

  @Bind(R.id.yards_pager) ViewPager pager;

  public YardsOverviewView(final Context context, final AttributeSet attrs) {

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

      tabStrip.setViewPager(pager);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }
}
