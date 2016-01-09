package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import nigel.com.werfleider.util.MeasuringUnit;

import static com.google.common.base.Strings.nullToEmpty;
import static nigel.com.werfleider.util.ParseStringUtils.ABSOLUTE_PATH;
import static nigel.com.werfleider.util.ParseStringUtils.AUDIO_BYTES;
import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.DESCRIPTION;
import static nigel.com.werfleider.util.ParseStringUtils.FLOOR;
import static nigel.com.werfleider.util.ParseStringUtils.HEIGHT;
import static nigel.com.werfleider.util.ParseStringUtils.IMAGE;
import static nigel.com.werfleider.util.ParseStringUtils.IMAGE_BYTES;
import static nigel.com.werfleider.util.ParseStringUtils.IMAGE_TAKEN_DATE;
import static nigel.com.werfleider.util.ParseStringUtils.LENGTH;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION;
import static nigel.com.werfleider.util.ParseStringUtils.LOCATION_ID;
import static nigel.com.werfleider.util.ParseStringUtils.MS;
import static nigel.com.werfleider.util.ParseStringUtils.QUANTITY;
import static nigel.com.werfleider.util.ParseStringUtils.TITLE;
import static nigel.com.werfleider.util.ParseStringUtils.WIDTH;

/**
 * Created by nigel on 16/04/15.
 */
@ParseClassName("DocumentImage") public class ParseDocumentImage extends ParseObject
    implements Comparable<ParseDocumentImage> {

  public ParseObject getLocationId() {

    return getParseObject(LOCATION_ID);
  }

  public ParseDocumentImage setLocationId(final ParseObject locationId) {

    put(LOCATION_ID, locationId);
    return this;
  }

  public ParseDocumentImage setImage(final ParseFile image) {

    put(IMAGE, image);
    return this;
  }

  public ParseFile getImage() {

    return getParseFile(IMAGE);
  }

  public ParseUser getAuthor() {

    return getParseUser(AUTHOR);
  }

  public ParseDocumentImage setAuthor(ParseUser user) {

    put(AUTHOR, user);
    return this;
  }

  public ParseDocumentImage setDescription(final String description) {

    if (!getDescription().equals(description)) {
      put(DESCRIPTION, description);
    }
    return this;
  }

  public String getDescription() {

    return nullToEmpty(getString(DESCRIPTION));
  }

  public String getTitle() {

    return nullToEmpty(getString(TITLE));
  }

  public ParseDocumentImage setTitle(final String title) {

    put(TITLE, title);
    return this;
  }

  public double getHeight() {

    return getDouble(HEIGHT);
  }

  public ParseDocumentImage setHeight(final double height) {

    if (getHeight() != height) {
      put(HEIGHT, height);
    }
    return this;
  }

  public double getLength() {

    return getDouble(LENGTH);
  }

  public ParseDocumentImage setLength(final double length) {

    if (length != getLength()) {
      put(LENGTH, length);
    }
    return this;
  }

  public double getWidth() {

    return getDouble(WIDTH);
  }

  public ParseDocumentImage setWidth(final double width) {

    if (getWidth() != width) {
      put(WIDTH, width);
    }
    return this;
  }

  public ParseDocumentImage setImageURL(final String absolutePath) {

    put(ABSOLUTE_PATH, absolutePath);
    return this;
  }

  public String getImageURL() {

    return getString(ABSOLUTE_PATH);
  }

  public String getFloor() {

    return nullToEmpty(getString(FLOOR));
  }

  public ParseDocumentImage setFloor(final String floor) {

    if (!getFloor().equals(floor)) {
      put(FLOOR, floor);
    }
    return this;
  }

  public int getQuantity() {

    return getInt(QUANTITY);
  }

  public ParseDocumentImage setQuantity(final int quantity) {

    if (getQuantity() != quantity) {
      put(QUANTITY, quantity);
    }
    return this;
  }

  public String getLocation() {

    return nullToEmpty(getString(LOCATION));
  }

  public ParseDocumentImage setLocation(final String location) {

    if (!Objects.equals(getLocation(), location)) {
      put(LOCATION, location);
    }
    return this;
  }

  public ParseDocumentImage setImageTakenDate(final Date date) {

    put(IMAGE_TAKEN_DATE, date);
    return this;
  }

  public Date getImageTakenDate() {

    return getDate(IMAGE_TAKEN_DATE);
  }

  @Override public int compareTo(final ParseDocumentImage that) {

    return this.getCreatedAt().compareTo(that.getCreatedAt());
  }

  public static final Comparator<ParseDocumentImage> COMPARE_BY_FLOOR =
      (lhs, rhs) -> lhs.getFloor().compareTo(rhs.getFloor());

  public ParseDocumentImage setMS(final String ms) {

    put(MS, ms);
    return this;
  }

  public String getMS() {

    return getString(MS);
  }

  public double getTotal(final MeasuringUnit measuringUnit) {

    switch (measuringUnit.getWeight()) {
      case 0:
        return getLength();
      case 1:
        return getLength() * getWidth();
      case 2:
        return getLength() * getWidth() * getHeight();
    }

    return 0;
  }

  public boolean hasImage() {

    return getImage() != null;
  }

  public ParseDocumentImage setImageBytes(byte[] bytesFromFilePath) {
    put(IMAGE_BYTES, bytesFromFilePath);
    return this;
  }

  public byte[] getImageBytes() {

    return getBytes(IMAGE_BYTES);
  }

  public ParseDocumentImage setAudio(byte[] audioBytes) {
    put(AUDIO_BYTES, audioBytes);
    return this;
  }

  public boolean hasAudio(){

    return containsKey(AUDIO_BYTES) && getAudio().length > 0;
  }

  public byte[] getAudio(){

    return getBytes(AUDIO_BYTES);
  }
}
