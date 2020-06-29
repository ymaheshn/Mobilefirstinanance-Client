package data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import loans.model.Datum;

@Dao
public interface ContractsDao {
    @Query("SELECT * FROM Datum")
    List<Datum> getContracts();

    @Delete
    void deleteDatum(Datum datum);

    @Insert
    Long insertDatum(Datum datum);

    @Query("DELETE FROM Datum")
    public void deleteAll();

}
