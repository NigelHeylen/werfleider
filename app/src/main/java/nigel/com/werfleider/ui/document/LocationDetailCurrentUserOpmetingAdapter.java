package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseUser;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.ParseDocument;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailCurrentUserOpmetingAdapter extends PagerAdapter {

  @Inject ParseDocument document;

  public LocationDetailCurrentUserOpmetingAdapter(Context context) {

    Mortar.inject(context, this);
  }

  @Override public int getCount() {

    return document.getAuthor() == ParseUser.getCurrentUser() ? 5 : 4;
  }

  @Override public boolean isViewFromObject(final View view, final Object object) {

    return view == object;
  }

  @Override public Object instantiateItem(final ViewGroup container, final int position) {

    View view;

    switch (position) {
      case 0:
        view = getView(R.layout.location_detail_description, container);
        break;
      case 1:
        view = getView(R.layout.location_detail_info, container);
        break;
      case 2:
        view = getView(R.layout.location_detail_dimensions, container);
        break;
      case 3:
        view = getView(R.layout.location_audio_recorder, container);
        break;
      default:
        view = getView(R.layout.location_camera_intent, container);
        break;
    }

    container.addView(view);
    return view;
  }

  private View getView(@LayoutRes int layout, final ViewGroup container) {
    return LayoutInflater.from(container.getContext()).inflate(layout, container, false);
  }

  @Override public CharSequence getPageTitle(final int position) {

    switch (position) {
      case 0:
        return "Tekst";
      case 1:
        return "Info";
      case 2:
        return "Afmetingen";
      case 3:
        return "Audio";
      case 4:
        return "Camera";
      default:
        return "";
    }
  }

  @Override
  public void destroyItem(final ViewGroup container, final int position, final Object object) {

    container.removeView((View) object);
  }
}
