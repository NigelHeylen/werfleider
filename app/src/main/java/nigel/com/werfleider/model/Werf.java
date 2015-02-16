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

    public void setNummer(final String nummer) {
        this.nummer = nummer;
    }

    public String getNummer() {
        return nummer;
    }

    public void setCreatedAt(final String string) {
        this.createdAt = new DateTime(string);
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(final String naam) {
        this.naam = naam;
    }

    public String getOpdrachtgever() {
        return opdrachtgever;
    }

    public void setOpdrachtgever(final String opdrachtgever) {
        this.opdrachtgever = opdrachtgever;
    }

    public String getOpdrachtgeverAdres() {
        return opdrachtgeverAdres;
    }

    public void setOpdrachtgeverAdres(final String opdrachtgeverAdres) {
        this.opdrachtgeverAdres = opdrachtgeverAdres;
    }

    public String getOpdrachtgeverStad() {
        return opdrachtgeverStad;
    }

    public void setOpdrachtgeverStad(final String opdrachtgeverStad) {
        this.opdrachtgeverStad = opdrachtgeverStad;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(final String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public DateTime getDatumAanvang() {
        return datumAanvang;
    }

    public void setDatumAanvang(final DateTime datumAanvang) {
        this.datumAanvang = datumAanvang;
    }

    public PlaatsBeschrijf getPlaatsBeschrijf() {
        return plaatsBeschrijf;
    }

    public void setPlaatsBeschrijf(final PlaatsBeschrijf plaatsBeschrijf) {
        this.plaatsBeschrijf = plaatsBeschrijf;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final DateTime createdAt) {
        this.createdAt = createdAt;
    }
}
