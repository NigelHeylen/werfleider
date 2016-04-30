package nigel.com.werfleider.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nigel on 02/01/16.
 */
public class AudioUtils {

  public static byte[] getByteArrayFromPath(final String path){
    File file = new File(path);
    int size = (int) file.length();
    byte[] bytes = new byte[size];
    try {
      BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
      buf.read(bytes, 0, bytes.length);
      buf.close();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return bytes;
  }

  public static void saveByteArrayToFile(String fileName, final byte[] soundBytes) {
    File path = new File(fileName);
    FileOutputStream fos;
    try {
      fos = new FileOutputStream(path);
      fos.write(soundBytes);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
