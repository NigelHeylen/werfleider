package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.astuetz.PagerSlidingTabStrip;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 17/04/15.
 */
public class YardDetailView extends LinearLayout {

    @Bind(R.id.yard_document_pager) ViewPager documentPager;
    @Bind(R.id.yard_document_tabs) PagerSlidingTabStrip documentTabs;

    public YardDetailView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
    }

    @Override protected void onFinishInflate() {

        super.onFinishInflate();
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {

        documentPager.setAdapter(new YardDetailDocumentAdapter(getContext()));
        documentPager.setOffscreenPageLimit(1);
        documentTabs.setViewPager(documentPager);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }

}
