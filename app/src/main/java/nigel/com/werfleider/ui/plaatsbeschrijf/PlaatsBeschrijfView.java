package nigel.com.werfleider.ui.plaatsbeschrijf;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import flow.Flow;
import mortar.Mortar;
import nigel.com.werfleider.R;
import nigel.com.werfleider.model.PlaatsBeschrijf;

/**
 * Created by nigel on 25/11/14.
 */
public class PlaatsBeschrijfView extends RelativeLayout {

    @Inject PlaatsBeschrijfScreen.Presenter presenter;

    @Inject Picasso pablo;

    @Inject Bus bus;

    @Inject Flow flow;

    private PdfViewHolder holder;

    public PlaatsBeschrijfView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        Mortar.inject(context, this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        holder = new PdfViewHolder(this, presenter);
        presenter.takeView(this);

        initLocationButton();

    }

    private void initLocationButton() {
//        holder.locationButton.setOnClickListener(
//                new OnClickListener() {
//                    @Override public void onClick(final View v) {
//                        presenter.goToLocationList();
//                    }
//                });
    }



    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.dropView(this);
    }

    public void showToast(final String message) {

        Toast.makeText(
                getContext(),
                message, Toast.LENGTH_SHORT)
             .show();
    }

    public void showPdfText(final String text) {

    }

    public void showLocationNumber(final String number) {
//        holder.locationNumber.setText(number);
    }

    public void initView(final PlaatsBeschrijf plaatsBeschrijf) {
        initTextViews(plaatsBeschrijf);
        initLocations(plaatsBeschrijf);
    }

    private void initLocations(final PlaatsBeschrijf plaatsBeschrijf) {
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        final PlaatsBeschrijfLocationAdapter adapter = new PlaatsBeschrijfLocationAdapter(plaatsBeschrijf, pablo, flow);

        Mortar.inject(getContext(), adapter);

        holder.locations.setLayoutManager(layoutManager);
        holder.locations.setAdapter(adapter);
    }

    private void initTextViews(final PlaatsBeschrijf plaatsBeschrijf) {
        holder.ontwerper.setText(plaatsBeschrijf.getOntwerper());
        holder.ontwerperAdres.setText(plaatsBeschrijf.getOntwerperAdres());
        holder.ontwerperLocatie.setText(plaatsBeschrijf.getOntwerperStad());
        holder.opdrachtgever.setText(plaatsBeschrijf.getOpdrachtgever());
        holder.opdrachtLocatie.setText(plaatsBeschrijf.getOpdrachtLocatie());
    }

    public void savePlaatscbeschrijf() {
        presenter.savePlaatsbeschrijf(
                holder.ontwerper.getText().toString(),
                holder.ontwerperAdres.getText().toString(),
                holder.ontwerperLocatie.getText().toString(),
                holder.opdrachtgever.getText().toString(),
                holder.opdrachtLocatie.getText().toString()
                                     );
    }

    static class PdfViewHolder {

        private final PlaatsBeschrijfScreen.Presenter presenter;

        @InjectView(R.id.plaatsbeschrijf_ontwerper) EditText ontwerper;
        @InjectView(R.id.plaatsbeschrijf_ontwerper_adres) EditText ontwerperAdres;
        @InjectView(R.id.plaatsbeschrijf_ontwerper_locatie) EditText ontwerperLocatie;
        @InjectView(R.id.plaatsbeschrijf_opdracht_locatie) EditText opdrachtLocatie;
        @InjectView(R.id.plaatsbeschrijf_opdrachtgever) EditText opdrachtgever;

        @InjectView(R.id.plaatsbeschrijf_locations) RecyclerView locations;

        @OnClick(R.id.plaatsbeschrijf_floating_button)
        public void newImageCollection() {
            presenter.newImageCollection();
        }


        @OnClick(R.id.btnwrite)
        public void write() {
            presenter.write();
        }

        PdfViewHolder(final View view, final PlaatsBeschrijfScreen.Presenter presenter) {
            this.presenter = presenter;
            ButterKnife.inject(this, view);
        }
    }
}
