package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.joda.time.DateTime;

import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.CONTRACTOR;
import static nigel.com.werfleider.util.ParseStringUtils.CONTRACTOR_ADDRESS;
import static nigel.com.werfleider.util.ParseStringUtils.CONTRACTOR_CITY;
import static nigel.com.werfleider.util.ParseStringUtils.CREATOR;
import static nigel.com.werfleider.util.ParseStringUtils.DATE_START_WORK;
import static nigel.com.werfleider.util.ParseStringUtils.DEFINITION;
import static nigel.com.werfleider.util.ParseStringUtils.DESIGNER;
import static nigel.com.werfleider.util.ParseStringUtils.DESIGNER_ADDRESS;
import static nigel.com.werfleider.util.ParseStringUtils.DESIGNER_CITY;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.NUMBER;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ADDRESS;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_CITY;

/**
 * Created by nigel on 14/04/15.
 */
@ParseClassName("Werf")
public class ParseYard extends ParseObject implements WerfInt{

    @Override public String getNaam() {

        return getString(NAME);
    }

    @Override public ParseYard setNaam(final String naam) {

        put(NAME, naam);
        return this;
    }

    @Override public String getNummer() {

        return getString(NUMBER);
    }

    @Override public ParseYard setNummer(final String nummer) {

        put(NUMBER, nummer);
        return this;
    }

    @Override public String getOpdrachtAdres() {

        return getString(YARD_ADDRESS);
    }

    @Override public ParseYard setOpdrachtAdres(final String opdrachtAdres) {

        put(YARD_ADDRESS, opdrachtAdres);
        return this;
    }

    @Override public String getOpdrachtStad() {

        return getString(YARD_CITY);
    }

    @Override public ParseYard setOpdrachtStad(final String opdrachtStad) {

        put(YARD_CITY, opdrachtStad);
        return this;
    }

    @Override public String getOntwerper() {

        return getString(DESIGNER);
    }

    @Override public ParseYard setOntwerper(final String ontwerper) {

        put(DESIGNER, ontwerper);
        return this;
    }

    @Override public String getOntwerperStad() {

        return getString(DESIGNER_CITY);
    }

    @Override public ParseYard setOntwerperStad(final String ontwerperStad) {

        put(DESIGNER_CITY, ontwerperStad);
        return this;
    }

    @Override public String getOntwerperAdres() {

        return getString(DESIGNER_ADDRESS);
    }

    @Override public ParseYard setOntwerperAdres(final String ontwerperAdres) {

        put(DESIGNER_ADDRESS, ontwerperAdres);
        return this;
    }

    @Override public String getOpdrachtgever() {

        return getString(CONTRACTOR);
    }

    @Override public ParseYard setOpdrachtgever(final String opdrachtgever) {

        put(CONTRACTOR, opdrachtgever);
        return this;
    }

    @Override public String getOpdrachtgeverAdres() {

        return getString(CONTRACTOR_ADDRESS);
    }

    @Override public ParseYard setOpdrachtgeverAdres(final String opdrachtgeverAdres) {

        put(CONTRACTOR_ADDRESS, opdrachtgeverAdres);
        return this;
    }

    @Override public String getOpdrachtgeverStad() {

        return getString(CONTRACTOR_CITY);
    }

    @Override public ParseYard setOpdrachtgeverStad(final String opdrachtgeverStad) {

        put(CONTRACTOR_CITY, opdrachtgeverStad);
        return this;
    }

    @Override public String getOmschrijving() {

        return getString(DEFINITION);
    }

    @Override public ParseYard setOmschrijving(final String omschrijving) {

        put(DEFINITION, omschrijving);
        return this;
    }

    @Override public DateTime getDatumAanvang() {

        return new DateTime(getString(DATE_START_WORK));
    }

    @Override public ParseYard setDatumAanvang(final DateTime datumAanvang) {

        put(DATE_START_WORK, datumAanvang.toString());
        return this;
    }


    @Override public ParseUser getAuthor() {
        return getParseUser(AUTHOR);
    }

    @Override public ParseYard setAuthor(ParseUser user) {
        put(AUTHOR, user);
        return this;
    }

    @Override public ParseYard setCreator(final String creator) {
        put(CREATOR, creator);
        return this;
    }

    @Override public String getId() {

        return getObjectId();
    }
}
