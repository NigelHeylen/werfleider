package nigel.com.werfleider.model;

import com.google.common.base.Strings;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import nigel.com.werfleider.util.ParseStringUtils;

import static nigel.com.werfleider.util.ParseStringUtils.COMPANY;
import static nigel.com.werfleider.util.ParseStringUtils.EMAIL;
import static nigel.com.werfleider.util.ParseStringUtils.ID;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.PROFESSION;

/**
 * Created by nigel on 11/02/15.
 */
@ParseClassName("Contact")
public class Contact extends ParseObject {

    public String getNaam() {
        return getString(NAME);
    }

    public Contact setNaam(final String naam) {
        put(NAME, naam);
        return this;
    }

    public String getEmail() {
        return getString(EMAIL);
    }

    public Contact setEmail(final String email) {
        put(ParseStringUtils.EMAIL, email);
        return this;
    }

    public String getBedrijf() {
        return getString(COMPANY);
    }

    public Contact setBedrijf(final String bedrijf) {
        put(COMPANY, Strings.nullToEmpty(bedrijf));
        return this;
    }
    public String getId() {
        return getString(ID);
    }

    public Contact setId(final String id) {
        put(ID, id);
        return this;
    }

    public Contact setProfession(final String profession){
        put(PROFESSION, profession);
        return this;
    }

    public String getProfession() {
        return getString(PROFESSION);
    }
}
