package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static nigel.com.werfleider.util.ParseStringUtils.CREATOR;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_TYPE;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ID;

/**
 * Created by nigel on 14/04/15.
 */
@ParseClassName("Document") public class Document extends ParseObject {

  public ParseObject getWerfId() {

    return getParseObject(YARD_ID);
  }

  public Document setWerf(final Yard werf) {

    put(YARD_ID, werf);
    return this;
  }

  public DocumentType getDocumentType() {

    return DocumentType.valueOf(getString(DOCUMENT_TYPE));
  }

  public Document setDocumentType(final DocumentType documentType) {

    put(DOCUMENT_TYPE, documentType.name());
    return this;
  }


  public Document setCreator(final String creator) {
    put(CREATOR, creator);
    return this;
  }

  public String getCreator() {
    return getString(CREATOR);
  }

  public Document setName(final String name) {

    put(NAME, name);
    return this;
  }

  public String getName(){

    return getString(NAME);
  }
}
