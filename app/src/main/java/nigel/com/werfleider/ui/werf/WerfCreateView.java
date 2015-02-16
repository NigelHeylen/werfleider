package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.ScrollView;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 07/02/15.
 */
public class WerfCreateView extends ScrollView{

    @InjectView(R.id.werf_create_naam) MaterialEditText naam;
    @InjectView(R.id.werf_create_nummer) MaterialEditText nummer;
    @InjectView(R.id.werf_create_opdrachtgever) MaterialEditText opdrachtgever;
    @InjectView(R.id.werf_create_opdrachtgever_adres) MaterialEditText opdrachtgeverAdres;
    @InjectView(R.id.werf_create_opdrachtgever_stad) MaterialEditText opdrachtgeverStad;
    @InjectView(R.id.werf_create_omschrijving) MaterialEditText omschrijving;
    @InjectView(R.id.werf_create_datum_aanvang) DatePicker datumAanvang;


    @OnClick(R.id.werf_create_save)
    public void save(){
        presenter.create(
                naam.getText().toString(),
                nummer.getText().toString(),
                opdrachtgever.getText().toString(),
                opdrachtgeverAdres.getText().toString(),
                opdrachtgeverStad.getText().toString(),
                omschrijving.getText().toString(),
                new DateTime(datumAanvang.getYear(), datumAanvang.getMonth(), datumAanvang.getDayOfMonth(), 1, 1));
    }

    @Inject WerfCreateScreen.Presenter presenter;

    public WerfCreateView(final Context context, final AttributeSet attrs) {
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
}
