package nigel.com.werfleider.ui.contact;

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
 * Created by nigel on 13/12/15.
 */
public class ContactsTopView extends LinearLayout {

  @Inject ContactsTopScreen.ContactsTopPresenter presenter;

  @Bind(R.id.contacts_tabs) PagerSlidingTabStrip tabStrip;

  @Bind(R.id.contacts_pager) ViewPager pager;

  public ContactsTopView(final Context context, final AttributeSet attrs) {

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
