package nigel.com.werfleider.ui.contacts;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.dao.contact.ContactDbHelper;
import nigel.com.werfleider.model.Profession;
import nigel.com.werfleider.ui.home.HomeScreen;
import nigel.com.werfleider.ui.profession.ProfessionListScreen;

import static java.lang.String.format;
import static nigel.com.werfleider.model.Profession.ARCHITECT;
import static nigel.com.werfleider.model.Profession.BOUWMEESTER;

/**
 * Created by nigel on 11/02/15.
 */
@Layout(R.layout.contacts_view)
public class ContactScreen implements Blueprint, HasParent<HomeScreen> {

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

    @Override public HomeScreen getParent() {
        return new HomeScreen();
    }

    @dagger.Module(
            injects = ContactView.class,
            addsTo = CorePresenter.Module.class
    )
    static class Module {

    }

    static class Presenter extends ViewPresenter<ContactView> implements SurfaceHolder.Callback {

        @Inject Flow flow;

        @Inject ContactDbHelper contactDbHelper;

        private Camera camera;

        SurfaceHolder surfaceHolder;

        Camera.PictureCallback rawCallback;
        Camera.ShutterCallback shutterCallback;
        Camera.PictureCallback jpegCallback;

        @Override protected void onLoad(final Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);

            getView().architectCount.setText(getCountFormat(contactDbHelper.getContactCount(ARCHITECT)));
            getView().bouwmeesterCount.setText(getCountFormat(contactDbHelper.getContactCount(BOUWMEESTER)));

            surfaceHolder = getView().surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            jpegCallback = new Camera.PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    FileOutputStream outStream = null;
                    System.out.println("Presenter.onPictureTaken");
                    try {
                        outStream = new FileOutputStream(String.format("%s/%d.jpg",
                                                                       Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis()));

                        outStream.write(data);
                        outStream.close();

                        Toast.makeText(getView().getContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                        refreshCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };



        }

        public void captureImage(View v) throws IOException {
            camera.takePicture(null, null, jpegCallback);
        }

        public void refreshCamera() {
            if (surfaceHolder.getSurface() == null) {
                return;
            }

            try {
                camera.stopPreview();
            }

            catch (Exception e) {
            }

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            }
            catch (Exception e) {
            }
        }


        private String getCountFormat(final int contactCount) {
            return format("%d contacts", contactCount);
        }

        public void goToProfessionDetailView(final Profession profession) {
            flow.goTo(new ProfessionListScreen(profession));
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open();
            }

            catch (RuntimeException e) {
                System.err.println(e);
                return;
            }

            Camera.Parameters param;
            param = camera.getParameters();
            param.setPreviewSize(352, 288);
            camera.setParameters(param);
            camera.setDisplayOrientation(90);

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            }

            catch (Exception e) {
                System.err.println(e);
                return;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            refreshCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        public void takePhoto() {
            camera.takePicture(
                    null,
                    null,
                    jpegCallback);

        }
    }
}
