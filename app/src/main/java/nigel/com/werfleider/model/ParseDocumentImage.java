package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Comparator;
import java.util.Date;

import static nigel.com.werfleider.util.ParseStringUtils.ABSOLUTE_PATH;
import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.DESCRIPTION;
import static nigel.com.werfleider.util.ParseStringUtils.FLOOR;
import static nigel.com.werfleider.util.ParseStringUtils.HEIGHT;
import static nigel.com.werfleider.util.ParseStringUtils.IMAGE;
import static nigel.com.werfleider.util.ParseStringUtils.IMAGE_TAKEN_DATE;
import static nigel.com.werfleider.util.ParseStringUtils.LENGTH;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;
import static nigel.com.werfleider.util.ParseStringUtils.TITLE;
import static nigel.com.werfleider.util.ParseStringUtils.WIDTH;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;

/**
 * Created by nigel on 16/04/15.
 */
@ParseClassName("DocumentImage")
public class ParseDocumentImage extends ParseObject implements Comparable<ParseDocumentImage> {

    public ParseObject getLocationId() {

        return getParseObject(LOCATION_ID);
    }

    public ParseDocumentImage setLocationId(final ParseObject locationId) {

        put(
                LOCATION_ID,
                locationId);
        return this;
    }

    public ParseDocumentImage setImage(final ParseFile image) {

        put(
                IMAGE,
                image);
        return this;
    }

    public ParseFile getImage() {

        return getParseFile(IMAGE);
    }

    public ParseUser getAuthor() {

        return getParseUser(AUTHOR);
    }

    public ParseDocumentImage setAuthor(ParseUser user) {

        put(
                AUTHOR,
                user);
        return this;
    }

    public ParseDocumentImage setDescription(final String description) {

        put(
                DESCRIPTION,
                description);
        return this;
    }

    public String getDescription() {

        return getString(DESCRIPTION);
    }

    public String getTitle() {

        return getString(TITLE);
    }

    public ParseDocumentImage setTitle(final String title) {

        put(
                TITLE,
                title);
        return this;
    }

    public double getHeight() {

        return getDouble(HEIGHT);
    }

    public ParseDocumentImage setHeight(final double height) {

        put(
                HEIGHT,
                height);
        return this;
    }

    public double getLength() {

        return getDouble(LENGTH);
    }

    public ParseDocumentImage setLength(final double length) {

        put(
                LENGTH,
                length);
        return this;
    }

    public double getWidth() {

        return getDouble(WIDTH);
    }

    public ParseDocumentImage setWidth(final double width) {

        put(
                WIDTH,
                width);
        return this;
    }

    public ParseDocumentImage setImageURL(final String absolutePath) {

        put(
                ABSOLUTE_PATH,
                absolutePath);
        return this;
    }

    public String getImageURL() {

        return getString(ABSOLUTE_PATH);
    }

    public String getFloor() {

        return stripToEmpty(getString(FLOOR));
    }

    public ParseDocumentImage setFloor(final String floor) {

        put(
                FLOOR,
                floor);
        return this;
    }

    public String getLocation() {

        return stripToEmpty(getString(LOCATION));
    }

    public ParseDocumentImage setLocation(final String location) {

        put(
                LOCATION,
                location);
        return this;
    }

    public ParseDocumentImage setImageTakenDate(final Date date){

        put(IMAGE_TAKEN_DATE,
            date);
        return this;
    }

    public Date getImageTakenDate(){

        return getDate(IMAGE_TAKEN_DATE);
    }

    @Override public int compareTo(final ParseDocumentImage that) {

        return this.getCreatedAt().compareTo(that.getCreatedAt());
    }

    public static final Comparator<ParseDocumentImage> COMPARE_BY_FLOOR = new Comparator<ParseDocumentImage>() {
        @Override public int compare(final ParseDocumentImage lhs, final ParseDocumentImage rhs) {

            return
                    lhs.getFloor().compareTo(rhs.getFloor());
        }
    };
}
