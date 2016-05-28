package nigel.com.werfleider.ui.werf;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContentResolverCompat;
import com.parse.ParseUser;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.StartActivityForResultPresenter;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Yard;
import nigel.com.werfleider.util.ImageUtils;
import org.joda.time.DateTime;

import static nigel.com.werfleider.util.ParseStringUtils.NAME;

/**
 * Created by nigel on 07/02/15.
 */
@Layout(R.layout.yard_create_view) public class YardCreateScreen
    implements Blueprint, HasParent<YardsOverviewScreen> {

  private final Yard yard;

  public YardCreateScreen(Yard yard) {

    this.yard = yard;
  }

  public YardCreateScreen() {
    yard = null;
  }

  @Override public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override public Object getDaggerModule() {
    return new Module(yard);
  }

  @Override public YardsOverviewScreen getParent() {
    return new YardsOverviewScreen();
  }

  @dagger.Module(
      injects = YardCreateView.class,
      addsTo = CorePresenter.Module.class

  ) static class Module {

    private final Yard werf;

    public Module(Yard werf) {
      this.werf = werf;
    }

    @Provides Yard provideWerf() {
      return werf;
    }
  }

  static class Presenter extends ViewPresenter<YardCreateView>
      implements StartActivityForResultPresenter.ActivityResultListener {

    private static final int SELECT_PHOTO = 100;
    @Inject Flow flow;

    @Inject Yard yard;

    @Inject StartActivityForResultPresenter resultPresenter;
    private byte[] imageByteArray = new byte[]{};

    @Override protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      if (getView() != null) {

        getView().setData(yard);

        resultPresenter.setActivityResultListener(this);

        if(yard != null && yard.getImageByteArray() != null){
          imageByteArray = yard.getImageByteArray();
        }
      }
    }

    @Override protected void onExitScope() {
      super.onExitScope();
      resultPresenter.removeListener();
    }

    public void create(String naam, String nummer, String adres, String huisNummer, String stad,
        String postcode, String omschrijving, DateTime aanvang, String termijn, String architect,
        String architectTelefoon, String architectEmail, String bouwheer, String bouwheerTelefoon,
        String bouwheerEmail, String ingenieur, String ingenieurTelefoon, String ingenieurEmail) {
      Yard yard;

      if (this.yard != null) {
        yard = this.yard;
      } else {
        yard = new Yard();
      }

      yard.setNaam(naam)
          .setNummer(nummer)
          .setYardAdress(adres)
          .setYardAddressNumber(huisNummer)
          .setYardCity(stad)
          .setYardAreaCode(postcode)
          .setOmschrijving(omschrijving)
          .setDatumAanvang(aanvang)
          .setTermijn(termijn)
          .setArchitectNaam(architect)
          .setArchitectTelefoon(architectTelefoon)
          .setArchitectEmail(architectEmail)
          .setBouwHeerNaam(bouwheer)
          .setBouwheerTelefoon(bouwheerTelefoon)
          .setBouwheerEmail(bouwheerEmail)
          .setIngenieurNaam(ingenieur)
          .setIngenieurTelefoon(ingenieurTelefoon)
          .setIngenieurEmail(ingenieurEmail)
          .setCreator(ParseUser.getCurrentUser().getString(NAME))
          .setImageByteArray(imageByteArray)
          .setAuthor(ParseUser.getCurrentUser());

      yard.pinInBackground();

      yard.saveEventually();
      flow.goTo(new YardsOverviewScreen());
    }

    public void getImage() {

      Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
      photoPickerIntent.setType("image/*");
      photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
      resultPresenter.setConfig(
          new StartActivityForResultPresenter.Config(photoPickerIntent, SELECT_PHOTO));
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PHOTO) {

        final Uri imageUri = data.getData();

        String[] filePathColumn = {
            MediaStore.Images.Media.DATA, MediaStore.Files.FileColumns.MIME_TYPE
        };

        Cursor cursor =
            ContentResolverCompat.query(getView().getContext().getContentResolver(), imageUri,
                filePathColumn, null, null, null, null);
        cursor.moveToFirst();

        String filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
        cursor.close();

        imageByteArray = ImageUtils.getBytesFromFilePath(filePath);
        if (getView() != null) {
          getView().setImage(imageUri);
        }
      }
    }
  }
}
