package domain.services.jobs;

import android.annotation.SuppressLint;
import android.support.annotation.WorkerThread;

import domain.DeleteLoanCollectionUseCase;
import domain.UpdateLoanCollectionUseCase;
import domain.services.SyncCommentResponse;
import domain.services.SyncCommentRxBus;
import domain.services.SyncResponseEventType;
import loans.model.LoanCollection;

public class SyncCommentResponseObserver {

    private final UpdateLoanCollectionUseCase updateCommentUseCase;
    private final DeleteLoanCollectionUseCase deleteCommentUseCase;

    public SyncCommentResponseObserver(UpdateLoanCollectionUseCase updateCommentUseCase,
                                       DeleteLoanCollectionUseCase deleteCommentUseCase) {
        this.updateCommentUseCase = updateCommentUseCase;
        this.deleteCommentUseCase = deleteCommentUseCase;
    }

    @SuppressLint("RxLeakedSubscription")
    void observeSyncResponse() {
        SyncCommentRxBus.getInstance().toObservable()
                .subscribe(this::handleSyncResponse,
                        t -> {
                        });
    }

    private void handleSyncResponse(SyncCommentResponse response) {
        if (response.eventType == SyncResponseEventType.SUCCESS) {
            onSyncCommentSuccess(response.loanCollection);
        } else {
            onSyncCommentFailed(response.loanCollection);
        }
    }

    @SuppressLint("RxLeakedSubscription")
    @WorkerThread
    private void onSyncCommentSuccess(LoanCollection comment) {
        updateCommentUseCase.updateComment(comment)
                .subscribe(() -> {
                        },
                        t -> {
                        });
    }

    @SuppressLint("RxLeakedSubscription")
    @WorkerThread
    private void onSyncCommentFailed(LoanCollection comment) {
        deleteCommentUseCase.deleteComment(comment)
                .subscribe(() -> {
                        },
                        t -> {
                        });
    }
}
