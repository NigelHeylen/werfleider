package nigel.com.werfleider.dao.document;

import java.util.List;

import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentType;
import nigel.com.werfleider.model.Werf;

/**
 * Created by nigel on 07/02/15.
 */
public interface DocumentDbHelper {

    /*
 * Creating a plaatsBeschrijf
 */
    long createDocument(Document document, final long werfId);

    Document createDocument(DocumentType documentType, final long werfId);

    /*
         * get single plaatsBeschrijf
         */
    Document getDocument(final long documentId);


    /*
     * Updating a plaatsBeschrijf
     */
    int updateDocument(Document document);

    // closing database
    void closeDB();

    List<Document> getDocuments(Werf werf, DocumentType documentType);

    int deleteDocument(int id);
}
