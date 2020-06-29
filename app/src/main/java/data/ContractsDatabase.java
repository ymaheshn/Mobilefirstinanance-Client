package data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import Utilities.Constants;
import loans.model.Datum;

@Database(entities = {Datum.class}, version = 1, exportSchema = false)
public abstract class ContractsDatabase extends RoomDatabase {
    private static ContractsDatabase instance;

    public static synchronized ContractsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(context.getApplicationContext(), ContractsDatabase.class,
                            Constants.DatabaseConstants.DATABASE_CONTRACTS_NAME)
                    .build();
        }
        return instance;
    }

    public abstract ContractsDao daoAccess();
}
