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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Inject;
import nigel.com.werfleider.android.StartActivityForResultPresenter;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.util.ImageUtils;

/**
 * Created by nigel on 26/12/15.
 */
public class LocationDetailCameraPresenter extends ReactiveViewPresenter<LocationDetailCameraView>
    implements StartActivityForResultPresenter.ActivityResultListener {

  @Inject DocumentImageAdapterData adapterData;

  @Inject ParseDocumentLocation location;

  @Inject StartActivityForResultPresenter startActivityForResultPresenter;

  private String mCurrentPhotoPath;

  static final int REQUEST_IMAGE_CAPTURE = 1;

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
            Intent takePictureIntent =
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getView().getContext().getPackageManager())
                != null) {
              // Create the File where the photo should go
              File photoFile = null;
              try {
                photoFile = createImageFile();
              } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
              }
              // Continue only if the File was successfully created
              if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResultPresenter.setConfig(
                    new StartActivityForResultPresenter.Config(takePictureIntent,
                        REQUEST_IMAGE_CAPTURE));
              }
            }
          } else {

            Toast.makeText(getView().getContext(), "Please grant the permission", Toast.LENGTH_LONG)
                .show();
          }
        });
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */);

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
    return image;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {

      reactToImageCreated(mCurrentPhotoPath);
    }
  }

  public void reactToImageCreated(final String path) {

    final ParseDocumentImage image = new ParseDocumentImage();
    image.setImageURL(path)
        .setImageTakenDate(new Date())
        .setAuthor(ParseUser.getCurrentUser())
        .setLocationId(location);

    image.pinInBackground(e -> {

      if (e == null) {
        adapterData.add(image);
      } else {
        e.printStackTrace();
      }
    });

    image.saveEventually(e -> {
      if (e == null) {
        final ParseFile parseFile = ImageUtils.getParseFile(path);
        parseFile.saveInBackground((ParseException ex) -> {

          if (ex == null) {
            image.setImage(parseFile);
            image.saveEventually();
          }
        });
      }
    });
  }
}
