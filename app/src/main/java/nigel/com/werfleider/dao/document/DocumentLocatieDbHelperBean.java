package nigel.com.werfleider.dao.document;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocatie;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_LOCATIE;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_DOCUMENT_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_DOCUMENT_LOCATIE;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class DocumentLocatieDbHelperBean implements DocumentLocatieDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject DocumentLocatieImageDbHelper documentLocatieImageDbHelper;

    @Override public long createDocumentLocatie(final DocumentLocatie documentLocatie, final long plaatsbeschrijfId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, now().toString());
        values.put(KEY_LOCATIE, documentLocatie.getLocation());
        values.put(KEY_DOCUMENT_ID, plaatsbeschrijfId);

        // insert row

        final long index = db.insert(TABLE_DOCUMENT_LOCATIE, null, values);

        for (DocumentImage documentImage : documentLocatie.getImageList()) {
            documentLocatieImageDbHelper.createDocumentLocatieImage(documentImage, index);
        }

        return index;
    }

    @Override public DocumentLocatie getDocumentLocatie(final long plaatsbeschrijfLocatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT_LOCATIE + " WHERE "
                + KEY_ID + " = " + plaatsbeschrijfLocatieId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        final DocumentLocatie documentLocatie =
                new DocumentLocatie(c.getString(c.getColumnIndex(KEY_LOCATIE)));

        documentLocatie.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                              .setImageList(documentLocatieImageDbHelper.getAllDocumentLocatieImages(plaatsbeschrijfLocatieId));

        return documentLocatie;
    }

    @Override public List<DocumentLocatie> getAllPlaatsBeschrijfsLocaties(final long plaatsbeschrijfId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        final List<DocumentLocatie> documentLocatieList = newArrayList();

        String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT_LOCATIE + " WHERE "
                + KEY_DOCUMENT_ID + " = " + plaatsbeschrijfId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                final DocumentLocatie documentLocatie =
                        new DocumentLocatie(c.getString(c.getColumnIndex(KEY_LOCATIE)));

                documentLocatie.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                                      .setImageList(
                                              documentLocatieImageDbHelper.getAllDocumentLocatieImages(c.getInt(c.getColumnIndex(KEY_ID))));

                documentLocatieList.add(documentLocatie);


            } while (c.moveToNext());
        }

        return documentLocatieList;
    }

    @Override public int updatePlaatsBeschrijfLocatie(final DocumentLocatie documentLocatie) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIE, documentLocatie.getLocation());

        documentLocatieImageDbHelper.deleteAllImages(documentLocatie.getId());

        for (DocumentImage documentImage : documentLocatie.getImageList()) {
            documentLocatieImageDbHelper.createDocumentLocatieImage(documentImage, documentLocatie.getId());
        }


        return db.update(
                TABLE_DOCUMENT_LOCATIE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(documentLocatie.getId())});
    }

    @Override public void closeDB() {
        databaseHelper.closeDB();
    }

    @Override public void deletePlaatsBeschrijfLocatie(final DocumentLocatie documentLocatie) {

    }
}
