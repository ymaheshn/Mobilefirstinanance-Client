package data;

import domain.RemoteDataRepository;
import domain.services.jobs.JobManagerFactory;
import domain.services.jobs.SyncCommentJob;
import io.reactivex.Completable;
import loans.model.LoanCollection;

public class RemoteDataStore implements RemoteDataRepository {

    @Override
    public Completable sync(LoanCollection loanCollection) {
        return Completable.fromAction(() ->
                JobManagerFactory.getJobManager().addJobInBackground(new SyncCommentJob(loanCollection)));
    }
}
