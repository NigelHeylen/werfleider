package nigel.com.werfleider.dao.document;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.DocumentImage;
import nigel.com.werfleider.model.DocumentLocation;
import nigel.com.werfleider.util.MeasuringUnit;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_DOCUMENT_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_LOCATIE;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_MEASURING_UNIT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_DOCUMENT_LOCATIE;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class DocumentLocatieDbHelperBean implements DocumentLocatieDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject DocumentLocatieImageDbHelper documentLocatieImageDbHelper;

    @Override public long createDocumentLocatie(final DocumentLocation documentLocation, final long plaatsbeschrijfId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, now().toString());
        values.put(KEY_LOCATIE, documentLocation.getLocation());
        values.put(KEY_DOCUMENT_ID, plaatsbeschrijfId);
        values.put(KEY_MEASURING_UNIT, MeasuringUnit.MM.name());

        // insert row

        final long index = db.insert(TABLE_DOCUMENT_LOCATIE, null, values);

        for (DocumentImage documentImage : documentLocation.getImageList()) {
            documentLocatieImageDbHelper.createDocumentLocatieImage(documentImage, index);
        }

        return index;
    }

    @Override public DocumentLocation getDocumentLocatie(final long plaatsbeschrijfLocatieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT_LOCATIE + " WHERE "
                + KEY_ID + " = " + plaatsbeschrijfLocatieId;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        final DocumentLocation documentLocation =
                new DocumentLocation(c.getString(c.getColumnIndex(KEY_LOCATIE)));

        documentLocation.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                        .setImageList(documentLocatieImageDbHelper.getAllDocumentLocatieImages(plaatsbeschrijfLocatieId));

        return documentLocation;
    }

    @Override public List<DocumentLocation> getAllDocumentLocations(final long documentId) {
        final String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT_LOCATIE + " WHERE "
                + KEY_DOCUMENT_ID + " = " + documentId;

        return getDocumentLocatiesByQuery(selectQuery);
    }

    @Override public int updateDocumentLocatie(final DocumentLocation documentLocation) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCATIE, documentLocation.getLocation());
        values.put(KEY_MEASURING_UNIT, documentLocation.getMeasuringUnit().name());

        documentLocatieImageDbHelper.deleteAllImages(documentLocation.getId());

        for (DocumentImage documentImage : documentLocation.getImageList()) {
            documentLocatieImageDbHelper.createDocumentLocatieImage(documentImage, documentLocation.getId());
        }


        return db.update(
                TABLE_DOCUMENT_LOCATIE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(documentLocation.getId())});
    }

    @Override public void closeDB() {
        databaseHelper.closeDB();
    }

    @Override public void deleteDocumentLocatie(final DocumentLocation documentLocation) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(
                TABLE_DOCUMENT_LOCATIE, KEY_ID + " = ?",
                new String[]{String.valueOf(documentLocation.getId())});

        documentLocatieImageDbHelper.deleteAllImages(documentLocation.getId());
    }

    @Override public List<DocumentLocation> getFirstDocumentLocation(final long documentId) {
        final String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT_LOCATIE + " WHERE "
                + KEY_DOCUMENT_ID + " = " + documentId + " LIMIT 1";

        return getDocumentLocatiesByQuery(selectQuery);
    }

    private List<DocumentLocation> getDocumentLocatiesByQuery(final String query) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        final List<DocumentLocation> documentLocationList = newArrayList();

        Log.e(LOG, query);

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                final DocumentLocation documentLocation =
                        new DocumentLocation(c.getString(c.getColumnIndex(KEY_LOCATIE)));

                documentLocation.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                                .setImageList(
                                        documentLocatieImageDbHelper.getAllDocumentLocatieImages(c.getInt(c.getColumnIndex(KEY_ID))))
                                .setMeasuringUnit(MeasuringUnit.valueOf(c.getString(c.getColumnIndex(KEY_MEASURING_UNIT))));

                documentLocationList.add(documentLocation);


            } while (c.moveToNext());
        }

        return documentLocationList;
    }
}
