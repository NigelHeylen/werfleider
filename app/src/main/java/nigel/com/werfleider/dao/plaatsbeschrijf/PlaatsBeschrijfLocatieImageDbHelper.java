package nigel.com.werfleider.dao.plaatsbeschrijf;

import java.util.List;

import nigel.com.werfleider.model.PlaatsBeschrijfImage;

/**
 * Created by nigel on 07/02/15.
 */
public interface PlaatsBeschrijfLocatieImageDbHelper {
    /*
   * Creating a plaatsBeschrijf
   */
    long createPlaatsBeschrijfLocatieImage(PlaatsBeschrijfImage plaatsBeschrijfImage, final long plaatsbeschrijfLocatieId);

    /*
    * getting all plaatsbeschrijf locaties
    * */
    List<PlaatsBeschrijfImage> getAllPlaatsBeschrijfsLocatieImages(final long plaatbeschrijfLocatieId);

    void deleteAllImages(int id);
}
