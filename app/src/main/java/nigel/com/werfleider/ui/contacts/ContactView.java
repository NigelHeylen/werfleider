package nigel.com.werfleider.ui.contacts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

import static nigel.com.werfleider.model.Profession.ARCHITECT;
import static nigel.com.werfleider.model.Profession.BOUWMEESTER;

/**
 * Created by nigel on 11/02/15.
 */
public class ContactView extends RelativeLayout {

    @Inject ContactScreen.Presenter presenter;

    @InjectView(R.id.contact_architect_people) TextView   architectCount;

    @InjectView(R.id.contact_bouwmeester_people) TextView bouwmeesterCount;

    @InjectView(R.id.take_picture_surface) SurfaceView surfaceView;


    @OnClick(R.id.take_photo)
    public void takePhoto() {

        presenter.takePhoto();
    }

    @OnClick(R.id.contact_architect_container)
    public void goToArchitect() {

        presenter.goToProfessionDetailView(ARCHITECT);
    }

    @OnClick(R.id.contact_bouwmeester_container)
    public void goToBouwmeester() {

        presenter.goToProfessionDetailView(BOUWMEESTER);
    }

    public ContactView(final Context context, final AttributeSet attrs) {

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
}
