package nigel.com.werfleider.dao.document;

import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentType;

/**
 * Created by nigel on 07/02/15.
 */
public interface DocumentDbHelper {

    /*
 * Creating a plaatsBeschrijf
 */
    long createDocument(Document document, final long werfId);

    /*
         * get single plaatsBeschrijf
         */
    Document getDocument(long werfId, final DocumentType documentType);


    /*
     * Updating a plaatsBeschrijf
     */
    int updateDocument(Document document);

    // closing database
    void closeDB();

}
