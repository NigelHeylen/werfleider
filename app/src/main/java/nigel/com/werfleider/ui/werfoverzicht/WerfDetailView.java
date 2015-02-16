package nigel.com.werfleider.ui.werfoverzicht;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 27/12/14.
 */
public class WerfDetailView extends ScrollView {

    @Inject WerfDetailScreen.Presenter presenter;

    private ViewHolder viewHolder;

    public WerfDetailView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
        viewHolder = new ViewHolder(this, presenter);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'werfoverzicht.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ViewHolder {

        private final WerfDetailScreen.Presenter presenter;

        @InjectView(R.id.Werfoverzicht_Button_Plannen) RelativeLayout mPlannen;
        @InjectView(R.id.Werfoverzicht_Button_Plaatsbeschrijf) LinearLayout mPlaatsbeschrijf;
//        @InjectView(R.id.Werfoverzicht_Button_Opmetingen) RelativeLayout mOpmetingen;
//        @InjectView(R.id.Werfoverzicht_Button_Afspraken) RelativeLayout mAfspraken;
//        @InjectView(R.id.Werfoverzicht_Button_ToDo) RelativeLayout mToDo;
//        @InjectView(R.id.Werfoverzicht_Button_Technische_Fiches) RelativeLayout mFiches;

        @OnClick(R.id.Werfoverzicht_Button_Plaatsbeschrijf)
        public void plaatsBeschrijfClick() {
            presenter.goToPlaatsbeschrijfView();
        }

        @OnClick(R.id.Werfoverzicht_Button_Plannen)
        public void plannenClick() {
            presenter.goToEmail();
        }

//        @OnClick(R.id.Werfoverzicht_Button_Afspraken)
//        public void afsprakenClick() {
//            presenter.goToEmailoverzicht();
//        }

        ViewHolder(final View view, final WerfDetailScreen.Presenter presenter) {
            this.presenter = presenter;
            ButterKnife.inject(this, view);
        }
    }
}
