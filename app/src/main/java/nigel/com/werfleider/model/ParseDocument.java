package nigel.com.werfleider.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import static nigel.com.werfleider.util.ParseStringUtils.AUTHOR;
import static nigel.com.werfleider.util.ParseStringUtils.DOCUMENT_TYPE;
import static nigel.com.werfleider.util.ParseStringUtils.NAME;
import static nigel.com.werfleider.util.ParseStringUtils.YARD_ID;

/**
 * Created by nigel on 14/04/15.
 */
@ParseClassName("Document") public class ParseDocument extends ParseObject {

  public ParseObject getWerfId() {

    return getParseObject(YARD_ID);
  }

  public ParseDocument setWerf(final Yard werf) {

    put(YARD_ID, werf);
    return this;
  }

  public DocumentType getDocumentType() {

    return DocumentType.valueOf(getString(DOCUMENT_TYPE));
  }

  public ParseDocument setDocumentType(final DocumentType documentType) {

    put(DOCUMENT_TYPE, documentType.name());
    return this;
  }

  public ParseUser getAuthor() {
    return getParseUser(AUTHOR);
  }

  public ParseDocument setAuthor(ParseUser user) {
    put(AUTHOR, user);
    return this;
  }

  public ParseDocument setName(final String name) {

    put(NAME, name);
    return this;
  }

  public String getName(){

    return getString(NAME);
  }
}
