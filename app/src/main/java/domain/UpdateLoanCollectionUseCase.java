package domain;

import io.reactivex.Completable;
import loans.model.LoanCollection;

public class UpdateLoanCollectionUseCase {
    private final LocalDataRepository localDataRepository;

    public UpdateLoanCollectionUseCase(LocalDataRepository localCommentRepository) {
        this.localDataRepository = localCommentRepository;
    }

    public Completable updateComment(LoanCollection comment) {
        return localDataRepository.update(comment);
    }

    public Completable updateComment(String whereCondition, String loanAmount) {
        return localDataRepository.update(whereCondition, loanAmount);
    }
}
