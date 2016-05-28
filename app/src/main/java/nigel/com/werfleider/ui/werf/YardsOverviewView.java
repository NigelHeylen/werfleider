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
import nigel.com.werfleider.android.ActionBarOwner;

/**
 * Created by nigel on 14/12/15.
 */
public class YardsOverviewView extends LinearLayout {

  @Bind(R.id.yards_tabs) PagerSlidingTabStrip tabStrip;

  @Bind(R.id.yards_pager) ViewPager pager;

  @Inject ActionBarOwner actionBarOwner;

  @Inject int tab;

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
      pager.setAdapter(new YardsOverViewAdapter(getContext()));
      tabStrip.setViewPager(pager);
      pager.setCurrentItem(tab);

      actionBarOwner.setConfig(
          new ActionBarOwner.Config(false, true, getContext().getString(R.string.tWerven), null));
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    ButterKnife.unbind(this);
  }
}
