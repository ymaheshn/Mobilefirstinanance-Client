package domain.services.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.odedtech.mff.mffapp.R;

import java.util.HashMap;
import java.util.Map;

import Utilities.PreferenceConnector;
import data.LoanCollectionUtils;
import domain.services.SyncCommentRxBus;
import domain.services.SyncResponseEventType;
import domain.services.networking.RemoteCommentService;
import domain.services.networking.RemoteException;
import loans.model.LoanCollection;
import networking.WebService;
import networking.WebServiceURLs;

public class SyncCommentJob extends Job {

    private static final String TAG = SyncCommentJob.class.getCanonicalName();
    private final LoanCollection comment;

    public SyncCommentJob(LoanCollection comment) {
        super(new Params(JobPriority.MID)
                .requireNetwork()
                .groupBy(TAG)
                .persist());
        this.comment = comment;
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        // if any exception is thrown, it will be handled by shouldReRunOnThrowable()
//        RemoteCommentService.getInstance().addComment(getApplicationContext(), comment);

        Map<String, String> params = new HashMap<>();
        params.put("contractCode", comment.getContractCode());
        params.put("amount", comment.getLoanAmount());
        String url = PreferenceConnector.readString(getApplicationContext(), "BASE_URL", "") +
                WebServiceURLs.SAVE_CONTRACT_DATA +
                PreferenceConnector.readString(getApplicationContext(), getApplicationContext().getString(R.string.accessToken), "");
        WebService.getInstance().apiPostRequestCall(url,
                params, new WebService.OnServiceResponseListener() {

                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        // remote call was successful--the Comment will be updated locally to reflect that sync is no longer pending
//                        LoanCollection updatedComment = LoanCollectionUtils.clone(comment, false);
//                        SyncCommentRxBus.getInstance().post(SyncResponseEventType.SUCCESS, updatedComment);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        // throw new RemoteException("");
                    }
                });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        // sync to remote failed
        SyncCommentRxBus.getInstance().post(SyncResponseEventType.FAILED, comment);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable instanceof RemoteException) {
            RemoteException exception = (RemoteException) throwable;

            int statusCode = exception.getResponse().code();
            if (statusCode >= 400 && statusCode < 500) {
                return RetryConstraint.CANCEL;
            }
        }
        // if we are here, most likely the connection was lost during job execution
        return RetryConstraint.RETRY;
    }
}
