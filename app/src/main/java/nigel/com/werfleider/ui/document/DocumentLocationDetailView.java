package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.model.StartCameraEvent;
import nigel.com.werfleider.model.WerfInt;
import nigel.com.werfleider.util.MeasuringUnit;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.model.DocumentType.OPMETINGEN;

/**
 * Created by nigel on 17/12/14.
 */
public class DocumentLocationDetailView extends RelativeLayout {

    @Inject DocumentLocationDetailScreen.Presenter presenter;

    @Inject Bus bus;

    @Inject Flow flow;

    @Inject Picasso pablo;

    @Inject WerfInt werf;

    private ViewHolder holder;

    public DocumentLocationDetailView(final Context context, final AttributeSet attrs) {

        super(
                context,
                attrs);
        Mortar.inject(
                context,
                this);
    }

    @Override protected void onFinishInflate() {

        super.onFinishInflate();

        holder = new ViewHolder(this);
        presenter.takeView(this);

    }


    @Override protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void initAdapter(final Document document, final int position) {

        final DocumentLocation location = document.getFotoReeksList().get(position);

        holder
                .mCamera
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override public void onClick(final View v) {

                                bus.post(new StartCameraEvent(position));
                            }
                        });

        holder
                .mEdit
                .setOnClickListener(
                        new View.OnClickListener() {
                            @Override public void onClick(final View v) {

                                flow.goTo(
                                        new PictureGridScreen(
                                                document,
                                                location,
                                                werf));
                            }
                        });

        holder.save
                .setOnClickListener(
                        new OnClickListener() {
                            @Override public void onClick(final View v) {
                                presenter.handleSave();
                            }
                        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        holder.imageList.setLayoutManager(linearLayoutManager);
        holder.imageList.setAdapter(
                new DocumentImageListItemAdapter(
                        location,
                        pablo,
                        holder.title,
                        holder.description,
                        holder.length,
                        holder.width,
                        holder.height));

        final List<String> measuringUnits = newArrayList();

        for (MeasuringUnit measuringUnit : MeasuringUnit.values()) {
            measuringUnits.add(measuringUnit.name().toLowerCase());
        }

        if (document.getDocumentType() == OPMETINGEN) {
            holder.measuringUnits.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, measuringUnits));

            holder.measuringUnits.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                            location.setMeasuringUnit(MeasuringUnit.values()[position]);

                            showOrHideEditTexts(location);
                        }

                        @Override public void onNothingSelected(final AdapterView<?> parent) {

                        }
                    });

            holder.measuringUnits.setVisibility(VISIBLE);

            if (location.getMeasuringUnit() == null) {
                location.setMeasuringUnit(MeasuringUnit.MM);

            }

            holder.measuringUnits.setSelection(getMeasuringUnits().indexOf(location.getMeasuringUnit()));

            showOrHideEditTexts(location);
        }

    }

    public List<MeasuringUnit> getMeasuringUnits(){
        return Arrays.asList(MeasuringUnit.values());
    }

    private void showOrHideEditTexts(final DocumentLocation location) {
        for (MaterialEditText editText : holder.editTexts) {
            editText.setVisibility(INVISIBLE);
        }

        for (int i = 0; i <= location.getMeasuringUnit().getWeight(); i++) {
            holder.editTexts.get(i).setVisibility(VISIBLE);

        }
    }


    static class ViewHolder {

        @InjectView(R.id.document_camera) FloatingActionButton mCamera;
        @InjectView(R.id.document_edit) FloatingActionButton mEdit;
        @InjectView(R.id.document_save) FloatingActionButton save;

        @InjectView(R.id.document_item_title) MaterialEditText title;

        @InjectView(R.id.document_description) MaterialEditText description;

        @InjectView(R.id.document_length) MaterialEditText length;
        @InjectView(R.id.document_height) MaterialEditText height;
        @InjectView(R.id.document_width) MaterialEditText width;

        @InjectView(R.id.document_measuring_units) Spinner measuringUnits;

        @InjectView(R.id.document_image_list) RecyclerView imageList;

        @InjectViews({R.id.document_length, R.id.document_width, R.id.document_height})
        List<MaterialEditText> editTexts;

        public ViewHolder(final View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }
}
