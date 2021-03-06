package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseUser;
import java.util.Objects;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentLocation;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailCurrentUserRegularAdapter extends PagerAdapter {

  @Inject DocumentLocation document;

  final LayoutInflater inflater;

  public LocationDetailCurrentUserRegularAdapter(Context context) {

    Mortar.inject(context, this);
    inflater = LayoutInflater.from(context);
  }

  @Override public int getCount() {

    return Objects.equals(document.getCreator(), ParseUser.getCurrentUser().getEmail()) ? 4 : 3;
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
    return inflater.inflate(layout, container, false);
  }

  @Override public CharSequence getPageTitle(final int position) {

    switch (position) {
      case 0:
        return "Tekst";
      case 1:
        return "Info";
      case 2:
        return "Audio";
      case 3:
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
