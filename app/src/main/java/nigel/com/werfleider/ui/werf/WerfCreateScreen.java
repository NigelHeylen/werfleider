package nigel.com.werfleider.ui.werf;

import android.os.Bundle;
import com.parse.ParseUser;
import dagger.Provides;
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
@Layout(R.layout.werf_create_view) public class WerfCreateScreen
    implements Blueprint, HasParent<YardsOverviewScreen> {

  private final Yard werf;

  public WerfCreateScreen(Yard werf) {

    this.werf = werf;
  }

  public WerfCreateScreen() {
    werf = null;
  }

  @Override public String getMortarScopeName() {
    return getClass().getName();
  }

  @Override public Object getDaggerModule() {
    return new Module(werf);
  }

  @Override public YardsOverviewScreen getParent() {
    return new YardsOverviewScreen();
  }

  @dagger.Module(
      injects = WerfCreateView.class,
      addsTo = CorePresenter.Module.class

  ) static class Module {

    private final Yard werf;

    public Module(Yard werf) {
      this.werf = werf;
    }

    @Provides Yard provideWerf() {
      return werf;
    }
  }

  static class Presenter extends ViewPresenter<WerfCreateView> {

    @Inject Flow flow;

    @Inject Yard yard;

    @Override protected void onLoad(Bundle savedInstanceState) {
      super.onLoad(savedInstanceState);
      if(getView() != null){

        getView().setData(yard);
      }
    }

    public void create(final String naam, final String nummer, final String opdrachtAdres,
        final String opdrachtStad, final String ontwerper, final String ontwerperAdres,
        final String ontwerperStad, final String opdrachtgever, final String opdrachtgeverAdres,
        final String opdrachtgeverStad, final String omschrijving, final DateTime datumAanvang) {

      Yard yard;

      if(this.yard != null){
        yard = this.yard;
      } else {
        yard = new Yard();
      }

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

      yard.pinInBackground();

      yard.saveEventually(e -> {

        if (e == null) {
          flow.goTo(new YardsOverviewScreen());
        }
      });
    }
  }
}
