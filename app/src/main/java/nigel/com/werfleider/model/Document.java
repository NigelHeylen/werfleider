package nigel.com.werfleider.model;

import org.joda.time.DateTime;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by nigel on 31/01/15.
 */
public class Document {

    private int id;

    private int werfId;

    private DocumentType documentType;

    private List<DocumentLocation> fotoReeksList = newArrayList();

    private DateTime createdAt;

    public Document() {
    }

    public List<DocumentLocation> getFotoReeksList() {
        return fotoReeksList;
    }

    public Document setFotoReeksList(final List<DocumentLocation> fotoReeksList) {
        this.fotoReeksList = fotoReeksList;
        return this;
    }

    public void add(final DocumentLocation collection) {
        fotoReeksList.add(collection);
    }

    public int getWerfId() {
        return werfId;
    }

    public Document setWerfId(final int werfId) {
        this.werfId = werfId;
        return this;
    }

    public int getId() {
        return id;
    }

    public Document setId(final int id) {
        this.id = id;
        return this;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public Document setCreatedAt(final String createdAt) {
        this.createdAt = new DateTime(createdAt);
        return this;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public Document setDocumentType(final DocumentType documentType) {
        this.documentType = documentType;
        return this;
    }

    public Document setCreatedAt(final DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override public String toString() {
        return "Document{" +
                "id=" + id +
                ", werfId=" + werfId +
                ", documentType=" + documentType +
                ", fotoReeksList=" + fotoReeksList +
                ", createdAt=" + createdAt +
                '}';
    }
}
