package domain;

import io.reactivex.Completable;
import loans.model.LoanCollection;

public class DeleteLoanCollectionUseCase {
    private final LocalDataRepository localDataRepository;

    public DeleteLoanCollectionUseCase(LocalDataRepository localCommentRepository) {
        this.localDataRepository = localCommentRepository;
    }

    public Completable deleteComment(LoanCollection comment) {
        return localDataRepository.delete(comment);
    }
}
