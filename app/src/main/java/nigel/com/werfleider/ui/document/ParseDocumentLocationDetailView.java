package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.astuetz.PagerSlidingTabStrip;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.List;
import javax.inject.Inject;
import mortar.Mortar;
import nigel.com.werfleider.R;

/**
 * Created by nigel on 17/12/14.
 */
public class ParseDocumentLocationDetailView extends RelativeLayout {

    @Inject ParseDocumentLocationDetailScreen.Presenter presenter;

    @Bind(R.id.document_description) MaterialEditText description;

    @Bind(R.id.document_length) MaterialEditText length;

    @Bind(R.id.document_height) MaterialEditText height;

    @Bind(R.id.document_width) MaterialEditText width;

    @Bind(R.id.document_quantity) MaterialEditText quantity;

    @Bind(R.id.document_ms) MaterialEditText ms;

    @Bind(R.id.document_detail_floor) MaterialEditText floor;

    @Bind(R.id.document_detail_location) MaterialEditText location;

    @Bind(R.id.document_measuring_units) Spinner measuringUnits;

    @Bind(R.id.document_image_list) RecyclerViewPager imageList;

    @Bind(R.id.document_image_detail_tabs) PagerSlidingTabStrip detailTabs;

    @Bind(R.id.document_image_detail_views) ViewPager detailViews;

    @Bind(R.id.document_location_detail_info_container) LinearLayout infoContainer;

    @Bind(R.id.document_location_detail_dimensions_container) RelativeLayout dimensionContainer;

    @Bind(R.id.document_menu_button) FloatingActionsMenu actionsMenu;

    @Bind(R.id.location_saver) ProgressBarCircularIndeterminate progressBar;

    @Bind({ R.id.document_length, R.id.document_width, R.id.document_height})
    List<MaterialEditText> editTexts;

    @Bind(R.id.take_picture_surface) SurfaceView surfaceView;

    @OnClick(R.id.take_photo)
    public void takePhoto() {

        presenter.takePhoto();
    }

    public ParseDocumentLocationDetailView(final Context context, final AttributeSet attrs) {

        super(
            context,
            attrs);
        Mortar.inject(
            context,
            this);
    }

    @Override protected void onFinishInflate() {

        super.onFinishInflate();

        ButterKnife.bind(this);
        presenter.takeView(this);


    }

    @Override protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.document_save)
    public void clickSave() {

        progressBar.setVisibility(VISIBLE);
        presenter.handleSave();
    }

    @OnTextChanged(R.id.document_detail_location)
    public void changeLocation(){

        presenter.changeLocation(location.getText().toString());
    }

    @OnTextChanged(R.id.document_description)
    public void changeDescription(){

        presenter.changeDescription(description.getText().toString());
    }

    @OnTextChanged(R.id.document_detail_floor)
    public void changeFloor(){

        presenter.changeFloor(floor.getText().toString());
    }

    @OnTextChanged(R.id.document_width)
    public void changeWidth(){

        System.out.println("width.getText().toString() = " + width.getText().toString());

        presenter.changeWidth(width.getText().toString());
    }

    @OnTextChanged(R.id.document_height)
    public void changeHeight(){

        presenter.changeHeight(height.getText().toString());
    }

    @OnTextChanged(R.id.document_length)
    public void changeLength(){

        presenter.changeLength(length.getText().toString());
    }

    @OnTextChanged(R.id.document_quantity)
    public void changeQuantity(){

        presenter.changeQuantity(quantity.getText().toString());
    }

    @OnTextChanged(R.id.document_ms)
    public void changeMS(){

        presenter.changeMS(ms.getText().toString());
    }

    @OnClick(R.id.document_edit)
    public void clickEdit() {

        presenter.handleEdit();
    }

}
