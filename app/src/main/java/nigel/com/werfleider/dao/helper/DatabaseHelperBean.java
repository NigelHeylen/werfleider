package nigel.com.werfleider.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

/**
 * Created by nigel on 07/02/15.
 */
public class DatabaseHelperBean extends SQLiteOpenHelper implements DatabaseHelper {

    // Logcat tag
    public static final String LOG = "DatabaseHelper";

    // Database Version
    public static final int DATABASE_VERSION = 10;

    // Database Name
    public static final String DATABASE_NAME = "werfManager";

    // Table Names
    public static final String TABLE_WERF = "werf";
    public static final String TABLE_DOCUMENT = "document";
    public static final String TABLE_DOCUMENT_LOCATIE = "document_locatie";
    public static final String TABLE_DOCUMENT_LOCATIE_IMAGE = "document_locatie_image";
    public static final String TABLE_CONTACT = "contact";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    // WERF Table - column names
    public static final String KEY_NUMMER = "nummer";
    public static final String KEY_NAAM = "naam";
    public static final String KEY_OPDRACHTGEVER = "opdrachtgever";
    public static final String KEY_OPDRACHTGEVER_ADRES = "opdrachtgever_adres";
    public static final String KEY_OPDRACHTGEVER_STAD = "opdrachtgever_stad";
    public static final String KEY_DATUM_AANVANG = "datum_aanvang";
    public static final String KEY_OPDRACHT_ADRES = "opdracht_adres";
    public static final String KEY_OPDRACHT_STAD = "opdracht_stad";
    public static final String KEY_ONTWERPER = "ontwerper";
    public static final String KEY_ONTWERPER_STAD = "ontwerper_stad";
    public static final String KEY_ONTWERPER_ADRES = "ontwerper_adres";


    // DOCUMENT Table - column names
    public static final String KEY_WERF_ID = "werf_id";
    public static final String KEY_DOCUMENT_TYPE = "document_type";

    // DOCUMENT_LOCATIE Table - column names
    public static final String KEY_DOCUMENT_ID = "document_id";
    public static final String KEY_LOCATIE = "locatie";


    // DOCUMENT_LOCATIE_IMAGE Table - column names
    public static final String KEY_DOCUMENT_LOCATIE_ID = "document_locatie_id";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_OMSCHRIJVING = "omschrijving";

    // CONTACT Table - column names
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PROFESSION = "profession";

    // Table Create Statements
    // Werf table create statement
    public static final String CREATE_TABLE_WERF = "CREATE TABLE "
            + TABLE_WERF + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_NUMMER + " TEXT," +
            KEY_NAAM + " TEXT," +
            KEY_OPDRACHT_ADRES + " TEXT," +
            KEY_OPDRACHT_STAD + " TEXT," +
            KEY_OPDRACHTGEVER + " TEXT," +
            KEY_OPDRACHTGEVER_ADRES + " TEXT," +
            KEY_OPDRACHTGEVER_STAD + " TEXT," +
            KEY_ONTWERPER + " TEXT," +
            KEY_ONTWERPER_STAD + " TEXT," +
            KEY_ONTWERPER_ADRES + " TEXT," +
            KEY_OMSCHRIJVING + " TEXT," +
            KEY_DATUM_AANVANG + " DATETIME," +
            KEY_CREATED_AT + " DATETIME" + ")";

    // Plaatsbeschrijf table create statement
    public static final String CREATE_TABLE_DOCUMENT
            = "CREATE TABLE " + TABLE_DOCUMENT
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DOCUMENT_TYPE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_WERF_ID + " INTEGER" + ")";

    // Plaatsbeschrijf locatie table create statement
    public static final String CREATE_TABLE_DOCUMENT_LOCATIE = "CREATE TABLE "
            + TABLE_DOCUMENT_LOCATIE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DOCUMENT_ID + " INTEGER,"
            + KEY_LOCATIE + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // Plaatsbeschrijf locatie image table create statement
    public static final String CREATE_TABLE_DOCUMENT_LOCATIE_IMAGE = "CREATE TABLE "
            + TABLE_DOCUMENT_LOCATIE_IMAGE + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DOCUMENT_LOCATIE_ID + " INTEGER,"
            + KEY_IMAGE_URL + " TEXT,"
            + KEY_OMSCHRIJVING + " TEXT" + ")";


    // Plaatsbeschrijf locatie image table create statement
    public static final String CREATE_TABLE_CONTACT = "CREATE TABLE "
            + TABLE_CONTACT + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CREATED_AT + " DATETIME,"
            + KEY_NAAM + " TEXT,"
            + KEY_PROFESSION + " TEXT,"
            + KEY_EMAIL + " TEXT" + ")";

    @Inject
    public DatabaseHelperBean(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override public void onCreate(final SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_WERF);
        db.execSQL(CREATE_TABLE_DOCUMENT);
        db.execSQL(CREATE_TABLE_DOCUMENT_LOCATIE);
        db.execSQL(CREATE_TABLE_DOCUMENT_LOCATIE_IMAGE);
        db.execSQL(CREATE_TABLE_CONTACT);


    }

    @Override public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WERF);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT_LOCATIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENT_LOCATIE_IMAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);

        onCreate(db);

    }


    // closing database
    @Override
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
