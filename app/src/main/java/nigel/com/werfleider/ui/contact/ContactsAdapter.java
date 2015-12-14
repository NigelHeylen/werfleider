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
import com.parse.ParseUser;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import mortar.Mortar;
import nigel.com.werfleider.R;
import rx.subjects.PublishSubject;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.ui.contact.Contacts.ALL_USERS;
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
  @Inject @Named(USER_CONTACTS) List<ParseUser> contactsList;

  @Inject Resources resources;

  @Inject PublishSubject<SocialAction> socialActionBus;

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

    final ParseUser user =
        contacts.equals(ALL_USERS) ? adapterData.get(position) : contactsList.get(position);

    user.fetchIfNeededInBackground((object, e) -> {

      if(e == null) {
        holder.name.setText(user.getString(NAME));
        holder.company.setText(user.getString(COMPANY));
        holder.profession.setText(user.getString(PROFESSION));

        holder.follow.setImageDrawable(resources.getDrawable(follows(user) ? R.drawable.ic_person : R.drawable.ic_person_add));

        holder.follow.setOnClickListener(v -> {

          if (follows(user)) {

            ParseUser.getCurrentUser().removeAll(CONTACTS, newArrayList(user));
            ParseUser.getCurrentUser().saveEventually(t -> {

              if (t == null) {

                holder.follow.setImageDrawable(resources.getDrawable(R.drawable.ic_person_add));
                contactsList.remove(user);

                socialActionBus.onNext(new SocialAction(user, UNFOLLOW));
              }
            });
          } else {

            ParseUser.getCurrentUser().addUnique(CONTACTS, user);
            ParseUser.getCurrentUser().saveEventually(t -> {

              if (t == null) {

                holder.follow.setImageDrawable(resources.getDrawable(R.drawable.ic_person));
                contactsList.add(user);
                socialActionBus.onNext(new SocialAction(user, FOLLOW));
              }
            });
          }
        });
      }
    });


  }

  private boolean follows(ParseUser user) {
    return contactsList.contains(user);
  }

  static class ContactsViewHolder extends RecyclerView.ViewHolder {

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
