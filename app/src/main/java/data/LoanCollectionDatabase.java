package data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import Utilities.Constants;
import loans.model.LoanCollection;

@Database(entities = {LoanCollection.class}, version = 1, exportSchema = false)
public abstract class LoanCollectionDatabase extends RoomDatabase {
    private static LoanCollectionDatabase instance;

    public static synchronized LoanCollectionDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), LoanCollectionDatabase.class, Constants.DatabaseConstants.DATABASE_NAME)
                    .build();
        }
        return instance;
    }

    public abstract LoanCollectionDao commentDao();
}
