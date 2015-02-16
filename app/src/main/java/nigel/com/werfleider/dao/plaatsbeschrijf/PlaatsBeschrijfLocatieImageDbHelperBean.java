package nigel.com.werfleider.dao.plaatsbeschrijf;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.PlaatsBeschrijfImage;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_IMAGE_URL;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OMSCHRIJVING;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_PLAATSBESCHRIJF_LOCATIE_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_PLAATSBESCHRIJF_LOCATIE_IMAGE;

/**
 * Created by nigel on 07/02/15.
 */
public class PlaatsBeschrijfLocatieImageDbHelperBean implements PlaatsBeschrijfLocatieImageDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Override public long createPlaatsBeschrijfLocatieImage(final PlaatsBeschrijfImage plaatsBeschrijfImage, final long plaatsbeschrijfLocatieId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLAATSBESCHRIJF_LOCATIE_ID, plaatsbeschrijfLocatieId);
        values.put(KEY_IMAGE_URL, plaatsBeschrijfImage.getImageURL());
        values.put(KEY_OMSCHRIJVING, plaatsBeschrijfImage.getDescription());

        // insert row

        return db.insert(TABLE_PLAATSBESCHRIJF_LOCATIE_IMAGE, null, values);

    }

    @Override public List<PlaatsBeschrijfImage> getAllPlaatsBeschrijfsLocatieImages(final long plaatbeschrijfLocatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        final List<PlaatsBeschrijfImage> plaatsBeschrijfImageList = newArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_PLAATSBESCHRIJF_LOCATIE_IMAGE + " WHERE "
                + KEY_PLAATSBESCHRIJF_LOCATIE_ID + " = " + plaatbeschrijfLocatieId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                final PlaatsBeschrijfImage plaatsBeschrijfImage =
                        new PlaatsBeschrijfImage();

                plaatsBeschrijfImage
                        .setDescription(c.getString(c.getColumnIndex(KEY_OMSCHRIJVING)))
                        .setImageURL(c.getString(c.getColumnIndex(KEY_IMAGE_URL)))
                        .setId(c.getInt(c.getColumnIndex(KEY_ID)))
                        .setLocatieId(c.getInt(c.getColumnIndex(KEY_PLAATSBESCHRIJF_LOCATIE_ID)));

                plaatsBeschrijfImageList.add(plaatsBeschrijfImage);

            }while(c.moveToNext());
        }

        return plaatsBeschrijfImageList;
    }

    @Override public void deleteAllImages(final int locatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        db.delete(TABLE_PLAATSBESCHRIJF_LOCATIE_IMAGE, KEY_PLAATSBESCHRIJF_LOCATIE_ID + " = ?",
                  new String[] { String.valueOf(locatieId) });
    }
}
