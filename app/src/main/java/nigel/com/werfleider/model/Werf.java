package nigel.com.werfleider.model;

import org.joda.time.DateTime;

/**
 * Created by nigel on 31/01/15.
 */
public class Werf {

    private String naam;
    private String nummer;
    private String opdrachtgever;
    private String opdrachtgeverAdres;
    private String opdrachtgeverStad;
    private String omschrijving;

    private DateTime datumAanvang;

    private PlaatsBeschrijf plaatsBeschrijf;

    private DateTime createdAt;
    private int id;

    public Werf(final String naam, final String nummer) {
        this.naam = naam;
        this.nummer = nummer;
        initLists();
    }

    public Werf(final String naam, final String nummer, final String opdrachtgever, final String opdrachtgeverAdres, final String opdrachtgeverStad, final String omschrijving, final DateTime datumAanvang) {

        this.naam = naam;
        this.nummer = nummer;
        this.opdrachtgever = opdrachtgever;
        this.opdrachtgeverAdres = opdrachtgeverAdres;
        this.opdrachtgeverStad = opdrachtgeverStad;
        this.omschrijving = omschrijving;
        this.datumAanvang = datumAanvang;
    }

    private void initLists() {
        plaatsBeschrijf = new PlaatsBeschrijf();
    }

    public Werf() {

    }

    public Werf(final int id, final String naam, final String nummer, final String date) {

        this.id = id;
        this.naam = naam;
        this.nummer = nummer;
        setCreatedAt(date);
        initLists();
    }

    public Werf setNummer(final String nummer) {
        this.nummer = nummer;
        return this;
    }

    public String getNummer() {
        return nummer;
    }

    public Werf setCreatedAt(final String string) {
        this.createdAt = new DateTime(string);
        return this;
    }

    public Werf setId(final int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public Werf setNaam(final String naam) {
        this.naam = naam;
        return this;
    }

    public String getOpdrachtgever() {
        return opdrachtgever;
    }

    public Werf setOpdrachtgever(final String opdrachtgever) {
        this.opdrachtgever = opdrachtgever;
        return this;
    }

    public String getOpdrachtgeverAdres() {
        return opdrachtgeverAdres;
    }

    public Werf setOpdrachtgeverAdres(final String opdrachtgeverAdres) {
        this.opdrachtgeverAdres = opdrachtgeverAdres;
        return this;
    }

    public String getOpdrachtgeverStad() {
        return opdrachtgeverStad;
    }

    public Werf setOpdrachtgeverStad(final String opdrachtgeverStad) {
        this.opdrachtgeverStad = opdrachtgeverStad;
        return this;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public Werf setOmschrijving(final String omschrijving) {
        this.omschrijving = omschrijving;
        return this;
    }

    public DateTime getDatumAanvang() {
        return datumAanvang;
    }

    public Werf setDatumAanvang(final String datumAanvang) {
        this.datumAanvang = new DateTime(datumAanvang);
        return this;
    }

    public PlaatsBeschrijf getPlaatsBeschrijf() {
        return plaatsBeschrijf;
    }

    public Werf setPlaatsBeschrijf(final PlaatsBeschrijf plaatsBeschrijf) {
        this.plaatsBeschrijf = plaatsBeschrijf;
        return this;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Werf setCreatedAt(final DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
