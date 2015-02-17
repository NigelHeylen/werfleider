package nigel.com.werfleider.dao.document;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.Document;
import nigel.com.werfleider.model.DocumentType;

import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_DOCUMENT_TYPE;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_WERF_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_DOCUMENT;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 07/02/15.
 */
public class DocumentDbHelperBean implements DocumentDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Inject DocumentLocatieDbHelper documentLocatieDbHelper;

    @Override public long createDocument(final Document document, final long werf_id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = getContentValues(document.setWerfId((int) werf_id));

        // insert row

        return db.insert(TABLE_DOCUMENT, null, values);
    }

    private ContentValues getContentValues(final Document document) {
        ContentValues values = new ContentValues();
        values.put(KEY_CREATED_AT, now().toString());
        values.put(KEY_DOCUMENT_TYPE, document.getDocumentType().toString());
        values.put(KEY_WERF_ID, document.getWerfId());

        return values;
    }

    @Override public Document getDocument(final long werfId, final DocumentType documentType) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_DOCUMENT + " WHERE " +
                KEY_WERF_ID + " = " + werfId + " AND " +
                KEY_DOCUMENT_TYPE + " = '" + documentType.name() + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        final Document document = new Document();

        document.setId(c.getInt(c.getColumnIndex(KEY_ID)))
                .setWerfId(c.getInt(c.getColumnIndex(KEY_WERF_ID)))
                .setDocumentType(DocumentType.valueOf(c.getString(c.getColumnIndex(KEY_DOCUMENT_TYPE))))
                .setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)))
                .setFotoReeksList(documentLocatieDbHelper.getAllPlaatsBeschrijfsLocaties(document.getId()));

        return document;
    }


    @Override public int updateDocument(final Document document) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = getContentValues(document);

        return db.update(
                TABLE_DOCUMENT, values, KEY_ID + " = ?",
                new String[]{String.valueOf(document.getId())});
    }

    @Override public void closeDB() {
        databaseHelper.closeDB();
    }

}
