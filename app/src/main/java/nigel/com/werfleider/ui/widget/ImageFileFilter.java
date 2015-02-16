package nigel.com.werfleider.ui.widget;

import java.io.File;

/**
 * Created by nigel on 03/12/14.
 */
public class ImageFileFilter implements java.io.FileFilter {

    private final String[] okFileExtensions =
            new String[]{"jpg", "png", "gif"};

    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}

