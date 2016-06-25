package nigel.com.werfleider.ui.contact;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.model.Contact;
import rx.subjects.PublishSubject;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.ui.contact.Contacts.ALL_USERS;
import static nigel.com.werfleider.ui.contact.Contacts.MY_CONTACTS;
import static nigel.com.werfleider.ui.contact.ContactsScreen.Module.USER_CONTACTS;
import static nigel.com.werfleider.ui.contact.ContactsScreen.Module.USER_DATA;
import static nigel.com.werfleider.ui.contact.SocialAction.Action.FOLLOW;
import static nigel.com.werfleider.ui.contact.SocialAction.Action.UNFOLLOW;
import static nigel.com.werfleider.util.ParseStringUtils.COMPANY;
import static nigel.com.werfleider.util.ParseStringUtils.CONTACTS;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.PROFESSION;

/**
 * Created by nigel on 13/12/15.
 */
public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

  private final Contacts contacts;

  @Inject @Named(USER_DATA) List<ParseUser> adapterData;
  @Inject @Named(USER_CONTACTS) List<Contact> contactsList;

  @Inject Resources resources;

  @Inject PublishSubject<SocialAction> socialActionBus;

  @Inject ParseErrorHandler parseErrorHandler;

  public ContactsAdapter(final Context context, final Contacts contacts) {
    this.contacts = contacts;

    Mortar.inject(context, this);
  }

  @Override public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    final View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

    return new ContactsViewHolder(view);
  }

  @Override public int getItemCount() {
    return contacts.equals(ALL_USERS) ? adapterData.size() : contactsList.size();
  }

  @Override public void onBindViewHolder(ContactsViewHolder holder, int position) {

    final ParseUser user = contacts.equals(ALL_USERS) ? adapterData.get(position) : null;

    final Contact contact = contacts.equals(MY_CONTACTS) ? contactsList.get(position) : null;

    if (user != null) {
      user.fetchIfNeededInBackground((object, e) -> {

        if (e == null) {
          holder.name.setText(user.getString(NAME));
          holder.company.setText(user.getString(COMPANY));
          holder.profession.setText(user.getString(PROFESSION));

          holder.follow.setImageDrawable(resources.getDrawable(
              follows(user) ? R.drawable.ic_person : R.drawable.ic_person_add));

          holder.follow.setOnClickListener(v -> {

            if (follows(user)) {
              final Contact contact1 = getContact(user);
              ParseUser.getCurrentUser().removeAll(CONTACTS, newArrayList(contact));
              ParseUser.getCurrentUser().saveEventually(e1 -> {

                if(e1 != null){
                  parseErrorHandler.handleParseError(e1);
                }
              });

              if(contact1 != null)
              contact1.deleteEventually(e1 -> {
                if(e1 != null){
                  parseErrorHandler.handleParseError(e1);
                }
              });

              holder.follow.setImageDrawable(resources.getDrawable(R.drawable.ic_person_add));
              contactsList.remove(contact1);

              socialActionBus.onNext(new SocialAction(user, UNFOLLOW));
            } else {

              final Contact newContact = ParseObject.create(Contact.class);
              newContact.setId(user.getObjectId())
                  .setNaam(user.getString(NAME))
                  .setBedrijf(user.getString(COMPANY))
                  .setEmail(user.getEmail())
                  .setProfession(user.getString(PROFESSION));

              newContact.pinInBackground();
              newContact.saveEventually(e1 -> {
                if(e1 != null) parseErrorHandler.handleParseError(e1);
              });

              ParseUser.getCurrentUser().addUnique(CONTACTS, newContact);
              ParseUser.getCurrentUser().saveEventually(e1 -> {
                if(e1 != null) parseErrorHandler.handleParseError(e1);
              });

              holder.follow.setImageDrawable(resources.getDrawable(R.drawable.ic_person));
              contactsList.add(newContact);
              socialActionBus.onNext(new SocialAction(user, FOLLOW));
            }
          });
        }
      });
    }

    if (contact != null) {

      contact.fetchIfNeededInBackground(((object, e) -> {

        if (e == null) {
          holder.name.setText(contact.getNaam());
          holder.company.setText(contact.getBedrijf());
          holder.profession.setText(contact.getProfession());

          holder.follow.setImageResource(R.drawable.ic_person);

          holder.follow.setOnClickListener(v -> {

            ParseUser.getCurrentUser().removeAll(CONTACTS, newArrayList(contact));
            ParseUser.getCurrentUser().saveEventually();

            holder.follow.setImageDrawable(resources.getDrawable(R.drawable.ic_person_add));
            contactsList.remove(contact);

            socialActionBus.onNext(new SocialAction(user, UNFOLLOW));
          });
        } else {
          parseErrorHandler.handleParseError(e);
        }
      }));
    }
  }

  private Contact getContact(ParseUser user) {
    final List<Contact> list = user.getList(CONTACTS);

    for (Contact contact : list) {

      if (contact.getId().equals(user.getObjectId())) {
        return contact;
      }
    }
    return null;
  }

  private boolean follows(ParseUser user) {
    for (Contact contact : contactsList) {
      if (contact.getId().equals(user.getObjectId())) {
        return true;
      }

    }
    return false;
  }

  public static class ContactsViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.contact_item_name) TextView name;
    @Bind(R.id.contact_item_profession) TextView profession;
    @Bind(R.id.contact_item_company) TextView company;
    @Bind(R.id.contact_item_follow) ImageView follow;

    public ContactsViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
