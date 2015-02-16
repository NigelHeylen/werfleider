package nigel.com.werfleider.ui.profession;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by nigel on 11/02/15.
 */
public class ProfessionListView extends LinearLayout {

    @InjectView(R.id.profession_name) TextView viewTitle;
    @InjectView(R.id.profession_user_name) EditText userName;
    @InjectView(R.id.profession_user_email) EditText userEmail;
    @InjectView(R.id.profession_contact_list) RecyclerView contactList;

    @OnClick(R.id.profession_user_add)
    public void addUser(){
        if(isNullOrEmpty(userName.getText().toString()) || isNullOrEmpty(userName.getText().toString())) {
            showToast("Please fill in the name and the email.");
        } else {
            presenter.addUser(userName.getText().toString(), userEmail.getText().toString());

        }
    }

    @Inject ProfessionListScreen.Presenter presenter;
    
    public ProfessionListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }


    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }

    public void showToast(final String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }
}
