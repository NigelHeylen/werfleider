package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import nigel.com.werfleider.util.MeasuringUnit;

import static com.google.common.base.Strings.isNullOrEmpty;
import static nigel.com.werfleider.util.ParseStringUtils.ART_NR;
import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_ID;
import static nigel.com.werfleider.util.ParseStringUtils.MEASURING_UNIT;
import static nigel.com.werfleider.util.ParseStringUtils.TITLE;

/**
 * Created by nigel on 14/04/15.
 */
@ParseClassName("DocumentLocation") public class DocumentLocation extends ParseObject {

  public ParseObject getDocumentId() {

    return getParseObject(DOCUMENT_ID);
  }

  public DocumentLocation setDocumentId(final ParseObject document) {

    put(DOCUMENT_ID, document);
    return this;
  }

  public String getTitle() {

    return getString(TITLE);
  }

  public DocumentLocation setTitle(final String title) {

    put(TITLE, title);
    return this;
  }

  public MeasuringUnit getMeasuringUnit() {

    if(isNullOrEmpty(getString(MEASURING_UNIT))){
      return null;
    }

    return MeasuringUnit.valueOf(getString(MEASURING_UNIT));
  }

  public DocumentLocation setMeasuringUnit(final MeasuringUnit measuringUnit) {

    if (getMeasuringUnit() == null || getMeasuringUnit() != measuringUnit) {
      put(MEASURING_UNIT, measuringUnit.name());
    }
    return this;
  }

  public ParseUser getAuthor() {

    return getParseUser(AUTHOR);
  }

  public DocumentLocation setAuthor(ParseUser user) {

    put(AUTHOR, user);
    return this;
  }

  public DocumentLocation setArtNr(final String artNr) {

    put(ART_NR, artNr);
    return this;
  }

  public String getArtNr() {

    return getString(ART_NR);
  }
}
