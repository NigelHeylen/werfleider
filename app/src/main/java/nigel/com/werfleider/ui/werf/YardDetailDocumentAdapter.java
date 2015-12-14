package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.ui.document.ParseDocumentOverviewView;

/**
 * Created by nigel on 17/04/15.
 */
public class YardDetailDocumentAdapter extends PagerAdapter {

  private final Context context;

  public YardDetailDocumentAdapter(final Context context) {

    Mortar.inject(context, this);
    this.context = context;
  }

  @Override public int getCount() {

    return DocumentType.values().length + 1;
  }

  @Override public boolean isViewFromObject(final View view, final Object object) {

    return object == view;
  }

  @Override public Object instantiateItem(final ViewGroup container, final int position) {

    if (position < DocumentType.values().length) {
      final ParseDocumentOverviewView view =
          (ParseDocumentOverviewView) LayoutInflater.from(context)
              .inflate(R.layout.parsedocument_overview_view, container, false);

      view.setDocumentType(DocumentType.values()[position]);
      container.addView(view);
      return view;
    } else {

      final InviteContactsView view = (InviteContactsView) LayoutInflater.from(context)
          .inflate(R.layout.invite_contacts_view, container, false);
      container.addView(view);
      return view;
    }
  }

  @Override
  public void destroyItem(final ViewGroup container, final int position, final Object object) {

    container.removeView((View) object);
  }

  @Override public CharSequence getPageTitle(final int position) {

    return position < DocumentType.values().length ? DocumentType.values()[position].toString()
        : context.getString(R.string.tInvites);
  }
}
