package nigel.com.werfleider.ui.contacts;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by nigel on 17/06/15.
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {

    private Camera theCamera;

    public Preview(final Context context) {

        super(context);
    }

    public void surfaceCreated(SurfaceHolder holder) {

        try {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
    }
}
