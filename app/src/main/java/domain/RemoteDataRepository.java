package domain;

import io.reactivex.Completable;
import loans.model.LoanCollection;

public interface RemoteDataRepository {
    Completable sync(LoanCollection comment);
}
