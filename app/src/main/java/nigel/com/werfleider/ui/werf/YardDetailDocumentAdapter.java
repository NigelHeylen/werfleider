package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.ParseUser;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewView;

/**
 * Created by nigel on 17/04/15.
 */
public class YardDetailDocumentAdapter extends PagerAdapter {

  private final Context context;

  @Inject Yard yard;

  final LayoutInflater inflater;

  public YardDetailDocumentAdapter(final Context context) {

    Mortar.inject(context, this);
    this.context = context;
    inflater = LayoutInflater.from(context);
  }

  @Override public int getCount() {

    return (yard.getAuthor() == ParseUser.getCurrentUser() ? DocumentType.values().length + 1
        : DocumentType.values().length) + 1;
  }

  @Override public boolean isViewFromObject(final View view, final Object object) {

    return object == view;
  }

  @Override public Object instantiateItem(final ViewGroup container, final int position) {

    if (position == 0) {

      final View view = inflater.inflate(R.layout.yard_create_view, container, false);
      container.addView(view);
      return view;
    } else if (position < DocumentType.values().length + 1) {
      final ParseDocumentOverviewView view =
          (ParseDocumentOverviewView) inflater.inflate(R.layout.parsedocument_overview_view,
              container, false);

      view.setDocumentType(DocumentType.values()[position - 1]);
      container.addView(view);
      return view;
    } else {

      final View view = inflater.inflate(R.layout.invite_contacts_view, container, false);
      container.addView(view);
      return view;
    }
  }

  @Override
  public void destroyItem(final ViewGroup container, final int position, final Object object) {

    container.removeView((View) object);
  }

  @Override public CharSequence getPageTitle(final int position) {

    if(position == 0){
      return "Details";
    } else if(position < DocumentType.values().length + 1){
      return DocumentType.values()[position - 1].name();
    } else {
      return "Invites";
    }
  }
}
