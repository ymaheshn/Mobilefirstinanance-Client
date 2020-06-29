package domain;

import io.reactivex.Completable;

public class AddLoanCollectionUseCase {
    private final LocalDataRepository localCommentRepository;
    private final SyncLoanCollectionUseCase syncCommentUseCase;

    public AddLoanCollectionUseCase(LocalDataRepository localDataRepository, SyncLoanCollectionUseCase syncLoanCollectionUseCase) {
        this.localCommentRepository = localDataRepository;
        this.syncCommentUseCase = syncLoanCollectionUseCase;
    }

    public Completable addComment(String loanAmount, String contractCode) {
        return localCommentRepository.add(contractCode, loanAmount)
                .flatMapCompletable(syncCommentUseCase::syncComment);
    }

    public Completable updateComment(String loanAmount, String contractCode) {
        return localCommentRepository.update(contractCode, loanAmount);
    }
}
