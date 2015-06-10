package nigel.com.werfleider.model;

import com.parse.ParseUser;

import org.joda.time.DateTime;

/**
 * Created by nigel on 31/01/15.
 */
public class Werf implements WerfInt{

    private String naam;
    private String nummer;

    private String opdrachtAdres;
    private String opdrachtStad;

    private String ontwerper;
    private String ontwerperStad;
    private String ontwerperAdres;

    private String opdrachtgever;
    private String opdrachtgeverAdres;
    private String opdrachtgeverStad;

    private String omschrijving;

    private DateTime datumAanvang;

    private Document document;

    private DateTime createdAt;
    private int id;

    public Werf(
            final String naam,
            final String nummer,
            final String opdrachtAdres,
            final String opdrachtStad,
            final String ontwerper,
            final String ontwerperStad,
            final String ontwerperAdres,
            final String opdrachtgever,
            final String opdrachtgeverAdres,
            final String opdrachtgeverStad,
            final String omschrijving,
            final DateTime datumAanvang) {

        this.naam = naam;
        this.nummer = nummer;
        this.opdrachtAdres = opdrachtAdres;
        this.opdrachtStad = opdrachtStad;
        this.ontwerperStad = ontwerperStad;
        this.ontwerperAdres = ontwerperAdres;
        this.opdrachtgever = opdrachtgever;
        this.ontwerper = ontwerper;
        this.opdrachtgeverAdres = opdrachtgeverAdres;
        this.opdrachtgeverStad = opdrachtgeverStad;
        this.omschrijving = omschrijving;
        this.datumAanvang = datumAanvang;
    }


    public Werf() {

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

    public String getId() {
        return String.valueOf(id);
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

    @Override public ParseUser getAuthor() {

        return null;
    }

    @Override public WerfInt setAuthor(final ParseUser user) {

        return null;
    }

    @Override public WerfInt setCreator(final String creator) {

        return null;
    }

    public Werf setDatumAanvang(final DateTime datumAanvang) {
        this.datumAanvang = new DateTime(datumAanvang);
        return this;
    }

    public Document getDocument() {
        return document;
    }

    public Werf setDocument(final Document document) {
        this.document = document;
        return this;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Werf setCreatedAt(final DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getOpdrachtAdres() {
        return opdrachtAdres;
    }

    public String getOpdrachtStad() {
        return opdrachtStad;
    }

    public String getOntwerper() {
        return ontwerper;
    }

    public String getOntwerperStad() {
        return ontwerperStad;
    }

    public String getOntwerperAdres() {
        return ontwerperAdres;
    }

    public Werf setOpdrachtAdres(final String opdrachtAdres) {
        this.opdrachtAdres = opdrachtAdres;
        return this;
    }

    public Werf setOpdrachtStad(final String opdrachtStad) {
        this.opdrachtStad = opdrachtStad;
        return this;
    }

    public Werf setOntwerper(final String ontwerper) {
        this.ontwerper = ontwerper;
        return this;
    }

    public Werf setOntwerperStad(final String ontwerperStad) {
        this.ontwerperStad = ontwerperStad;
        return this;
    }

    public Werf setOntwerperAdres(final String ontwerperAdres) {
        this.ontwerperAdres = ontwerperAdres;
        return this;
    }

    @Override public String toString() {
        return "Werf{" +
                "naam='" + naam + '\'' +
                ", nummer='" + nummer + '\'' +
                ", opdrachtAdres='" + opdrachtAdres + '\'' +
                ", opdrachtStad='" + opdrachtStad + '\'' +
                ", ontwerper='" + ontwerper + '\'' +
                ", ontwerperStad='" + ontwerperStad + '\'' +
                ", ontwerperAdres='" + ontwerperAdres + '\'' +
                ", opdrachtgever='" + opdrachtgever + '\'' +
                ", opdrachtgeverAdres='" + opdrachtgeverAdres + '\'' +
                ", opdrachtgeverStad='" + opdrachtgeverStad + '\'' +
                ", omschrijving='" + omschrijving + '\'' +
                ", datumAanvang=" + datumAanvang +
                ", plaatsBeschrijf=" + document +
                ", createdAt=" + createdAt +
                ", id=" + id +
                '}';
    }
}
