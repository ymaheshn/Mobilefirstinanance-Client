package data;


import java.util.List;

import domain.LocalDataRepository;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import loans.model.LoanCollection;

public class LocalDataStore implements LocalDataRepository {

    private final LoanCollectionDao loanCollectionDao;

    public LocalDataStore(LoanCollectionDao commentDao) {
        this.loanCollectionDao = commentDao;
    }

    /**
     * Adds a comment to a given photo
     */
    public Single<LoanCollection> add(String contractCode, String loanAmount) {
        LoanCollection loanCollection = new LoanCollection(contractCode, loanAmount);

        return Single.fromCallable(() -> {
            long rowId = loanCollectionDao.add(loanCollection);
            return LoanCollectionUtils.clone(loanCollection, rowId);
        });
    }

    @Override
    public Completable update(LoanCollection comment) {
        return Completable.fromAction(() -> loanCollectionDao.update(comment));
    }

    @Override
    public Completable update(String whereCondition, String loanAmount) {
        return Completable.fromAction(() -> loanCollectionDao.update(whereCondition, loanAmount));
    }

    @Override
    public Completable delete(LoanCollection comment) {
        return null;
    }

    @Override
    public Flowable<List<LoanCollection>> getComments(String contactCode) {
        return loanCollectionDao.getComments(contactCode);
    }

    @Override
    public Flowable<List<LoanCollection>> getComments() {
        return loanCollectionDao.getComments();
    }

}