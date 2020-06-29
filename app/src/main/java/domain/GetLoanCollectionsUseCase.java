package domain;

import java.util.List;

import io.reactivex.Flowable;
import loans.model.LoanCollection;

public class GetLoanCollectionsUseCase {
    private final LocalDataRepository localDataRepository;

    public GetLoanCollectionsUseCase(LocalDataRepository localCommentRepository) {
        this.localDataRepository = localCommentRepository;
    }

    public Flowable<List<LoanCollection>> getComments(String contractCode) {
        return localDataRepository.getComments(contractCode);
    }
    public Flowable<List<LoanCollection>> getComments() {
        return localDataRepository.getComments();
    }

}
