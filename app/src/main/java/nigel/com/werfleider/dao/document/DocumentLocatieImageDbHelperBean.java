package nigel.com.werfleider.dao.document;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.DocumentImage;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_IMAGE_URL;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_OMSCHRIJVING;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_DOCUMENT_LOCATIE_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_DOCUMENT_LOCATIE_IMAGE;

/**
 * Created by nigel on 07/02/15.
 */
public class DocumentLocatieImageDbHelperBean implements DocumentLocatieImageDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Override public long createDocumentLocatieImage(final DocumentImage documentImage, final long documentLocatieId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DOCUMENT_LOCATIE_ID, documentLocatieId);
        values.put(KEY_IMAGE_URL, documentImage.getImageURL());
        values.put(KEY_OMSCHRIJVING, documentImage.getDescription());

        // insert row

        return db.insert(TABLE_DOCUMENT_LOCATIE_IMAGE, null, values);

    }

    @Override public List<DocumentImage> getAllDocumentLocatieImages(final long plaatbeschrijfLocatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        final List<DocumentImage> documentImageList = newArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT_LOCATIE_IMAGE + " WHERE "
                + KEY_DOCUMENT_LOCATIE_ID + " = " + plaatbeschrijfLocatieId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do{
                final DocumentImage documentImage =
                        new DocumentImage();

                documentImage
                        .setDescription(c.getString(c.getColumnIndex(KEY_OMSCHRIJVING)))
                        .setImageURL(c.getString(c.getColumnIndex(KEY_IMAGE_URL)))
                        .setId(c.getInt(c.getColumnIndex(KEY_ID)))
                        .setLocatieId(c.getInt(c.getColumnIndex(KEY_DOCUMENT_LOCATIE_ID)));

                documentImageList.add(documentImage);

            }while(c.moveToNext());
        }

        return documentImageList;
    }

    @Override public void deleteAllImages(final int locatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        db.delete(TABLE_DOCUMENT_LOCATIE_IMAGE, KEY_DOCUMENT_LOCATIE_ID + " = ?",
                  new String[] { String.valueOf(locatieId) });
    }
}
