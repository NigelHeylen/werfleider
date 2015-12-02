package nigel.com.werfleider.ui.document;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManagerEx;
import android.support.v7.widget.OrientationHelperEx;
import android.support.v7.widget.RecyclerViewEx;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.commons.load.Load;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.util.ImageUtils;
import nigel.com.werfleider.util.MeasuringUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static nigel.com.werfleider.commons.load.Load.LOCAL;
import static nigel.com.werfleider.commons.load.Load.NETWORK;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;
import static nigel.com.werfleider.util.StringUtils.emptyToOne;

/**
 * Created by nigel on 17/04/15.
 */
@Layout(R.layout.parsedocument_location_detail_view)
public class ParseDocumentLocationDetailScreen implements Blueprint, HasParent<ParseDocumentScreen> {

    private final ParseDocument document;

    private final ParseYard yard;

    private final ParseDocumentLocation location;

    public ParseDocumentLocationDetailScreen(final ParseDocument document, final ParseYard yard, final ParseDocumentLocation location) {

        this.document = document;
        this.yard = yard;
        this.location = location;
    }

    @Override public String getMortarScopeName() {

        return format(
                "%s document: yard id %s, %s, location: %s",
                getClass().getName(),
                yard.getObjectId(),
                document.getDocumentType().toString(),
                location.getTitle());
    }

    @Override public ParseDocumentScreen getParent() {

        return new ParseDocumentScreen(
                yard,
                document);
    }

    @Override public Object getDaggerModule() {

        return new Module(
                document,
                yard,
                location);
    }

    @dagger.Module(
            injects = {
                    ParseDocumentLocationDetailView.class,
                    ParseDocumentImageListItemAdapter.class
            },
            addsTo = CorePresenter.Module.class
    )
    static class Module {

        private final ParseDocument document;

        private final ParseYard yard;

        private final ParseDocumentLocation location;

        Module(final ParseDocument document, final ParseYard yard, final ParseDocumentLocation location) {


            this.document = document;
            this.location = location;
            this.yard = yard;
        }

        @Provides @Singleton ParseYard provideYard() {

            return yard;
        }

        @Provides @Singleton ParseDocument provideDocument() {

            return document;
        }

        @Provides @Singleton ParseDocumentLocation provideLocation() {

            return location;
        }

        @Provides @Singleton List<ParseDocumentImage> provideImageList() {

            return newArrayList();
        }

    }

    @Singleton
    public static class Presenter extends ViewPresenter<ParseDocumentLocationDetailView> implements SurfaceHolder.Callback {

        @Inject ParseDocument document;

        @Inject ParseDocumentLocation location;

        @Inject Picasso pablo;

        @Inject ParseYard yard;

        @Inject ActionBarOwner actionBarOwner;

        @Inject List<ParseDocumentImage> imageList;

        @Inject Flow flow;

        private int position;

        private ParseDocumentImageListItemAdapter adapter;

        private LinearLayoutManagerEx layoutManager;

        private Camera camera;

        SurfaceHolder surfaceHolder;

        Camera.PictureCallback rawCallback;

        Camera.ShutterCallback shutterCallback;

        Camera.PictureCallback jpegCallback;

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);

            if (getView() == null) {
                return;
            }

            initActionBar();


            initView();

            if (imageList.isEmpty()) {
                getImages(LOCAL);
            }

