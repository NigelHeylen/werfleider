package nigel.com.werfleider.dao.plaatsbeschrijf;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.PlaatsBeschrijf;

import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ONTWERPER;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ONTWERPER_ADRES;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ONTWERPER_STAD;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHTGEVER;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OPDRACHTLOCATIE;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_TITLE;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_WERF_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_PLAATSBESCHRIJF;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class PlaatsBeschrijfDbHelperBean implements PlaatsBeschrijfDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject PlaatsBeschrijfLocatieDbHelper plaatsBeschrijfLocatieDbHelper;

    @Override public long createPlaatsBeschrijf(final PlaatsBeschrijf plaatsBeschrijf, final long werf_id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, now().toString());
        values.put(KEY_WERF_ID, werf_id);

        // insert row

        return db.insert(TABLE_PLAATSBESCHRIJF, null, values);
    }

    private ContentValues getContentValues(final PlaatsBeschrijf plaatsBeschrijf) {
        ContentValues values = new ContentValues();
        values.put(KEY_OPDRACHTGEVER, plaatsBeschrijf.getOpdrachtgever());
        values.put(KEY_OPDRACHTLOCATIE, plaatsBeschrijf.getOpdrachtLocatie());
        values.put(KEY_TITLE, plaatsBeschrijf.getTitle());
        values.put(KEY_ONTWERPER, plaatsBeschrijf.getOntwerper());
        values.put(KEY_ONTWERPER_STAD, plaatsBeschrijf.getOntwerperStad());
        values.put(KEY_ONTWERPER_ADRES, plaatsBeschrijf.getOntwerperAdres());
        return values;
    }

    @Override public PlaatsBeschrijf getPlaatsBeschrijf(final long werfId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAATSBESCHRIJF + " WHERE "
                + KEY_WERF_ID + " = " + werfId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        final PlaatsBeschrijf plaatsBeschrijf = new PlaatsBeschrijf();

        plaatsBeschrijf.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                       .setWerfId(c.getInt(c.getColumnIndex(KEY_WERF_ID)))
                       .setOpdrachtgever(c.getString(c.getColumnIndex(KEY_OPDRACHTGEVER)))
                       .setOpdrachtLocatie(c.getString(c.getColumnIndex(KEY_OPDRACHTLOCATIE)))
                       .setTitle(c.getString(c.getColumnIndex(KEY_TITLE)))
                       .setOntwerper(c.getString(c.getColumnIndex(KEY_ONTWERPER)))
                       .setOntwerperAdres(c.getString(c.getColumnIndex(KEY_ONTWERPER_ADRES)))
                       .setOntwerperStad(c.getString(c.getColumnIndex(KEY_ONTWERPER_STAD)))
                       .setFotoReeksList(plaatsBeschrijfLocatieDbHelper.getAllPlaatsBeschrijfsLocaties(plaatsBeschrijf.getId()));

        return plaatsBeschrijf;
    }

    @Override public List<PlaatsBeschrijf> getAllPlaatsBeschrijfs() {
        return null;
    }

    @Override public int updatePlaatsBeschrijf(final PlaatsBeschrijf plaatsBeschrijf) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = getContentValues(plaatsBeschrijf);

        return db.update(
                TABLE_PLAATSBESCHRIJF, values, KEY_ID + " = ?",
                new String[]{String.valueOf(plaatsBeschrijf.getId())});
    }

    @Override public void closeDB() {
        databaseHelper.closeDB();
    }

    @Override public void deletePlaatsBeschrijf(final PlaatsBeschrijf plaatsBeschrijf) {

    }
}
