package data;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import Utilities.Constants;
import loans.model.LoanCollection;

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
