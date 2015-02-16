package nigel.com.werfleider.ui.profession;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Contact;

/**
 * Created by nigel on 11/02/15.
 */
public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final List<Contact> contactList;

    public ContactListAdapter(final List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Contact contact = contactList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.naam.setText(contact.getNaam());
        viewHolder.email.setText(contact.getEmail());

    }

    @Override public int getItemCount() {
        return contactList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.contact_item_naam) TextView naam;
        @InjectView(R.id.contact_item_email) TextView email;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
