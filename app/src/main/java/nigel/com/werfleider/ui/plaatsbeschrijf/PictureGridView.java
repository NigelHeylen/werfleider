package nigel.com.werfleider.ui.plaatsbeschrijf;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.PlaatsBeschrijfImage;

/**
 * Created by nigel on 03/12/14.
 */
public class PictureGridView extends LinearLayout {

    @Inject PictureGridScreen.Presenter presenter;

    @Inject Picasso pablo;

    private RecyclerView.LayoutManager manager;

    private PictureGridAdapter adapter;

    @InjectView(R.id.picture_grid) RecyclerView grid;

    @InjectView(R.id.picture_grid_location) EditText location;

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

    public void initAdapter(final List<PlaatsBeschrijfImage> images, final List<Integer> indices, final List<PlaatsBeschrijfImage> imageList) {
        manager = new GridLayoutManager(getContext(), 3);
        adapter = new PictureGridAdapter(images, indices, pablo, imageList);

        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);
    }

    public void setLocation(final String location) {
        this.location.setText(location);
    }
}
