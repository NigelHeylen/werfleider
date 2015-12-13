package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.ScrollView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import com.rengwuxian.materialedittext.MaterialEditText;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import org.joda.time.DateTime;

/**
 * Created by nigel on 07/02/15.
 */
public class WerfCreateView extends ScrollView{

    @Bind(R.id.werf_create_naam) MaterialEditText naam;
    @Bind(R.id.werf_create_nummer) MaterialEditText nummer;

    @Bind(R.id.werf_create_opdracht_adres) MaterialEditText opdrachtAdres;
    @Bind(R.id.werf_create_opdracht_stad) MaterialEditText opdrachtStad;

    @Bind(R.id.werf_create_ontwerper) MaterialEditText ontwerper;
    @Bind(R.id.werf_create_ontwerper_adres) MaterialEditText ontwerperAdres;
    @Bind(R.id.werf_create_ontwerper_stad) MaterialEditText ontwerperStad;

    @Bind(R.id.werf_create_opdrachtgever) MaterialEditText opdrachtgever;
    @Bind(R.id.werf_create_opdrachtgever_adres) MaterialEditText opdrachtgeverAdres;
    @Bind(R.id.werf_create_opdrachtgever_stad) MaterialEditText opdrachtgeverStad;

    @Bind(R.id.werf_create_omschrijving) MaterialEditText omschrijving;
    @Bind(R.id.werf_create_datum_aanvang) DatePicker datumAanvang;


    @OnClick(R.id.werf_create_save)
    public void save(){
        presenter.create(
                naam.getText().toString(),
                nummer.getText().toString(),
                opdrachtAdres.getText().toString(),
                opdrachtStad.getText().toString(),
                ontwerper.getText().toString(),
                ontwerperAdres.getText().toString(),
                ontwerperStad.getText().toString(),
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
        ButterKnife.bind(this);
        presenter.takeView(this);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.unbind(this);
    }
}
