package domain;

import io.reactivex.Completable;
import loans.model.LoanCollection;

/**
 * Responsible for syncing a comment with remote repository.
 */
public class SyncLoanCollectionUseCase {
    private final RemoteDataRepository remoteCommentRepository;

    public SyncLoanCollectionUseCase(RemoteDataRepository remoteDataRepository) {
        this.remoteCommentRepository = remoteDataRepository;
    }

    public Completable syncComment(LoanCollection loanCollection) {
        return remoteCommentRepository.sync(loanCollection);
    }
}
