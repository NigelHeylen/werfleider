package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.astuetz.PagerSlidingTabStrip;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 17/12/14.
 */
public class LocationDetailView extends RelativeLayout {

  @Inject LocationDetailScreen.Presenter presenter;

  @Bind(R.id.document_image_list) RecyclerViewPager imageList;

  @Bind(R.id.document_image_detail_tabs) PagerSlidingTabStrip detailTabs;

  @Bind(R.id.document_image_detail_views) ViewPager detailViews;

  @Bind(R.id.document_menu_button) FloatingActionsMenu actionsMenu;

  @Bind(R.id.location_saver) ProgressBarCircularIndeterminate progressBar;

  public LocationDetailView(final Context context, final AttributeSet attrs) {

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

  @OnClick(R.id.document_save) public void clickSave() {

    progressBar.setVisibility(VISIBLE);
    presenter.handleSave();
  }

  @OnClick(R.id.document_edit) public void clickEdit() {

    presenter.handleEdit();
  }
}
