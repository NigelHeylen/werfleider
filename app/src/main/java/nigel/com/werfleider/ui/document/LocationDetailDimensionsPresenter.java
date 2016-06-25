package nigel.com.werfleider.ui.document;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import nigel.com.werfleider.model.ParseDocumentImage;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.ui.presenter.ReactiveViewPresenter;
import nigel.com.werfleider.util.MeasuringUnit;
import rx.subjects.BehaviorSubject;

import static android.view.View.VISIBLE;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.util.StringUtils.emptyToOne;

/**
 * Created by nigel on 26/12/15.
 */
@Singleton public class LocationDetailDimensionsPresenter
    extends ReactiveViewPresenter<LocationDetailDimensionsView> {

  @Inject DocumentImageAdapterData imageList;

  @Inject DocumentLocation location;

  @Inject BehaviorSubject<ParseDocumentImage> documentImageBus;

  private ParseDocumentImage documentImage;

  @Override protected void onLoad(Bundle savedInstanceState) {
    super.onLoad(savedInstanceState);
    if (getView() == null) return;

    initView();

    this.documentImage = documentImageBus.getValue();
    if(documentImage != null){

      bindDocumentImage(documentImage);
    }

    subscribe(documentImageBus.subscribe(this::bindDocumentImage));
  }

  private void initView() {

    getView().measuringUnits.setAdapter(
        new ArrayAdapter<>(getView().getContext(), android.R.layout.simple_dropdown_item_1line,
            getMeasuringUnitsAsString()));

    getView().measuringUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
          final long id) {

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

  private void showOrHideEditTexts(final DocumentLocation location) {

    for (MaterialEditText editText : getView().editTexts) {
      editText.setVisibility(View.INVISIBLE);
    }

    for (int i = 0; i <= location.getMeasuringUnit().getWeight(); i++) {
      getView().editTexts.get(i).setVisibility(VISIBLE);
    }
  }

  public List<MeasuringUnit> getMeasuringUnits() {

    return newArrayList(MeasuringUnit.values());
  }

  @NonNull private List<String> getMeasuringUnitsAsString() {

    return newArrayList(transform(getMeasuringUnits(), input -> input.name().toLowerCase()));
  }

  private void bindDocumentImage(final ParseDocumentImage documentImage) {

    this.documentImage = documentImage;
    if(getView() == null) return;

    getView().height.setText(Double.toString(documentImage.getHeight()));
    getView().width.setText(Double.toString(documentImage.getWidth()));
    getView().length.setText(Double.toString(documentImage.getLength()));
    getView().quantity.setText(Integer.toString(documentImage.getQuantity()));
  }

  public void changeWidth(final String width) {

    documentImage.setWidth(Double.parseDouble(emptyToOne(width)));
  }

  public void changeHeight(final String height) {

    documentImage.setHeight(Double.parseDouble(emptyToOne(height)));
  }

  public void changeLength(final String length) {

    documentImage.setLength(Double.parseDouble(emptyToOne(length)));
  }

  public void changeQuantity(final String quantity) {

    documentImage.setQuantity(Integer.parseInt(emptyToOne(quantity)));
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

    return filter(imageList.getList(), input -> {

      return input.getLocation().trim().equals(documentImage.getLocation().trim());
    });
  }
}
