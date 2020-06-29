package data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import loans.model.LoanCollection;

@Dao
public interface LoanCollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(LoanCollection comment);

    @Query("UPDATE loancollection SET loan_amount = :loanAmount WHERE contract_code = :whereCondition")
    void update(String whereCondition, String loanAmount);

    @Update
    void update(LoanCollection loanCollection);

    @Query("SELECT * FROM loancollection WHERE contract_code = :contractCode ORDER BY timestamp DESC")
    Flowable<List<LoanCollection>> getComments(String contractCode);

    @Query("SELECT * FROM loancollection ORDER BY timestamp DESC")
    Flowable<List<LoanCollection>> getComments();
}
