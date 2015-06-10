package nigel.com.werfleider.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.ParseFile;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by nigel on 02/05/15.
 */
public class ImageUtils {

    public static ParseFile getParseFile(final String imageUrl) {

        final File imageFile = new File(imageUrl);

        final Bitmap bitmap = ImageUtils.decodeFile(imageFile);

        return new ParseFile(
                StringUtils.substringAfterLast(imageUrl, "/"), getBytesFromBitmap(bitmap));
    }


    // convert from bitmap to byte array
    public static byte[] getBytesFromBitmap(final Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100,
                stream);

        return stream.toByteArray();
    }

    //decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(final File f) {

        try {
            //Decode image size
            final BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(
                    new FileInputStream(f),
                    null,
                    o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 300;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(
                    new FileInputStream(f),
                    null,
                    o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
