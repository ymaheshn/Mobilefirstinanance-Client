package domain;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import loans.model.LoanCollection;

public interface LocalDataRepository {
    Single<LoanCollection> add(String contractCode, String loanAmount);

    Completable update(LoanCollection comment);

    Completable update(String whereCondition, String loanAmount);

    Completable delete(LoanCollection comment);

    Flowable<List<LoanCollection>> getComments(String contractCode);

    Flowable<List<LoanCollection>> getComments();
}
