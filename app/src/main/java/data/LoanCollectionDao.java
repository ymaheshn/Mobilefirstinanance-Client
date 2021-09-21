package data;

import java.util.List;

import io.reactivex.Flowable;
import loans.model.LoanCollection;

public interface LoanCollectionDao {
    long add(LoanCollection comment);

    void update(String whereCondition, String loanAmount);

    void update(LoanCollection loanCollection);

    Flowable<List<LoanCollection>> getComments(String contractCode);

    Flowable<List<LoanCollection>> getComments();
}
