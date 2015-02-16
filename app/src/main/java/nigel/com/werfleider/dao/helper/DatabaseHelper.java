package nigel.com.werfleider.dao.helper;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by nigel on 07/02/15.
 */
public interface DatabaseHelper {

   

    // closing database
    void closeDB();

    SQLiteDatabase getWritableDatabase();

    SQLiteDatabase getReadableDatabase();
}
