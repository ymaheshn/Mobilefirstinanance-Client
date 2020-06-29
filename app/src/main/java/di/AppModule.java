package di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import data.LoanCollectionDao;
import data.LoanCollectionDatabase;
import data.LocalDataStore;
import data.RemoteDataStore;
import domain.DeleteLoanCollectionUseCase;
import domain.LocalDataRepository;
import domain.RemoteDataRepository;
import domain.UpdateLoanCollectionUseCase;
import domain.services.jobs.GcmJobService;
import domain.services.jobs.SchedulerJobService;
import domain.services.jobs.SyncCommentJobManagerInitializer;
import domain.services.jobs.SyncCommentResponseObserver;
import networking.MyApplication;

/**
 * This is where you will inject application-wide dependencies.
 */
@Module
public class AppModule {

    @Provides
    Context provideContext(MyApplication application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    SchedulerJobService provideSchedulerJobService() {
        return new SchedulerJobService();
    }

    @Singleton
    @Provides
    GcmJobService provideGcmJobService() {
        return new GcmJobService();
    }

    @Singleton
    @Provides
    LoanCollectionDao provideCommentDao(Context context) {
        return LoanCollectionDatabase.getInstance(context).commentDao();
    }

    @Singleton
    @Provides
    LocalDataRepository provideLocalCommentRepository(LoanCollectionDao commentDao) {
        return new LocalDataStore(commentDao);
    }

    @Singleton
    @Provides
    RemoteDataRepository provideRemoteCommentRepository() {
        return new RemoteDataStore();
    }

    @Singleton
    @Provides
    SyncCommentResponseObserver provideSyncCommentResponseObserver(UpdateLoanCollectionUseCase updateCommentUseCase, DeleteLoanCollectionUseCase deleteCommentUseCase) {
        return new SyncCommentResponseObserver(updateCommentUseCase, deleteCommentUseCase);
    }

    @Singleton
    @Provides
    UpdateLoanCollectionUseCase provideUpdateCommentUseCase(LocalDataRepository localCommentRepository) {
        return new UpdateLoanCollectionUseCase(localCommentRepository);
    }

    @Singleton
    @Provides
    DeleteLoanCollectionUseCase provideDeleteCommentUseCase(LocalDataRepository localCommentRepository) {
        return new DeleteLoanCollectionUseCase(localCommentRepository);
    }

    @Singleton
    @Provides
    SyncCommentJobManagerInitializer provideSyncCommentJobManagerInitializer(SyncCommentResponseObserver syncCommentResponseObserver) {
        return new SyncCommentJobManagerInitializer(syncCommentResponseObserver);
    }
}
