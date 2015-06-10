package nigel.com.werfleider.model;

import java.util.Date;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by nigel on 03/12/14.
 */
public class DocumentImage {

    private int id;

    private int locatieId;

    private String imageURL;

    private String description;

    private String title;

    private Date createdDate;

    public String getImageURL() {

        return imageURL;
    }

    public String getDescription() {

        return description;
    }

    private double height;

    private double length;
    private double width;

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

    public double getHeight() {
        return height;
    }

    public DocumentImage setHeight(final double height) {
        this.height = height;
        return this;
    }

    public double getLength() {
        return length;
    }

    public DocumentImage setLength(final double length) {
        this.length = length;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public DocumentImage setWidth(final double width) {
        this.width = width;
        return this;
    }

    public DocumentImage setTitle(final String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return isNullOrEmpty(title) ? "" : title;
    }

    public DocumentImage setCreatedDate(final Date createdDate) {

        this.createdDate = createdDate;
        return this;
    }

    public Date getCreatedDate() {

        return createdDate;
    }
}
