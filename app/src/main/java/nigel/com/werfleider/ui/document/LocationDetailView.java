package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 17/12/14.
 */
public class LocationDetailView extends FrameLayout {

  @Inject LocationDetailScreen.Presenter presenter;

  @Bind(R.id.content_view) LinearLayout contentView;

  @Bind(R.id.empty_view) TextView emptyView;

  @Bind(R.id.document_image_list) RecyclerViewPager imageList;

  @Bind(R.id.document_image_detail_tabs) PagerSlidingTabStrip detailTabs;

  @Bind(R.id.document_image_detail_views) ViewPager detailViews;

  @Bind(R.id.document_add_images) AddFloatingActionButton addImages;

  public LocationDetailView(final Context context, final AttributeSet attrs) {

    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {

    super.onFinishInflate();
    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {

    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  @OnClick(R.id.document_add_images) public void clickAddImages() {

    presenter.handleEdit();
  }

  public void showContentView() {

    emptyView.setVisibility(GONE);
    contentView.setVisibility(VISIBLE);
  }

  public void showEmptyView() {

    contentView.setVisibility(GONE);
    emptyView.setVisibility(VISIBLE);
  }
}
