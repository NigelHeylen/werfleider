package nigel.com.werfleider.model;

import com.parse.ParseUser;

import org.joda.time.DateTime;

/**
 * Created by nigel on 16/04/15.
 */
public interface WerfInt {

    String getNaam();

    WerfInt setNaam(String naam);

    String getNummer();

    WerfInt setNummer(String nummer);

    String getOpdrachtAdres();

    WerfInt setOpdrachtAdres(String opdrachtAdres);

    String getOpdrachtStad();

    WerfInt setOpdrachtStad(String opdrachtStad);

    String getOntwerper();

    WerfInt setOntwerper(String ontwerper);

    String getOntwerperStad();

    WerfInt setOntwerperStad(String ontwerperStad);

    String getOntwerperAdres();

    WerfInt setOntwerperAdres(String ontwerperAdres);

    String getOpdrachtgever();

    WerfInt setOpdrachtgever(String opdrachtgever);

    String getOpdrachtgeverAdres();

    WerfInt setOpdrachtgeverAdres(String opdrachtgeverAdres);

    String getOpdrachtgeverStad();

    WerfInt setOpdrachtgeverStad(String opdrachtgeverStad);

    String getOmschrijving();

    WerfInt setOmschrijving(String omschrijving);

    DateTime getDatumAanvang();

    WerfInt setDatumAanvang(DateTime datumAanvang);

    ParseUser getAuthor();

    WerfInt setAuthor(ParseUser user);

    WerfInt setCreator(String creator);

    String getId();
}
