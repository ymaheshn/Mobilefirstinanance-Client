package database.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by rajesh on 13/10/16.
 */

public class DataBaseContext {
    private static DBOperations dbOperations;

    /**
     * function used to setDbOperations
     *
     * @param dbOperations dbOperations
     */
    public static void setDbOperations(DBOperations dbOperations) {
        DataBaseContext.dbOperations = dbOperations;
    }

    /**
     * function used to getDBObject
     *
     * @param isWritable permissions
     * @return SQLiteDatabase
     */
    public static SQLiteDatabase getDBObject(int isWritable) {
        return dbOperations.getDBOject(isWritable);
    }
}
