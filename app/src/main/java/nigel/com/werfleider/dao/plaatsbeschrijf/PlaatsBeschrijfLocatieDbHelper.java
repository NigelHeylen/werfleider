package nigel.com.werfleider.dao.plaatsbeschrijf;

import java.util.List;

import nigel.com.werfleider.model.PlaatsBeschrijfLocatie;

/**
 * Created by nigel on 07/02/15.
 */
public interface PlaatsBeschrijfLocatieDbHelper {

    /*
 * Creating a plaatsBeschrijf
 */
    long createPlaatsBeschrijfLocatie(PlaatsBeschrijfLocatie plaatsBeschrijfLocatie, final long plaatsbeschrijfId);

    /*
         * get single plaatsBeschrijf
         */
    PlaatsBeschrijfLocatie getPlaatsBeschrijfLocatie(long werfId);

    /*
     * getting all plaatsbeschrijf locaties
     * */
    List<PlaatsBeschrijfLocatie> getAllPlaatsBeschrijfsLocaties(final long plaatbeschrijfId);

    /*
     * Updating a plaatsBeschrijf
     */
    int updatePlaatsBeschrijfLocatie(PlaatsBeschrijfLocatie plaatsBeschrijfLocatie);

    // closing database
    void closeDB();

    void deletePlaatsBeschrijfLocatie(PlaatsBeschrijfLocatie plaatsBeschrijfLocatie);
}
