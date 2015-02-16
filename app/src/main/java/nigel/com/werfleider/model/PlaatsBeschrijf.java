package nigel.com.werfleider.model;

import org.joda.time.DateTime;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 31/01/15.
 */
public class PlaatsBeschrijf {

    private int id;
    private int werfId;
    private String opdrachtgever;
    private String opdrachtLocatie;
    private List<PlaatsBeschrijfLocatie> fotoReeksList = newArrayList();
    private String title;
    private String ontwerper;
    private String ontwerperStad;
    private String ontwerperAdres;
    private DateTime createdAt;

    public PlaatsBeschrijf() {
        opdrachtgever = "";
        opdrachtLocatie = "";
        title = "";
        ontwerper = "";
        ontwerperStad = "";
        ontwerperAdres = "";
    }

    public String getOpdrachtgever() {
        return opdrachtgever;
    }

    public PlaatsBeschrijf setOpdrachtgever(final String opdrachtgever) {
        this.opdrachtgever = opdrachtgever;
        return this;
    }

    public String getOpdrachtLocatie() {
        return opdrachtLocatie;
    }

    public PlaatsBeschrijf setOpdrachtLocatie(final String opdrachtLocatie) {
        this.opdrachtLocatie = opdrachtLocatie;
        return this;
    }

    public List<PlaatsBeschrijfLocatie> getFotoReeksList() {
        return fotoReeksList;
    }

    public PlaatsBeschrijf setFotoReeksList(final List<PlaatsBeschrijfLocatie> fotoReeksList) {
        this.fotoReeksList = fotoReeksList;
        return this;
    }

    public void add(final PlaatsBeschrijfLocatie collection) {
        fotoReeksList.add(collection);
    }

    public String getTitle() {
        return title;
    }

    public PlaatsBeschrijf setTitle(final String title) {
        this.title = title;
        return this;
    }

    public String getOntwerper() {
        return ontwerper;
    }

    public PlaatsBeschrijf setOntwerper(final String ontwerper) {

        this.ontwerper = ontwerper;
        return this;
    }

    public String getOntwerperStad() {
        return ontwerperStad;
    }

    public PlaatsBeschrijf setOntwerperStad(final String ontwerperStad) {
        this.ontwerperStad = ontwerperStad;
        return this;
    }

    public String getOntwerperAdres() {
        return ontwerperAdres;
    }

    public PlaatsBeschrijf setOntwerperAdres(final String ontwerperAdres) {
        this.ontwerperAdres = ontwerperAdres;
        return this;
    }

    public int getWerfId() {
        return werfId;
    }

    public PlaatsBeschrijf setWerfId(final int werfId) {
        this.werfId = werfId;
        return this;
    }

    public int getId() {
        return id;
    }

    public PlaatsBeschrijf setId(final int id) {
        this.id = id;
        return this;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public PlaatsBeschrijf setCreatedAt(final String createdAt) {
        this.createdAt = new DateTime(createdAt);
        return this;
    }

    @Override public String toString() {
        return "PlaatsBeschrijf{" +
                "id=" + id +
                ", werfId=" + werfId +
                ", opdrachtgever='" + opdrachtgever + '\'' +
                ", opdrachtLocatie='" + opdrachtLocatie + '\'' +
                ", fotoReeksList=" + fotoReeksList +
                ", title='" + title + '\'' +
                ", ontwerper='" + ontwerper + '\'' +
                ", ontwerperStad='" + ontwerperStad + '\'' +
                ", ontwerperAdres='" + ontwerperAdres + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
