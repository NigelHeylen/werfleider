package nigel.com.werfleider.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.parse.ParseFile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import nigel.com.werfleider.model.Yard;

import static nigel.com.werfleider.util.StringUtils.substringAfterLast;

/**
 * Created by nigel on 02/05/15.
 */
public class ImageUtils {

  public static ParseFile getParseFile(final String imageUrl) {

    final File imageFile = new File(imageUrl);

    final Bitmap bitmap = ImageUtils.decodeFile(imageFile);

    return new ParseFile(substringAfterLast(imageUrl, "/"), getBytesFromBitmap(bitmap));
  }

  public static byte[] getBytesFromFilePath(final String imageUrl) {

    System.out.println("imageUrl = " + imageUrl);
    final File imageFile = new File(imageUrl);

    final Bitmap bitmap = ImageUtils.decodeFile(imageFile);

    System.out.println("bitmap = " + bitmap);
    return getBytesFromBitmap(bitmap);
  }

  // convert from bitmap to byte array
  public static byte[] getBytesFromBitmap(final Bitmap bitmap) {

    int startCompress = 100;

    byte[] bytes;

    do{

      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG, startCompress, stream);

      bytes = stream.toByteArray();
      if(startCompress > 90) {
        startCompress -= 5;
      } else if(startCompress > 80){

        startCompress -= 3;
      } else {
        startCompress -= 2;
      }

    }while(bytes.length > 110000);

    return bytes;
  }

  //decodes image and scales it to reduce memory consumption
  public static Bitmap decodeFile(final File f) {

    try {
      //Decode image size
      final BitmapFactory.Options o = new BitmapFactory.Options();
      o.inJustDecodeBounds = true;
      BitmapFactory.decodeStream(new FileInputStream(f), null, o);

      //The new size we want to scale to
      final int REQUIRED_SIZE = 300;

      //Find the correct scale value. It should be the power of 2.
      int scale = 1;
      while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
        scale *= 2;
      }

      //Decode with inSampleSize
      final BitmapFactory.Options o2 = new BitmapFactory.Options();
      o2.inSampleSize = scale;
      return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String getOnDiskUrl(final String url) {
    return String.format("file://%s", url);
  }

  public static File getFileFromImageByteArray(Yard yard, Context context) throws IOException {

    File f = new File(context.getCacheDir(), "werf_image" + yard.getImageByteArray().length);
    f.createNewFile();
    FileOutputStream fos = new FileOutputStream(f);
    fos.write(yard.getImageByteArray());
    fos.flush();
    fos.close();
    return f;
  }
}
