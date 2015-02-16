package nigel.com.werfleider.model;

import org.joda.time.DateTime;

/**
 * Created by nigel on 11/02/15.
 */
public class Contact {
    private String naam;
    private String email;
    private String bedrijf;
    private Profession profession;
    private DateTime createdAt;
    private int id;

    public Contact() {
    }

    public Contact(final String naam, final String email, final String bedrijf, final Profession profession) {
        this.naam = naam;
        this.email = email;
        this.bedrijf = bedrijf;
        this.profession = profession;
    }

    public String getNaam() {
        return naam;
    }

    public Contact setNaam(final String naam) {
        this.naam = naam;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Contact setEmail(final String email) {
        this.email = email;
        return this;
    }

    public String getBedrijf() {
        return bedrijf;
    }

    public Contact setBedrijf(final String bedrijf) {
        this.bedrijf = bedrijf;
        return this;
    }

    public Profession getProfession() {
        return profession;
    }

    public Contact setProfession(final Profession profession) {
        this.profession = profession;
        return this;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Contact setCreatedAt(final DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int getId() {
        return id;
    }

    public Contact setId(final int id) {
        this.id = id;
        return this;
    }
}
