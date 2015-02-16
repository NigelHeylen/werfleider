package nigel.com.werfleider.dao.contact;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import nigel.com.werfleider.dao.helper.DatabaseHelper;
import nigel.com.werfleider.model.Contact;
import nigel.com.werfleider.model.Profession;

import static com.google.common.collect.Lists.newArrayList;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_CREATED_AT;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_EMAIL;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_ID;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_NAAM;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.KEY_PROFESSION;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.LOG;
import static nigel.com.werfleider.dao.helper.DatabaseHelperBean.TABLE_CONTACT;
import static org.joda.time.DateTime.now;

/**
 * Created by nigel on 11/02/15.
 */
public class ContactDbHelperBean implements ContactDbHelper {

    @Inject DatabaseHelper databaseHelper;

    @Override public long createContact(final Contact contact) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if(getContact(contact.getEmail()) != null){
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(KEY_NAAM, contact.getNaam());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_PROFESSION, contact.getProfession().name().toLowerCase());
        values.put(KEY_CREATED_AT, now().toString());

        // insert row

        return db.insert(TABLE_CONTACT, null, values);

    }

    @Override public Contact getContact(final String email) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACT + " WHERE "
                + KEY_EMAIL + " = '" + email +"'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c == null || !c.moveToNext()) {
            return null;
        }

        c.moveToFirst();

        return new Contact()
                .setId(c.getInt(c.getColumnIndex(KEY_ID)))
                .setNaam(c.getString(c.getColumnIndex(KEY_NAAM)))
                .setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)))
                .setProfession(Profession.valueOf(c.getString(c.getColumnIndex(KEY_PROFESSION)).toUpperCase()));
    }

    @Override public List<Contact> getAllContactsByProfession(final Profession profession) {
        List<Contact> contactList = newArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACT + " WHERE "
                + KEY_PROFESSION + " = '" + profession.name().toLowerCase() +"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Contact contact =
                        new Contact()
                                .setId(c.getInt(c.getColumnIndex(KEY_ID)))
                                .setNaam(c.getString(c.getColumnIndex(KEY_NAAM)))
                                .setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)))
                                .setProfession(Profession.valueOf(c.getString(c.getColumnIndex(KEY_PROFESSION)).toUpperCase()));

                // adding to todo list
                contactList.add(contact);
            } while (c.moveToNext());
        }

        return contactList;
    }

    @Override public int updateContact(final Contact contact) {
        return 0;
    }

    @Override public void closeDB() {

    }

    @Override public void deleteContact(final Contact contact) {

    }

    @Override public int getContactCount(final Profession profession) {
        String selectQuery = "SELECT * FROM " + TABLE_CONTACT + " WHERE "
                + KEY_PROFESSION + " = '" + profession.name().toLowerCase() +"'";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            return c.getCount();
        }

        return 0;
    }
}
