package nigel.com.werfleider.ui.werf;

import android.content.Context;
import android.widget.Toast;
import com.parse.ParseUser;
import flow.Flow;
import flow.HasParent;
import flow.Layout;
import javax.inject.Inject;
import mortar.Blueprint;
import mortar.ViewPresenter;
import nigel.com.werfleider.R;
import nigel.com.werfleider.core.CorePresenter;
import nigel.com.werfleider.model.Yard;
import org.joda.time.DateTime;

import static nigel.com.werfleider.util.ParseStringUtils.NAME;

/**
 * Created by nigel on 07/02/15.
 */
@Layout(R.layout.werf_create_view)
public class WerfCreateScreen implements Blueprint, HasParent<YardsOverviewScreen> {

    @Override public String getMortarScopeName() {
        return getClass().getName();
    }

    @Override public Object getDaggerModule() {
        return new Module();
    }

    @Override public YardsOverviewScreen getParent() {
        return new YardsOverviewScreen();
    }

    @dagger.Module(
            injects = WerfCreateView.class,
            addsTo =
                    CorePresenter.Module.class

    )
    static class Module {

    }

    static class Presenter extends ViewPresenter<WerfCreateView> {

        @Inject Flow flow;

        @Inject Context context;

        public void create(
                final String naam,
                final String nummer,
                final String opdrachtAdres,
                final String opdrachtStad,
                final String ontwerper,
                final String ontwerperAdres,
                final String ontwerperStad,
                final String opdrachtgever,
                final String opdrachtgeverAdres,
                final String opdrachtgeverStad,
                final String omschrijving,
                final DateTime datumAanvang) {

            final Yard yard = new Yard();

            yard.setNaam(naam)
                    .setNummer(nummer)
                    .setOpdrachtAdres(opdrachtAdres)
                    .setOpdrachtStad(opdrachtStad)
                    .setOntwerper(ontwerper)
                    .setOntwerperAdres(ontwerperAdres)
                    .setOntwerperStad(ontwerperStad)
                    .setOpdrachtgever(opdrachtgever)
                    .setOpdrachtgeverAdres(opdrachtgeverAdres)
                    .setOpdrachtgeverStad(opdrachtgeverStad)
                    .setOmschrijving(omschrijving)
                    .setDatumAanvang(datumAanvang)
                    .setCreator(ParseUser.getCurrentUser().getString(NAME))
                    .setAuthor(ParseUser.getCurrentUser());

            yard.pinInBackground(e -> {

                if (e == null) {
                    Toast.makeText(
                            context,
                            "Werf " + naam + " saved.",
                            Toast.LENGTH_LONG).show();
                    flow.goTo(new YardListScreen());

                }
            });

            yard.saveEventually();

        }
    }
}
