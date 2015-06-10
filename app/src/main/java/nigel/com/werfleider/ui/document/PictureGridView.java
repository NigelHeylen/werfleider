package nigel.com.werfleider.ui.document;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.ui.widget.HeaderViewRecyclerAdapter;

/**
 * Created by nigel on 03/12/14.
 */
public class PictureGridView extends RelativeLayout {

    @Inject PictureGridScreen.Presenter presenter;

    @Inject Picasso pablo;

    @InjectView(R.id.picture_grid) RecyclerView grid;

    private MaterialEditText location;

    @OnClick(R.id.picture_grid_save)
    public void save(){
        presenter.handleSave();
    }

    public PictureGridView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        presenter.takeView(this);

        location.addTextChangedListener(
                provideTextChangedListener());

        location.setOnFocusChangeListener(
                provideFocusChangeListener());
    }

    private OnFocusChangeListener provideFocusChangeListener() {
        return new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                }

            }
        };
    }

    private TextWatcher provideTextChangedListener() {
        return new TextWatcher() {
            @Override public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override public void afterTextChanged(final Editable s) {
                presenter.editLocation(s.toString());
            }
        };
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(location.getWindowToken(), 0);
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
        ButterKnife.reset(this);
    }

    public void initAdapter(final List<DocumentImage> images, final List<Integer> indices, final List<DocumentImage> imageList) {

        grid.setLayoutManager(createManager());

        final HeaderViewRecyclerAdapter adapter = new HeaderViewRecyclerAdapter(new PictureGridAdapter(images, indices, pablo, imageList));

        final View title = LayoutInflater.from(getContext()).inflate(R.layout.picture_grid_title_item, grid, false);

        location = ButterKnife.findById(title, R.id.picture_grid_title);

        adapter.addHeaderView(title);

        grid.setAdapter(adapter);
    }

    private GridLayoutManager createManager() {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 3);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                      @Override public int getSpanSize(final int position) {

                                          return position == 0 ? 3 : 1;
                                      }
                                  });
        return manager;
    }

    public void setLocation(final String location) {
        this.location.setText(location);
    }
}
