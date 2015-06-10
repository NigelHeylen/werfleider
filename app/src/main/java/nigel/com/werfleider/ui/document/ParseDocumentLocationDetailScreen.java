package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManagerEx;
import android.support.v7.widget.OrientationHelperEx;
import android.support.v7.widget.RecyclerViewEx;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.android.ActionBarOwner;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.CreateImage;
import nigel.com.werfleider.model.ParseDocument;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.ParseDocumentLocation;
import nigel.com.werfleider.model.ParseYard;
import nigel.com.werfleider.model.StartCameraEvent;
import nigel.com.werfleider.util.ImageUtils;
import nigel.com.werfleider.util.MeasuringUnit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;

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
    public static class Presenter extends ViewPresenter<ParseDocumentLocationDetailView> {

        @Inject ParseDocument document;

        @Inject ParseDocumentLocation location;

        @Inject Picasso pablo;

        @Inject ParseYard yard;

        @Inject ActionBarOwner actionBarOwner;

        @Inject Bus bus;

        @Inject List<ParseDocumentImage> imageList;

        @Inject Flow flow;

        private int position;

        private ParseDocumentImageListItemAdapter adapter;

        private LinearLayoutManagerEx layoutManager;

        @Inject
        public Presenter(final Bus bus) {

            bus.register(this);
        }

        @Override protected void onLoad(final Bundle savedInstanceState) {

            super.onLoad(savedInstanceState);

            if (getView() == null) {
                return;
            }

            initActionBar();


            initView();

            getImages();
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
                                position = layoutManager.findFirstCompletelyVisibleItemPosition();
                                initPagerDetailViews();
                            }
                        }

                        @Override public void onScrolled(final RecyclerViewEx recyclerViewEx, final int i, final int i1) {

                        }
                    });
        }

        private void getImages() {

            final ParseQuery<ParseDocumentImage> query = ParseQuery.getQuery(ParseDocumentImage.class);
            query.whereEqualTo(
                    LOCATION_ID,
                    location);

            query.findInBackground(
                    new FindCallback<ParseDocumentImage>() {
                        @Override public void done(final List<ParseDocumentImage> list, final ParseException e) {

                            if (e == null) {
                                imageList.clear();

                                Collections.sort(list);
                                imageList.addAll(list);
                                initAdapter();
                                initPagerDetailViews();
                                getView().detailTabs.setViewPager(getView().detailViews);

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

        @Subscribe
        public void reactToImageCreated(final CreateImage createImage) {

            final ParseFile parseFile = ImageUtils.getParseFile(createImage.getCurrentUri());
            parseFile.saveInBackground(
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {
                                System.out.println("Presenter.done");
                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

            final ParseDocumentImage image = new ParseDocumentImage();
            image.setImageURL(createImage.getCurrentUri())
                 .setImage(parseFile)
                 .setImageTakenDate(new Date())
                 .setAuthor(ParseUser.getCurrentUser())
                 .setLocationId(location);

            imageList.add(image);

            adapter.notifyDataSetChanged();

        }


        public void startCameraEvent() {

            bus.post(new StartCameraEvent(0));
        }

        public void handleSave() {

            ParseObject.saveAllInBackground(
                    imageList,
                    new SaveCallback() {
                        @Override public void done(final ParseException e) {

                            if (e == null) {
                                Toast.makeText(
                                        getView().getContext(),
                                        format(
                                                "%s saved",
                                                location.getTitle()),
                                        Toast.LENGTH_LONG).show();

                            } else {
                                e.printStackTrace();
                            }

                            getView().progressBar.setVisibility(GONE);
                        }
                    });

        }

        public void initAdapter() {

            getView().imageList.setAdapter(
                    adapter = new ParseDocumentImageListItemAdapter(
                            getView().getContext(),
                            getView().progressBar));

            getView().detailViews.setOffscreenPageLimit(5);
            getView().detailViews.setAdapter(
                    new PagerAdapter() {
                        @Override public int getCount() {

                            return document.getDocumentType() == OPMETINGEN ? 3 : 2;
                        }

                        @Override public boolean isViewFromObject(final View view, final Object object) {

                            return view == object;
                        }

                        @Override public Object instantiateItem(final ViewGroup container, final int position) {

                            return container.getChildAt(position);
                        }

                        @Override public CharSequence getPageTitle(final int position) {

                            switch (position) {
                                case 0:
                                    return "Tekst";
                                case 1:
                                    return "Info";
                                case 2:
                                    return "Afmetingen";
                                default:
                                    return "PIET";
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

            final ParseDocumentImage image = getCurrentImage();

            getView().width.setText(Double.toString(image.getWidth()));
            getView().height.setText(Double.toString(image.getHeight()));
            getView().length.setText(Double.toString(image.getLength()));

            getView().description.setText(image.getDescription());
            getView().floor.setText(image.getFloor());
            getView().location.setText(image.getLocation());

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

            getCurrentImage().setWidth(Double.parseDouble(width));
        }

        public void changeHeight(final String height) {

            getCurrentImage().setHeight(Double.parseDouble(height));
        }

        public void changeLength(final String length) {

            getCurrentImage().setLength(Double.parseDouble(length));
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
    }
}