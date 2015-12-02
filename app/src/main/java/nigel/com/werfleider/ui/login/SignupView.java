package nigel.com.werfleider.ui.login;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 01/04/15.
 */
public class SignupView extends ScrollView{

    @InjectView(R.id.signup_name) MaterialEditText name;

    @InjectView(R.id.signup_email) MaterialEditText email;

    @InjectView(R.id.signup_profession) MaterialEditText profession;

    @InjectView(R.id.signup_company) MaterialEditText company;

    @InjectView(R.id.signup_password) MaterialEditText password;

    @InjectView(R.id.signup_password_check) MaterialEditText check;

    @InjectViews({ R.id.signup_name, R.id.signup_email, R.id.signup_profession, R.id.signup_company, R.id.signup_password, R.id.signup_password_check})
    List<MaterialEditText> textFields;

    @Inject SignupScreen.Presenter presenter;

    public SignupView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
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

    @OnClick(R.id.signup_button)
    public void signUp(){

        if(emptyFields()){
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
        } else if(passwordsDontMatch()){
            Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
        } else {
            presenter.signUp();
        }
    }

    private boolean passwordsDontMatch() {

        return !password.getText().toString().equals(check.getText().toString());
    }

    private boolean emptyFields() {

        return Iterables.any(textFields, new Predicate<MaterialEditText>() {
                @Override public boolean apply(final MaterialEditText input) {

                    return input.getText().toString().length() <= 0;
                }
            });
    }
}
