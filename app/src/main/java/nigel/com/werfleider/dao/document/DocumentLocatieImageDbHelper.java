package nigel.com.werfleider.dao.document;

import java.util.List;

import nigel.com.werfleider.model.DocumentImage;

/**
 * Created by nigel on 07/02/15.
 */
public interface DocumentLocatieImageDbHelper {
    /*
   * Creating a plaatsBeschrijf
   */
    long createDocumentLocatieImage(DocumentImage documentImage, final long plaatsbeschrijfLocatieId);

    /*
    * getting all plaatsbeschrijf locaties
    * */
    List<DocumentImage> getAllDocumentLocatieImages(final long plaatbeschrijfLocatieId);

    void deleteAllImages(int locatieId);
}
