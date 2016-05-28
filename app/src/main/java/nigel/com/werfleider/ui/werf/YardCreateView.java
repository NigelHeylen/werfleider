package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.util.ImageUtils;
import org.joda.time.DateTime;

/**
 * Created by nigel on 07/02/15.
 */
public class YardCreateView extends ScrollView {

  @Bind(R.id.werf_create_naam) MaterialEditText naam;
  @Bind(R.id.werf_create_nummer) MaterialEditText nummer;
  @Bind(R.id.werf_create_opdracht_adres) MaterialEditText adres;
  @Bind(R.id.werf_create_opdracht_adres_nummer) MaterialEditText huisNummer;
  @Bind(R.id.werf_create_opdracht_stad) MaterialEditText stad;
  @Bind(R.id.werf_create_opdracht_postcode) MaterialEditText postcode;
  @Bind(R.id.werf_create_omschrijving) MaterialEditText omschrijving;
  @Bind(R.id.werf_create_datum_aanvang) DatePicker aanvang;
  @Bind(R.id.werf_create_termijn) MaterialEditText termijn;
  @Bind(R.id.werf_image) ImageView werfImage;

  @Bind(R.id.werf_create_architect) MaterialEditText architect;
  @Bind(R.id.werf_create_architect_telefoon) MaterialEditText architectTelefoon;

  @Bind(R.id.werf_create_architect_email) MaterialEditText architectEmail;
  @Bind(R.id.werf_create_bouwheer) MaterialEditText bouwheer;
  @Bind(R.id.werf_create_bouwheer_telefoon) MaterialEditText bouwheerTelefoon;

  @Bind(R.id.werf_create_bouwheer_email) MaterialEditText bouwheerEmail;
  @Bind(R.id.werf_create_ingenieur) MaterialEditText ingenieur;
  @Bind(R.id.werf_create_ingenieur_telefoon) MaterialEditText ingenieurTelefoon;

  @Bind(R.id.werf_create_ingenieur_email) MaterialEditText ingenieurEmail;

  @Bind(R.id.werf_create_save) ButtonRectangle save;

  @OnClick(R.id.werf_create_save) public void save() {
    presenter.create(getString(naam), getString(nummer), getString(adres), getString(huisNummer),
        getString(stad), getString(postcode), getString(omschrijving),
        new DateTime(aanvang.getYear(), aanvang.getMonth(), aanvang.getDayOfMonth(), 1, 1),
        getString(termijn), getString(architect), getString(architectTelefoon),
        getString(architectEmail), getString(bouwheer), getString(bouwheerTelefoon),
        getString(bouwheerEmail), getString(ingenieur), getString(ingenieurTelefoon),
        getString(ingenieurEmail));
  }

  @NonNull private String getString(EditText editText) {
    return editText.getText().toString();
  }

  @Inject Picasso pablo;

  @Inject YardCreateScreen.Presenter presenter;

  public YardCreateView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    if (!isInEditMode()) Mortar.inject(context, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (!isInEditMode()) {
      ButterKnife.bind(this);
      presenter.takeView(this);
    }
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    presenter.dropView(this);
    ButterKnife.unbind(this);
  }

  public void setData(Yard yard) {
    if (yard != null) {
      naam.setText(yard.getNaam());
      nummer.setText(yard.getNummer());
      adres.setText(yard.getYardAddress());
      huisNummer.setText(yard.getYardAddressNumber());
      stad.setText(yard.getYardCity());
      postcode.setText(yard.getYardAreaCode());
      omschrijving.setText(yard.getOmschrijving());
      aanvang.updateDate(yard.getDatumAanvang().getYear(), yard.getDatumAanvang().getMonthOfYear(),
          yard.getDatumAanvang().getDayOfMonth());
      termijn.setText(yard.getTermijn());

      architect.setText(yard.getArchitectNaam());
      architectTelefoon.setText(yard.getArchitectTelefoon());
      architectEmail.setText(yard.getArchitectEmail());

      bouwheer.setText(yard.getBouwheerNaam());
      bouwheerTelefoon.setText(yard.getBouwheerTelefoon());
      bouwheerEmail.setText(yard.getBouwheerEmail());

      ingenieur.setText(yard.getIngenieurNaam());
      ingenieurTelefoon.setText(yard.getIngenieurTelefoon());
      ingenieurEmail.setText(yard.getIngenieurEmail());

      if (yard.getImageByteArray() != null) {

        werfImage.post(() -> {

          try {
            File f = ImageUtils.getFileFromImageByteArray(yard, getContext());
            pablo.load(f).resize(werfImage.getWidth(), 0).into(werfImage);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      }
    }
  }

  @OnClick(R.id.werf_choose_image) public void chooseImage() {

    presenter.getImage();
  }

  public void setImage(Uri imageUri) {

    pablo.load(imageUri).resize(werfImage.getWidth(), 0).into(werfImage);
  }
}
