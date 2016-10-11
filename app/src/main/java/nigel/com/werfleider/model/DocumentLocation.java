package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import nigel.com.werfleider.util.MeasuringUnit;

import static com.google.common.base.Strings.isNullOrEmpty;
import static nigel.com.werfleider.util.ParseStringUtils.ART_NR;
import static nigel.com.werfleider.util.ParseStringUtils.CREATOR;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_TYPE;
import static nigel.com.werfleider.util.ParseStringUtils.MEASURING_UNIT;
import static nigel.com.werfleider.util.ParseStringUtils.TITLE;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ID;

/**
 * Created by nigel on 14/04/15.
 */
@ParseClassName("DocumentLocation") public class DocumentLocation extends ParseObject {

  public DocumentLocation setWerf(final Yard werf) {

    put(YARD_ID, werf);
    return this;
  }

  public DocumentType getDocumentType() {

    return DocumentType.valueOf(getString(DOCUMENT_TYPE));
  }

  public DocumentLocation setDocumentType(final DocumentType documentType) {

    put(DOCUMENT_TYPE, documentType.name());
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

  public DocumentLocation setCreator(final String creator) {
    put(CREATOR, creator);
    return this;
  }

  public String getCreator() {
    return getString(CREATOR);
  }

  public DocumentLocation setArtNr(final String artNr) {

    put(ART_NR, artNr);
    return this;
  }

  public String getArtNr() {

    return getString(ART_NR);
  }
}
