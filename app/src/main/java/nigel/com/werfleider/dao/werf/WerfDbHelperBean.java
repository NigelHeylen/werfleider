package nigel.com.werfleider.dao.werf;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.dao.plaatsbeschrijf.PlaatsBeschrijfDbHelper;
import nigel.com.werfleider.model.PlaatsBeschrijf;
import nigel.com.werfleider.model.Werf;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_NAAM;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_NUMBER;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_WERF;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class WerfDbHelperBean implements WerfDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject PlaatsBeschrijfDbHelper plaatsBeschrijfDbHelper;

    /*
 * Creating a werf
 */
    @Override
    public long createWerf(Werf werf) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAAM, werf.getName());
        values.put(KEY_NUMBER, werf.getNummer());
        values.put(KEY_CREATED_AT, now().toString());

        // insert row
        long werf_id = db.insert(TABLE_WERF, null, values);

        plaatsBeschrijfDbHelper.createPlaatsBeschrijf(new PlaatsBeschrijf(), werf_id);

        return werf_id;
    }

    /*
 * get single werf
 */
    @Override
    public Werf getWerf(long werfId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_WERF + " WHERE "
                + KEY_ID + " = " + werfId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        return new Werf(
                c.getInt(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_NAAM)),
                c.getString(c.getColumnIndex(KEY_NUMBER)),
                c.getString(c.getColumnIndex(KEY_CREATED_AT)));
    }

    /*
 * getting all todos
 * */
    @Override
    public List<Werf> getAllWerfs() {
        List<Werf> werfList = newArrayList();
        String selectQuery = "SELECT  * FROM " + TABLE_WERF;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Werf werf =
                        new Werf(
                                c.getInt(c.getColumnIndex(KEY_ID)),
                                c.getString(c.getColumnIndex(KEY_NUMBER)),
                                c.getString(c.getColumnIndex(KEY_NAAM)),
                                c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                // adding to todo list
                werfList.add(werf);
            } while (c.moveToNext());
        }

        return werfList;
    }

    /*
 * Updating a werf
 */
    @Override
    public int updateWerf(Werf werf) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAAM, werf.getName());
        values.put(KEY_NUMBER, werf.getNummer());

        // updating row
        return db.update(
                TABLE_WERF, values, KEY_ID + " = ?",
                new String[]{String.valueOf(werf.getId())});
    }

    @Override public void closeDB() {
        databaseHelper.closeDB();
    }

    @Override public void deleteWerf(final Werf werf) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_WERF, KEY_ID + " = ?",
                  new String[] { String.valueOf(werf.getId()) });

    }

}