            initSurfaceHolder();
        }

        private void initView() {

            getView().imageList.setLayoutManager(
                    layoutManager = new LinearLayoutManagerEx(
                            getView().getContext(),
                            OrientationHelperEx.HORIZONTAL,
                            false));

            getView().imageList.setOnScrollListener(
                    new RecyclerViewEx.OnScrollListener() {
                        @Override public void onScrollStateChanged(final RecyclerViewEx recyclerViewEx, final int i) {

                            if (i == RecyclerViewEx.SCROLL_STATE_IDLE) {
                                if (position != layoutManager.findFirstCompletelyVisibleItemPosition()) {
                                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
                                    initPagerDetailViews();
                                }
                            }
                        }

                        @Override public void onScrolled(final RecyclerViewEx recyclerViewEx, final int i, final int i1) {

                        }
                    });
        }

        private void getImages(final Load load) {

            final ParseQuery<ParseDocumentImage>
                query = ParseQuery.getQuery(ParseDocumentImage.class);

            if(load == LOCAL){

                query.fromLocalDatastore();
            }

            query.whereEqualTo(
                    LOCATION_ID,
                    location)
                 .findInBackground(
                         new FindCallback<ParseDocumentImage>() {
                             @Override public void done(final List<ParseDocumentImage> list, final ParseException e) {

                                 if (e == null) {

                                     for (ParseDocumentImage image : list) {

                                         if (!imageList.contains(image)) {

                                             imageList.add(image);
                                         }
                                     }

                                     Collections.sort(imageList);


                                     if (load == LOCAL) {

                                         initAdapter();
                                         initPagerDetailViews();
                                         getView().detailTabs.setViewPager(getView().detailViews);
                                         getImages(NETWORK);

                                     }

                                     adapter.notifyDataSetChanged();

                                 } else {
                                     e.printStackTrace();
                                 }
                                 if (getView() != null) {
                                     getView().progressBar.setVisibility(GONE);
                                 }
                             }
                         });
        }

        private void initActionBar() {


            actionBarOwner.setConfig(
                    new ActionBarOwner.Config(
                            false,
                            true,
                            isNullOrEmpty(location.getTitle()) ? "Location" : location.getTitle(),
                            null));
        }


        public void reactToImageCreated(final File file) {

            final ParseDocumentImage image = new ParseDocumentImage();
            image.setImageURL(file.getAbsolutePath())
                 .setImageTakenDate(new Date())
                 .setAuthor(ParseUser.getCurrentUser())
                 .setLocationId(location);

            image.pinInBackground(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {
                                imageList.add(image);

                                adapter.notifyDataSetChanged();

                                getView().imageList.smoothScrollToPosition(imageList.size());

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

            image.saveEventually(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {
                            if(e == null){
                                final ParseFile
                                    parseFile = ImageUtils.getParseFile(file.getAbsolutePath());
                                parseFile.saveInBackground(
                                        new SaveCallback() {
                                            @Override public void done(final ParseException e) {

                                                image.setImage(parseFile);
                                                image.saveEventually();
                                            }
                                        });

                            }
                        }
                    });

        }


        public void handleSave() {

            for (ParseDocumentImage parseDocumentImage : imageList) {
                parseDocumentImage.saveEventually();
            }

            getView().progressBar.setVisibility(GONE);

        }


        public void initAdapter() {

            getView().imageList.setAdapter(
                    adapter = new ParseDocumentImageListItemAdapter(
                            getView().getContext(),
                            getView().progressBar));

            getView().detailViews.setOffscreenPageLimit(10);
            getView().detailViews.setAdapter(
                    new PagerAdapter() {
                        @Override public int getCount() {

                            return document.getDocumentType() == OPMETINGEN ? 4 : 3;
                        }

                        @Override public boolean isViewFromObject(final View view, final Object object) {

                            return view == object;
                        }

                        @Override public Object instantiateItem(final ViewGroup container, final int position) {

                            return container.getChildAt(document.getDocumentType() != OPMETINGEN && position == 2 ? 3 : position);
                        }

                        @Override public CharSequence getPageTitle(final int position) {

                            switch (position) {
                                case 0:
                                    return "Tekst";
                                case 1:
                                    return "Info";
                                case 2:
                                    return document.getDocumentType() == OPMETINGEN ? "Afmetingen" : "Camera";
                                case 3:
                                    return "Camera";
                                default:
                                    return "";
                            }
                        }

                        @Override public void destroyItem(final ViewGroup container, final int position, final Object object) {

                            container.removeViewAt(position);
                        }
                    });

            final List<String> measuringUnits = newArrayList();

            for (MeasuringUnit measuringUnit : MeasuringUnit.values()) {
                measuringUnits.add(measuringUnit.name().toLowerCase());
            }

            getView().measuringUnits.setAdapter(
                    new ArrayAdapter<>(
                            getView().getContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            measuringUnits));

            getView().measuringUnits.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {

                            location.setMeasuringUnit(MeasuringUnit.values()[position]);

                            showOrHideEditTexts(location);
                        }

                        @Override public void onNothingSelected(final AdapterView<?> parent) {

                        }
                    });

            getView().measuringUnits.setVisibility(VISIBLE);

            if (location.getMeasuringUnit() == null) {
                location.setMeasuringUnit(MeasuringUnit.MM);

            }

            getView().measuringUnits.setSelection(getMeasuringUnits().indexOf(location.getMeasuringUnit()));

            showOrHideEditTexts(location);

        }

        private void initPagerDetailViews() {

            if (imageList.size() != 0) {
                final ParseDocumentImage image = getCurrentImage();

                getView().height.setText(Double.toString(image.getHeight()));
                getView().length.setText(Double.toString(image.getLength()));
                getView().width.setText(Double.toString(image.getWidth()));
                getView().quantity.setText(Integer.toString(image.getQuantity()));
                getView().ms.setText(image.getMS());

                getView().description.setText(image.getDescription());
                getView().floor.setText(image.getFloor());
                getView().location.setText(image.getLocation());
            }

        }

        public List<MeasuringUnit> getMeasuringUnits() {

            return Arrays.asList(MeasuringUnit.values());
        }

        private void showOrHideEditTexts(final ParseDocumentLocation location) {

            for (MaterialEditText editText : getView().editTexts) {
                editText.setVisibility(View.INVISIBLE);
            }

            for (int i = 0; i <= location.getMeasuringUnit().getWeight(); i++) {
                getView().editTexts.get(i).setVisibility(VISIBLE);

            }
        }

        public void changeLocation(final String locationString) {

            getCurrentImage().setLocation(locationString);
        }

        public void changeFloor(final String floor) {

            getCurrentImage().setFloor(floor);
        }

        public void changeWidth(final String width) {

            getCurrentImage().setWidth(Double.parseDouble(emptyToOne(width)));
        }

        public void changeHeight(final String height) {

            getCurrentImage().setHeight(Double.parseDouble(emptyToOne(height)));
        }

        public void changeLength(final String length) {

            getCurrentImage().setLength(Double.parseDouble(emptyToOne(length)));
        }

        private ParseDocumentImage getCurrentImage() {

            return imageList.get(position);
        }


        public void changeDescription(final String description) {

            getCurrentImage().setDescription(description);

        }

        public void handleEdit() {

            flow.goTo(
                    new ParsePictureGridScreen(
                            document,
                            location,
                            yard));
        }

        public void changeQuantity(final String quantity) {

            getCurrentImage().setQuantity(Integer.parseInt(emptyToOne(quantity)));
        }

        public void changeMS(final String ms) {

            changeAllMSStringsPerFloor(ms);
        }

        private void changeAllMSStringsPerFloor(final String ms) {

            for (ParseDocumentImage documentImage : filterOnLocation()) {

                documentImage.setMS(ms);
            }
        }

        private Iterable<ParseDocumentImage> filterOnLocation() {

            return Iterables.filter(imageList, new Predicate<ParseDocumentImage>() {
                    @Override public boolean apply(final ParseDocumentImage input) {

                        return input.getLocation()
                            .trim()
                            .equals(getCurrentImage().getLocation().trim());
                    }
                });
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

            try {
                camera = Camera.open();
            } catch (RuntimeException e) {
                System.err.println(e);
                return;
            }

            Camera.Parameters param;
            param = camera.getParameters();
//            param.setPreviewSize(
//                    352,
//                    288);
            param.setRotation(90);
            camera.setParameters(param);
            camera.setDisplayOrientation(90);

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
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

        public void refreshCamera() {

            if (surfaceHolder.getSurface() == null) {
                return;
            }

            try {
                camera.stopPreview();
            } catch (Exception e) {
            }

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (Exception e) {
            }
        }


        private void initSurfaceHolder() {

            surfaceHolder = getView().surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            jpegCallback = new Camera.PictureCallback() {

                @Override
                public void onPictureTaken(byte[] data, Camera camera) {

                    try {
                        final String path = format(
                                "%s/%d.jpg",
                                Environment.getExternalStorageDirectory().getPath(),
                                System.currentTimeMillis());

                        FileOutputStream fos = new FileOutputStream(path);

                        fos.write(data);
                        fos.close();

                        final File file = new File(path);

                        reactToImageCreated(file);

                        refreshCamera();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };
        }
    }
}