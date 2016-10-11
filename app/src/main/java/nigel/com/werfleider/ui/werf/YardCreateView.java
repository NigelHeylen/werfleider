package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.net.Uri;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gc.materialdesign.views.ButtonRectangle;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions.RxPermissions;
import flow.Flow;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.util.ImageUtils;
import org.joda.time.DateTime;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static java.lang.String.format;

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

  @Bind(R.id.werf_choose_image) ButtonRectangle chooseImage;

  @Bind(R.id.werf_create_number_floors) TextView numberFloors;
  @Bind(R.id.werf_create_number_locations) TextView numberLocations;

  @Inject Flow flow;

  private CompositeSubscription subscription = new CompositeSubscription();

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
    subscription.unsubscribe();
  }

  public void setData(Yard yard) {

    if (!Objects.equals(yard.getCreator(), ParseUser.getCurrentUser().getEmail())) {
      naam.setInputType(InputType.TYPE_NULL);
      nummer.setInputType(InputType.TYPE_NULL);
      adres.setInputType(InputType.TYPE_NULL);
      huisNummer.setInputType(InputType.TYPE_NULL);
      stad.setInputType(InputType.TYPE_NULL);
      postcode.setInputType(InputType.TYPE_NULL);
      omschrijving.setInputType(InputType.TYPE_NULL);
      aanvang.setEnabled(false);
      termijn.setInputType(InputType.TYPE_NULL);
      architect.setInputType(InputType.TYPE_NULL);
      architectTelefoon.setInputType(InputType.TYPE_NULL);
      architectEmail.setInputType(InputType.TYPE_NULL);
      bouwheer.setInputType(InputType.TYPE_NULL);
      bouwheerTelefoon.setInputType(InputType.TYPE_NULL);
      bouwheerEmail.setInputType(InputType.TYPE_NULL);
      ingenieur.setInputType(InputType.TYPE_NULL);
      ingenieurTelefoon.setInputType(InputType.TYPE_NULL);
      ingenieurEmail.setInputType(InputType.TYPE_NULL);
      chooseImage.setVisibility(GONE);
    }

    naam.setText(yard.getNaam());
    nummer.setText(yard.getNummer());
    adres.setText(yard.getYardAddress());
    huisNummer.setText(yard.getYardAddressNumber());
    stad.setText(yard.getYardCity());
    postcode.setText(yard.getYardAreaCode());
    omschrijving.setText(yard.getOmschrijving());
    aanvang.init(yard.getDatumAanvang().getYear(), yard.getDatumAanvang().getMonthOfYear(),
        yard.getDatumAanvang().getDayOfMonth(),
        (view, year, monthOfYear, dayOfMonth) -> yard.setDatumAanvang(
            new DateTime(year, monthOfYear + 1, dayOfMonth, 1, 1)));
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

    numberFloors.setText(format("Aantal verdiepingen: %d", yard.getFloors().size()));
    numberLocations.setText(format("Aantal lokalen: %d", yard.getLocations().size()));

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

    subscribeToTextChangeEvent(naam, yard::setNaam);
    subscribeToTextChangeEvent(nummer, yard::setNummer);
    subscribeToTextChangeEvent(adres, yard::setYardAdress);
    subscribeToTextChangeEvent(huisNummer, yard::setYardAddressNumber);
    subscribeToTextChangeEvent(stad, yard::setYardCity);
    subscribeToTextChangeEvent(postcode, yard::setYardAreaCode);
    subscribeToTextChangeEvent(omschrijving, yard::setOmschrijving);
    subscribeToTextChangeEvent(termijn, yard::setTermijn);

    subscribeToTextChangeEvent(bouwheer, yard::setBouwHeerNaam);
    subscribeToTextChangeEvent(bouwheerEmail, yard::setBouwheerEmail);
    subscribeToTextChangeEvent(bouwheerTelefoon, yard::setBouwheerTelefoon);

    subscribeToTextChangeEvent(ingenieur, yard::setIngenieurNaam);
    subscribeToTextChangeEvent(ingenieurTelefoon, yard::setIngenieurTelefoon);
    subscribeToTextChangeEvent(ingenieurEmail, yard::setIngenieurEmail);

    subscribeToTextChangeEvent(architect, yard::setArchitectNaam);
    subscribeToTextChangeEvent(architectTelefoon, yard::setArchitectTelefoon);
    subscribeToTextChangeEvent(architectEmail, yard::setArchitectEmail);
  }

  private void subscribeToTextChangeEvent(EditText editText, Action1<String> action) {
    subscription.add(
        RxTextView.textChanges(editText).skip(1).map(CharSequence::toString).subscribe(action));
  }

  @OnClick(R.id.werf_choose_image) public void chooseImage() {

    RxPermissions.getInstance(getContext())
        .request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        .subscribe(granted -> {
          if (granted) {
            presenter.getImage();
          }
        });
  }

  @OnClick(R.id.werf_create_locations) public void chooseLocations(){

    presenter.handleGoToLocationScreen();
  }

  @OnClick(R.id.werf_create_floors) public void chooseFloors(){

    presenter.handleGoToFloorScreen();
  }

  public void setImage(Uri imageUri) {

    pablo.load(imageUri).resize(werfImage.getWidth(), 0).into(werfImage);
  }
}
