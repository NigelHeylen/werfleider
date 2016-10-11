package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.astuetz.PagerSlidingTabStrip;
import com.google.common.base.Strings;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.model.Yard;

/**
 * Created by nigel on 17/04/15.
 */
public class YardDetailView extends LinearLayout {

  @Inject ActionBarOwner actionBarOwner;

  @Inject Yard yard;

  @Inject int tabIndex;

  @Bind(R.id.yard_document_pager) ViewPager documentPager;
  @Bind(R.id.yard_document_tabs) PagerSlidingTabStrip documentTabs;

  public YardDetailView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();
    ButterKnife.bind(this);
    initView();
  }

  protected void initView() {

    actionBarOwner.setConfig(new ActionBarOwner.Config(false, true,
        Strings.isNullOrEmpty(yard.getNaam()) ? getContext().getString(R.string.tNewYard)
            : yard.getNaam(), null));

    documentPager.setAdapter(new YardDetailDocumentAdapter(getContext()));
    documentPager.setOffscreenPageLimit(1);
    documentTabs.setViewPager(documentPager);
    documentPager.setCurrentItem(tabIndex);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    ButterKnife.unbind(this);
  }
}
