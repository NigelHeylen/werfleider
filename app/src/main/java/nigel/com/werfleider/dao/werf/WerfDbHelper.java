package nigel.com.werfleider.dao.werf;

import java.util.List;

import nigel.com.werfleider.model.Werf;

/**
 * Created by nigel on 07/02/15.
 */
public interface WerfDbHelper {

    /*
 * Creating a werf
 */
    long createWerf(Werf werf);

    /*
         * get single werf
         */
    Werf getWerf(long werfId);

    /*
     * getting all todos
     * */
    List<Werf> getAllWerfs();

    /*
     * Updating a werf
     */
    int updateWerf(Werf werf);

    // closing database
    void closeDB();

    void deleteWerf(Werf werf);
}
