package nigel.com.werfleider.model;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by nigel on 03/12/14.
 */
public class DocumentImage {

    private int id;

    private int locatieId;

    private String imageURL;

    private String description;

    public String getImageURL() {
        return imageURL;
    }

    public String getDescription() {
        return description;
    }

    @Override public String toString() {
        return "PlaatsBeschrijfImage{" +
                "imageURL='" + imageURL + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getOnDiskUrl(){
        return String.format("file://%s", getImageURL());
    }

    public int getId() {
        return id;
    }

    public int getLocatieId() {
        return locatieId;
    }

    public DocumentImage setId(final int id) {
        this.id = id;
        return this;
    }

    public DocumentImage setDescription(final String description) {
        this.description = description;
        return this;
    }

    public DocumentImage setLocatieId(final int locatieId) {
        this.locatieId = locatieId;
        return this;
    }

    public DocumentImage setImageURL(final String imageURL) {
        this.imageURL = imageURL;
        return this;
    }

    public boolean hasDescription() {
        return !isNullOrEmpty(getDescription());
    }
}
