package nigel.com.werfleider.dao.document;

import java.util.List;

import nigel.com.werfleider.model.DocumentLocation;

/**
 * Created by nigel on 07/02/15.
 */
public interface DocumentLocatieDbHelper {

    /*
 * Creating a plaatsBeschrijf
 */
    long createDocumentLocatie(DocumentLocation documentLocation, final long documentId);

    /*
         * get single plaatsBeschrijf
         */
    DocumentLocation getDocumentLocatie(long werfId);

    /*
     * getting all plaatsbeschrijf locaties
     * */
    List<DocumentLocation> getAllDocumentLocations(final long documentId);

    /*
     * Updating a plaatsBeschrijf
     */
    int updateDocumentLocatie(DocumentLocation documentLocation);

    // closing database
    void closeDB();

    void deleteDocumentLocatie(DocumentLocation documentLocation);

    List<DocumentLocation> getFirstDocumentLocation(final long documentId);
}
