package nigel.com.werfleider.ui.werf;

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
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.util.ParseStringUtils;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.ui.werf.InviteContactsScreen.CONTACTS;
import static nigel.com.werfleider.ui.werf.InviteContactsScreen.INVITES;
import static nigel.com.werfleider.ui.werf.InviteContactsScreen.INVITES_YARD;
import static nigel.com.werfleider.util.ParseStringUtils.COMPANY;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.PROFESSION;

/**
 * Created by nigel on 14/12/15.
 */
public class InvitesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  @Inject @Named(CONTACTS) List<ParseUser> contacts;

  @Inject @Named(INVITES) List<ParseUser> invites;

  @Inject Resources resources;

  @Inject @Named(INVITES_YARD) Yard yard;

  public static final int HEADER = 200;

  public static final int CONTENT = 100;

  public InvitesRecyclerAdapter(Context context) {

    Mortar.inject(context, this);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if(viewType == CONTENT) {
      final View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

      return new InvitesViewHolder(view);
    } else {

      final View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.invites_header, parent, false);

      return new HeaderViewHolder(view);
    }
  }

  @Override public int getItemCount() {
    return invites.size() + contacts.size() + 2;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


    if(holder instanceof InvitesViewHolder){
      final ParseUser user = getUser(position);

      final InvitesViewHolder viewHolder = (InvitesViewHolder) holder;

      user.fetchIfNeededInBackground((object, e) -> {

        if (e == null) {
          viewHolder.name.setText(user.getString(NAME));
          viewHolder.company.setText(user.getString(COMPANY));
          viewHolder.profession.setText(user.getString(PROFESSION));

          viewHolder.follow.setImageDrawable(resources.getDrawable(
              invited(user) ? R.drawable.ic_person : R.drawable.ic_person_add));

          viewHolder.follow.setOnClickListener(v -> {

            if (invited(user)) {

              yard.removeAll(ParseStringUtils.INVITES, newArrayList(user));
              yard.saveEventually();
              viewHolder.follow.setImageDrawable(
                  resources.getDrawable(R.drawable.ic_person_add));
              invites.remove(user);
              notifyDataSetChanged();
            } else {

              yard.addUnique(ParseStringUtils.INVITES, user);
              yard.saveEventually();

              viewHolder.follow.setImageDrawable(resources.getDrawable(R.drawable.ic_person));
              invites.add(user);
              notifyDataSetChanged();
            }
          });
        }
      });

    } else {

      HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
      viewHolder.header.setText(position == 0 ? R.string.tInvites : R.string.tContacts);
    }
  }

  private boolean invited(ParseUser user) {
    return invites.contains(user);
  }

  private ParseUser getUser(int position) {

    if(position < invites.size() + 1){

       return invites.get(position - 1);
    } else {

      return contacts.get(position - 2 - invites.size());
    }
  }

  @Override public int getItemViewType(int position) {
    return position == 0 || position == invites.size() + 1 ? HEADER : CONTENT;
  }

  static class InvitesViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.contact_item_name) TextView name;
    @Bind(R.id.contact_item_profession) TextView profession;
    @Bind(R.id.contact_item_company) TextView company;
    @Bind(R.id.contact_item_follow) ImageView follow;

    public InvitesViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  static class HeaderViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.invites_header) TextView header;

    public HeaderViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
