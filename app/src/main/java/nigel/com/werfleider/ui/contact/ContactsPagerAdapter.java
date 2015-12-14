package nigel.com.werfleider.ui.contact;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import mortar.Mortar;
import nigel.com.werfleider.R;

import static nigel.com.werfleider.ui.contact.Contacts.ALL_USERS;
import static nigel.com.werfleider.ui.contact.Contacts.MY_CONTACTS;

/**
 * Created by nigel on 13/12/15.
 */
public class ContactsPagerAdapter extends PagerAdapter {
  public ContactsPagerAdapter(Context context) {

    Mortar.inject(context, this);
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view.equals(object);
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {

    final ContactsView contactsView = (ContactsView) LayoutInflater.from(container.getContext())
        .inflate(R.layout.contacts_view, container, false);

    contactsView.setContacts(position == 0 ? ALL_USERS : MY_CONTACTS);
    container.addView(contactsView);
    return contactsView;
  }

  @Override public CharSequence getPageTitle(int position) {
    return position == 0 ? "Users" : "My Contacts";
  }
}
