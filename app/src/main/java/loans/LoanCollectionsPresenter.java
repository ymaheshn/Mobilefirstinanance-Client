package loans;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import data.LoanCollectionDao;
import data.LoanCollectionDatabase;
import data.LoanCollectionUtils;
import domain.AddLoanCollectionUseCase;
import domain.GetLoanCollectionsUseCase;
import domain.services.SyncCommentRxBus;
import domain.services.SyncResponseEventType;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import loans.model.LoanCollection;
import loans.model.ProfileCollectionResponse;
import networking.WebService;
import networking.WebServiceURLs;

public class LoanCollectionsPresenter {

    private Context context;
    private LoanCollectionsFragmentCallback loansFragmentCallback;

    private boolean dontUpdateForFirstTime;


    private final CompositeDisposable disposables = new CompositeDisposable();
    private int totalAmount;

    LoanCollectionsPresenter(Context context, LoanCollectionsFragmentCallback loansFragmentCallback) {
        this.context = context;
        this.loansFragmentCallback = loansFragmentCallback;
    }

    void savePayment(double totalAmount, JSONArray jsonArray) {
        String url = PreferenceConnector.readString(context, "BASE_URL", "") +
                WebServiceURLs.SAVE_CONTRACT_DATA +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        WebService.getInstance().apiPostRequestCallJSONArray(url,
                jsonArray, new WebService.OnServiceResponseListener() {

                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        String message = null;
                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            if (jsonObject.getInt("status") == 200) {
                                message = jsonObject.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loansFragmentCallback.onSaveContractData(message, (int) totalAmount);
                        // remote call was successful--the Comment will be updated locally to reflect that sync is no longer pending
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        // throw new RemoteException("");
                        loansFragmentCallback.onSaveContractData(null, (int) totalAmount);
                    }
                });
    }

    void saveContractData(AddLoanCollectionUseCase collectionUseCase, String loanAmount, String contractCode) {
        Completable completable;
        if (totalAmount == 0) {
            completable = collectionUseCase.addComment(loanAmount, contractCode);
        } else {
            int completeCollection = totalAmount + Integer.parseInt(loanAmount);
            completable = collectionUseCase.updateComment(String.valueOf(completeCollection), contractCode);
        }
        Disposable disposable = completable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                }, throwable -> {

                });
        disposables.add(disposable);
    }

    public void getLinkedProfileDetails(String contractUUID) {
        loansFragmentCallback.showProgressBar();
        String serviceUrl = WebServiceURLs.GET_LINKED_PORTFOLIO_PROFILE_URL.replace("PROFILE_ID", contractUUID);
        String url = PreferenceConnector.readString(context, "BASE_URL", "") + serviceUrl +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        Gson gson = new Gson();
                        ProfileCollectionResponse profileCollectionResponse = gson.fromJson(object, ProfileCollectionResponse.class);
                        loansFragmentCallback.hideProgressBar();
                        loansFragmentCallback.onGetLinkedProfile(null, profileCollectionResponse);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        loansFragmentCallback.hideProgressBar();
                        loansFragmentCallback.showMessage(errorMessage);
                    }
                });
    }

    void getPortfolioData(GetLoanCollectionsUseCase getLoanCollectionsUseCase, String contractCode) {
        Flowable<List<LoanCollection>> loanData = getLoanCollectionsUseCase.getComments(contractCode);
        disposables.add(loanData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<LoanCollection>>() {
            @Override
            public void accept(List<LoanCollection> loanCollections) throws Exception {
                totalAmount = 0;
                if (loanCollections != null && loanCollections.size() > 0) {
                    for (LoanCollection loanCollection : loanCollections) {
                        totalAmount = totalAmount + Integer.parseInt(loanCollection.getLoanAmount());
                    }
                    if (dontUpdateForFirstTime) {
                        loansFragmentCallback.onSaveContractData(null, totalAmount);
                    }
                    dontUpdateForFirstTime = true;
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
//                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
