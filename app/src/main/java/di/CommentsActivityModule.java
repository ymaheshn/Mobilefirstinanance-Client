package di;

import dagger.Module;
import dagger.Provides;
import domain.AddLoanCollectionUseCase;
import domain.GetLoanCollectionsUseCase;
import domain.LocalDataRepository;
import domain.RemoteDataRepository;
import domain.SyncLoanCollectionUseCase;

/**
 * Define CommentsActivity-specific dependencies here.
 */
@Module
public class CommentsActivityModule {

    @Provides
    AddLoanCollectionUseCase provideAddLoanCollectionUseCase(LocalDataRepository localDataRepository, SyncLoanCollectionUseCase syncLoanCollectionUseCase) {
        return new AddLoanCollectionUseCase(localDataRepository, syncLoanCollectionUseCase);
    }

    @Provides
    GetLoanCollectionsUseCase provideGetLoanCollectionUseCase(LocalDataRepository localCommentRepository) {
        return new GetLoanCollectionsUseCase(localCommentRepository);
    }

    @Provides
    SyncLoanCollectionUseCase provideSyncLoanCollectionUseCase(RemoteDataRepository remoteDataRepository) {
        return new SyncLoanCollectionUseCase(remoteDataRepository);
    }
}
