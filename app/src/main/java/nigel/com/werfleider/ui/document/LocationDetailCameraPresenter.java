package nigel.com.werfleider.ui.document;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;
import nigel.com.werfleider.android.StartActivityForResultPresenter;
import nigel.com.werfleider.commons.parse.ParseErrorHandler;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.util.ImageUtils;

/**
 * Created by nigel on 26/12/15.
 */
@Singleton public class LocationDetailCameraPresenter extends ReactiveViewPresenter<LocationDetailCameraView>
    implements StartActivityForResultPresenter.ActivityResultListener {

  @Inject DocumentImageAdapterData adapterData;

  @Inject DocumentLocation location;

  @Inject StartActivityForResultPresenter startActivityForResultPresenter;

  @Inject ParseErrorHandler parseErrorHandler;

  private Uri fileUri;

  static final int REQUEST_IMAGE_CAPTURE = 500;

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    startActivityForResultPresenter.setActivityResultListener(this);
  }

  public void openCamera() {

    RxPermissions.getInstance(getView().getContext())
        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribe(granted -> {

          if (granted) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "img_" + timeStamp + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
            fileUri = Uri.fromFile(photo);
            startActivityForResultPresenter.setConfig(
                new StartActivityForResultPresenter.Config(intent, REQUEST_IMAGE_CAPTURE));
          } else {

            Toast.makeText(getView().getContext(), "Please grant the permission", Toast.LENGTH_LONG)
                .show();
          }
        });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

      reactToImageCreated(fileUri.getPath());
    }
  }

  public void reactToImageCreated(final String path) {

    final ParseDocumentImage image = new ParseDocumentImage();
    image.setImageURL(path)
        .setImageTakenDate(new Date())
        .setCreator(ParseUser.getCurrentUser().getEmail())
        .setImageBytes(ImageUtils.getBytesFromFilePath(path))
        .setLocationId(location);

    image.pinInBackground();

    image.saveEventually(e -> {
      if (e == null) {
        final ParseFile parseFile = ImageUtils.getParseFile(path);
        parseFile.saveInBackground((ParseException ex) -> {

          if (ex == null) {
            image.setImage(parseFile);
            image.saveEventually(e1 -> {
              if(e1 != null) parseErrorHandler.handleParseError(e1);
            });
          } else {
            parseErrorHandler.handleParseError(ex);
          }
        });
      }
    });

    adapterData.add(image);
  }

  @Override protected void onExitScope() {
    super.onExitScope();
    startActivityForResultPresenter.removeListener();
  }
}
