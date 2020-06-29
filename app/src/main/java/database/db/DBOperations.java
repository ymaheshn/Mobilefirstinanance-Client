package database.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by siva on 10/13/2016.
 */

public class DBOperations extends SQLiteOpenHelper {

    public static final String TABLE_CLIENT_DATA = "client_data";
    public static final String TABLE_FORM_DATA = "form_data";
    public static final String DATABASE_NAME = "mff.db";
    private static final int DATABASE_VERSION = 2;
    private static DBOperations dbOperations;

    private final String TABLE_CLIENT_DATA_CREATE_QUERY = "CREATE TABLE " + TABLE_CLIENT_DATA + " (" +
            "client_id               INTEGER PRIMARY KEY AUTOINCREMENT," +
            "kyc_data                    TEXT DEFAULT ''" +
            ");";


    private final String TABLE_FORM_DATA_CREATE_QUERY = "CREATE TABLE " + TABLE_FORM_DATA + " (" +
            "form_id                  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "json_data                TEXT DEFAULT ''" +
            ");";


    /**
     * public constructor for Database Operations
     */
    public DBOperations(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Returns database singleton instance
     *
     * @param context Context or Activity
     * @return DBOperations Instance
     */
    public static DBOperations getInstance(Context context) {
        if (dbOperations == null) {
            dbOperations = new DBOperations(context, DATABASE_NAME, null, DATABASE_VERSION);
            DataBaseContext.setDbOperations(dbOperations);
        }
        return dbOperations;
    }

    /**
     * method to get Database Object
     *
     * @param isWritable write permisssions
     * @return SQLiteDatabase object
     */
    public synchronized SQLiteDatabase getDBOject(int isWritable) {
        return (isWritable == 1) ? this.getWritableDatabase() : this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CLIENT_DATA_CREATE_QUERY);
        db.execSQL(TABLE_FORM_DATA_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM_DATA);

        // Create tables again
        onCreate(db);
    }
}
