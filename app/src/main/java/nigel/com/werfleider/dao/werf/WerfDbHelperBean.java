package nigel.com.werfleider.dao.werf;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.document.DocumentDbHelper;
import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.Werf;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_DATUM_AANVANG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_NAAM;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_NUMMER;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OMSCHRIJVING;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ONTWERPER;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ONTWERPER_ADRES;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ONTWERPER_STAD;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHTGEVER;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHTGEVER_ADRES;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHTGEVER_STAD;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHT_ADRES;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHT_STAD;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_WERF;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class WerfDbHelperBean implements WerfDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject DocumentDbHelper documentDbHelper;

    /*
 * Creating a werf
 */
    @Override
    public long createWerf(Werf werf) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = getContentValues(werf);

        return db.insert(TABLE_WERF, null, values);
    }

    private ContentValues getContentValues(final Werf werf) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAAM, werf.getNaam());
        values.put(KEY_NUMMER, werf.getNummer());
        values.put(KEY_OPDRACHT_STAD, werf.getOpdrachtStad());
        values.put(KEY_OPDRACHT_ADRES, werf.getOpdrachtAdres());
        values.put(KEY_ONTWERPER, werf.getOntwerper());
        values.put(KEY_ONTWERPER_ADRES, werf.getOntwerperAdres());
        values.put(KEY_ONTWERPER_STAD, werf.getOntwerperStad());
        values.put(KEY_OPDRACHTGEVER, werf.getOpdrachtgever());
        values.put(KEY_OPDRACHTGEVER_ADRES, werf.getOpdrachtgeverAdres());
        values.put(KEY_OPDRACHTGEVER_STAD, werf.getOpdrachtgeverStad());
        values.put(KEY_OMSCHRIJVING, werf.getOmschrijving());
        values.put(KEY_DATUM_AANVANG, werf.getDatumAanvang().toString());
        values.put(KEY_CREATED_AT, now().toString());
        return values;
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

        return getWerf(c);
    }

    private Werf getWerf(final Cursor c) {
        return new Werf()
                .setId(c.getInt(c.getColumnIndex(KEY_ID)))
                .setNaam(c.getString(c.getColumnIndex(KEY_NAAM)))
                .setNummer(c.getString(c.getColumnIndex(KEY_NUMMER)))
                .setOpdrachtAdres(c.getString(c.getColumnIndex(KEY_OPDRACHT_ADRES)))
                .setOpdrachtStad(c.getString(c.getColumnIndex(KEY_OPDRACHT_STAD)))
                .setOntwerper(c.getString(c.getColumnIndex(KEY_ONTWERPER)))
                .setOntwerperStad(c.getString(c.getColumnIndex(KEY_ONTWERPER_STAD)))
                .setOntwerperAdres(c.getString(c.getColumnIndex(KEY_ONTWERPER_ADRES)))
                .setOpdrachtgever(c.getString(c.getColumnIndex(KEY_OPDRACHTGEVER)))
                .setOpdrachtgeverAdres(c.getString(c.getColumnIndex(KEY_OPDRACHTGEVER_ADRES)))
                .setOpdrachtgeverStad(c.getString(c.getColumnIndex(KEY_OPDRACHTGEVER_STAD)))
                .setOmschrijving(c.getString(c.getColumnIndex(KEY_OMSCHRIJVING)))
                .setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)))
                .setDatumAanvang(new DateTime(c.getString(c.getColumnIndex(KEY_DATUM_AANVANG))));
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
                Werf werf = getWerf(c);
                // adding to werf list
                werfList.add(werf);
                System.out.println("werf = " + werf);
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

        ContentValues values = getContentValues(werf);

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
        db.delete(
                TABLE_WERF, KEY_ID + " = ?",
                new String[]{String.valueOf(werf.getId())});

    }

}
