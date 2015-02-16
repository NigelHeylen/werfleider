package nigel.com.werfleider.dao.plaatsbeschrijf;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.PlaatsBeschrijfImage;
import nigel.com.werfleider.model.PlaatsBeschrijfLocatie;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_LOCATIE;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_PLAATSBESCHRIJF_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_PLAATSBESCHRIJF_LOCATIE;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class PlaatsBeschrijfLocatieDbHelperBean implements PlaatsBeschrijfLocatieDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject PlaatsBeschrijfLocatieImageDbHelper plaatsBeschrijfLocatieImageDbHelper;

    @Override public long createPlaatsBeschrijfLocatie(final PlaatsBeschrijfLocatie plaatsBeschrijfLocatie, final long plaatsbeschrijfId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, now().toString());
        values.put(KEY_LOCATIE, plaatsBeschrijfLocatie.getLocation());
        values.put(KEY_PLAATSBESCHRIJF_ID, plaatsbeschrijfId);

        // insert row

        final long index = db.insert(TABLE_PLAATSBESCHRIJF_LOCATIE, null, values);

        for (PlaatsBeschrijfImage plaatsBeschrijfImage : plaatsBeschrijfLocatie.getImageList()) {
            plaatsBeschrijfLocatieImageDbHelper.createPlaatsBeschrijfLocatieImage(plaatsBeschrijfImage, index);
        }

        return index;
    }

    @Override public PlaatsBeschrijfLocatie getPlaatsBeschrijfLocatie(final long plaatsbeschrijfLocatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAATSBESCHRIJF_LOCATIE + " WHERE "
                + KEY_ID + " = " + plaatsbeschrijfLocatieId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        final PlaatsBeschrijfLocatie plaatsBeschrijfLocatie =
                new PlaatsBeschrijfLocatie(c.getString(c.getColumnIndex(KEY_LOCATIE)));

        plaatsBeschrijfLocatie.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                              .setImageList(plaatsBeschrijfLocatieImageDbHelper.getAllPlaatsBeschrijfsLocatieImages(plaatsbeschrijfLocatieId));

        return plaatsBeschrijfLocatie;
    }

    @Override public List<PlaatsBeschrijfLocatie> getAllPlaatsBeschrijfsLocaties(final long plaatsbeschrijfId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        final List<PlaatsBeschrijfLocatie> plaatsBeschrijfLocatieList = newArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_PLAATSBESCHRIJF_LOCATIE + " WHERE "
                + KEY_PLAATSBESCHRIJF_ID + " = " + plaatsbeschrijfId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                final PlaatsBeschrijfLocatie plaatsBeschrijfLocatie =
                        new PlaatsBeschrijfLocatie(c.getString(c.getColumnIndex(KEY_LOCATIE)));

                plaatsBeschrijfLocatie.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                                      .setImageList(
                                              plaatsBeschrijfLocatieImageDbHelper.getAllPlaatsBeschrijfsLocatieImages(c.getInt(c.getColumnIndex(KEY_ID))));

                plaatsBeschrijfLocatieList.add(plaatsBeschrijfLocatie);


            } while (c.moveToNext());
        }

        return plaatsBeschrijfLocatieList;
    }

    @Override public int updatePlaatsBeschrijfLocatie(final PlaatsBeschrijfLocatie plaatsBeschrijfLocatie) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIE, plaatsBeschrijfLocatie.getLocation());

        plaatsBeschrijfLocatieImageDbHelper.deleteAllImages(plaatsBeschrijfLocatie.getId());

        for (PlaatsBeschrijfImage plaatsBeschrijfImage : plaatsBeschrijfLocatie.getImageList()) {
            plaatsBeschrijfLocatieImageDbHelper.createPlaatsBeschrijfLocatieImage(plaatsBeschrijfImage, plaatsBeschrijfLocatie.getId());
        }


        return db.update(
                TABLE_PLAATSBESCHRIJF_LOCATIE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(plaatsBeschrijfLocatie.getId())});
    }

    @Override public void closeDB() {
        databaseHelper.closeDB();
    }

    @Override public void deletePlaatsBeschrijfLocatie(final PlaatsBeschrijfLocatie plaatsBeschrijfLocatie) {

    }
}
