package nigel.com.werfleider.dao.plaatsbeschrijf;

import java.util.List;

import nigel.com.werfleider.model.PlaatsBeschrijf;

/**
 * Created by nigel on 07/02/15.
 */
public interface PlaatsBeschrijfDbHelper {

    /*
 * Creating a plaatsBeschrijf
 */
    long createPlaatsBeschrijf(PlaatsBeschrijf plaatsBeschrijf, final long werfId);

    /*
         * get single plaatsBeschrijf
         */
    PlaatsBeschrijf getPlaatsBeschrijf(long werfId);

    /*
     * getting all todos
     * */
    List<PlaatsBeschrijf> getAllPlaatsBeschrijfs();

    /*
     * Updating a plaatsBeschrijf
     */
    int updatePlaatsBeschrijf(PlaatsBeschrijf plaatsBeschrijf);

    // closing database
    void closeDB();

    void deletePlaatsBeschrijf(PlaatsBeschrijf plaatsBeschrijf);
}
