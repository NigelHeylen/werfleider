package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 14/12/15.
 */
public class YardsOverViewAdapter extends PagerAdapter {
  private final Context context;

  public YardsOverViewAdapter(Context context) {
    this.context = context;
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view.equals(object);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {

    final YardListView view = (YardListView) LayoutInflater.from(container.getContext())
        .inflate(R.layout.yard_list_view, container, false);

    view.setYardType(position == 0 ? YardType.MINE : YardType.INVITED);

    container.addView(view);

    return view;
  }

  @Override public CharSequence getPageTitle(int position) {
    return context.getString(position == 0 ? R.string.tMyYards : R.string.tInvitedYards);
  }
}
