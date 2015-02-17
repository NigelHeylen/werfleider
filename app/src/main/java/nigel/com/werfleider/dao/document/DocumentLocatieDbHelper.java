package nigel.com.werfleider.dao.document;

import java.util.List;

import nigel.com.werfleider.model.DocumentLocatie;

/**
 * Created by nigel on 07/02/15.
 */
public interface DocumentLocatieDbHelper {

    /*
 * Creating a plaatsBeschrijf
 */
    long createDocumentLocatie(DocumentLocatie documentLocatie, final long plaatsbeschrijfId);

    /*
         * get single plaatsBeschrijf
         */
    DocumentLocatie getDocumentLocatie(long werfId);

    /*
     * getting all plaatsbeschrijf locaties
     * */
    List<DocumentLocatie> getAllPlaatsBeschrijfsLocaties(final long plaatbeschrijfId);

    /*
     * Updating a plaatsBeschrijf
     */
    int updatePlaatsBeschrijfLocatie(DocumentLocatie documentLocatie);

    // closing database
    void closeDB();

    void deletePlaatsBeschrijfLocatie(DocumentLocatie documentLocatie);
}
