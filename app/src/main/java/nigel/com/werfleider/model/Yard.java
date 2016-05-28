package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.List;
import nigel.com.werfleider.util.ParseStringUtils;
import org.joda.time.DateTime;

import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.CONTRACTOR;
import static nigel.com.werfleider.util.ParseStringUtils.CONTRACTOR_PHONE;
import static nigel.com.werfleider.util.ParseStringUtils.CONTRACTOR_EMAIL;
import static nigel.com.werfleider.util.ParseStringUtils.CREATOR;
import static nigel.com.werfleider.util.ParseStringUtils.DATE_START_WORK;
import static nigel.com.werfleider.util.ParseStringUtils.DEADLINE;
import static nigel.com.werfleider.util.ParseStringUtils.DEFINITION;
import static nigel.com.werfleider.util.ParseStringUtils.DESIGNER;
import static nigel.com.werfleider.util.ParseStringUtils.DESIGNER_EMAIL;
import static nigel.com.werfleider.util.ParseStringUtils.DESIGNER_PHONE;
import static nigel.com.werfleider.util.ParseStringUtils.ENGINEER;
import static nigel.com.werfleider.util.ParseStringUtils.ENGINEER_EMAIL;
import static nigel.com.werfleider.util.ParseStringUtils.ENGINEER_PHONE;
import static nigel.com.werfleider.util.ParseStringUtils.INVITES;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.NUMBER;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ADDRESS;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ADDRESS_NUMBER;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_AREA_CODE;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_CITY;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_IMAGE;

/**
 * Created by nigel on 14/04/15.
 */
@ParseClassName("Werf")
public class Yard extends ParseObject {

    public String getNaam() {

        return getString(NAME);
    }

    public Yard setNaam(final String naam) {

        put(NAME, naam);
        return this;
    }

    public String getNummer() {

        return getString(NUMBER);
    }

    public Yard setNummer(final String nummer) {

        put(NUMBER, nummer);
        return this;
    }


    public String getYardAddress() {

        return getString(YARD_ADDRESS);
    }

    public Yard setYardAdress(final String yardAdress) {

        put(YARD_ADDRESS, yardAdress);
        return this;
    }

    public String getYardAddressNumber() {

        return getString(ParseStringUtils.YARD_ADDRESS_NUMBER);
    }

    public Yard setYardAddressNumber(final String yardAddressNumber) {

        put(YARD_ADDRESS_NUMBER, yardAddressNumber);
        return this;
    }

    public String getYardAreaCode() {

        return getString(ParseStringUtils.YARD_AREA_CODE);
    }

    public Yard setYardAreaCode(final String areaCode) {

        put(YARD_AREA_CODE, areaCode);
        return this;
    }

    public String getYardCity() {

        return getString(YARD_CITY);
    }

    public Yard setYardCity(final String yardCity) {

        put(YARD_CITY, yardCity);
        return this;
    }



    public String getOmschrijving() {

        return getString(DEFINITION);
    }

    public Yard setOmschrijving(final String omschrijving) {

        put(DEFINITION, omschrijving);
        return this;
    }

    public DateTime getDatumAanvang() {

        return new DateTime(getString(DATE_START_WORK));
    }

    public Yard setDatumAanvang(final DateTime datumAanvang) {

        put(DATE_START_WORK, datumAanvang.toString());
        return this;
    }


    public String getTermijn() {

        return getString(ParseStringUtils.DEADLINE);
    }

    public Yard setTermijn(final String termijn) {

        put(DEADLINE, termijn);
        return this;
    }

    public byte[] getImageByteArray(){

        return getBytes(YARD_IMAGE);
    }

    public Yard setImageByteArray(final byte[] imageBytes){
        put(YARD_IMAGE, imageBytes);
        return this;
    }


    public String getArchitectNaam() {

        return getString(DESIGNER);
    }

    public Yard setArchitectNaam(final String ontwerper) {

        put(DESIGNER, ontwerper);
        return this;
    }

    public String getArchitectTelefoon() {

        return getString(DESIGNER_PHONE);
    }

    public Yard setArchitectTelefoon(final String telefoon) {

        put(DESIGNER_PHONE, telefoon);
        return this;
    }

    public String getArchitectEmail() {

        return getString(DESIGNER_EMAIL);
    }

    public Yard setArchitectEmail(final String email) {

        put(DESIGNER_EMAIL, email);
        return this;
    }

    public String getBouwheerNaam() {

        return getString(CONTRACTOR);
    }

    public Yard setBouwHeerNaam(final String bouwHeerNaam) {

        put(CONTRACTOR, bouwHeerNaam);
        return this;
    }

    public String getBouwheerTelefoon() {

        return getString(CONTRACTOR_PHONE);
    }

    public Yard setBouwheerTelefoon(final String bouwheerTelefoon) {

        put(CONTRACTOR_PHONE, bouwheerTelefoon);
        return this;
    }

    public String getBouwheerEmail() {

        return getString(CONTRACTOR_EMAIL);
    }

    public Yard setBouwheerEmail(final String email) {

        put(CONTRACTOR_EMAIL, email);
        return this;
    }
    public String getIngenieurNaam() {

        return getString(ENGINEER);
    }

    public Yard setIngenieurNaam(final String naam) {

        put(ENGINEER, naam);
        return this;
    }

    public String getIngenieurTelefoon() {

        return getString(ENGINEER_PHONE);
    }

    public Yard setIngenieurTelefoon(final String bouwheerTelefoon) {

        put(ENGINEER_PHONE, bouwheerTelefoon);
        return this;
    }

    public String getIngenieurEmail() {

        return getString(ENGINEER_EMAIL);
    }

    public Yard setIngenieurEmail(final String email) {

        put(ENGINEER_EMAIL, email);
        return this;
    }


    public ParseUser getAuthor() {
        return getParseUser(AUTHOR);
    }

    public Yard setAuthor(ParseUser user) {
        put(AUTHOR, user);
        return this;
    }

    public Yard setCreator(final String creator) {
        put(CREATOR, creator);
        return this;
    }

    public String getId() {

        return getObjectId();
    }

    public List<ParseUser> getInvites() {
        return getList(INVITES);
    }
}
