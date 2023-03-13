package loans;

import static networking.WebServiceURLs.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.client.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import Utilities.PreferenceConnector;
import domain.AddLoanCollectionUseCase;
import domain.GetLoanCollectionsUseCase;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import loans.model.LoanCollection;
import loans.model.ProfileCollection;
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
        String url = BASE_URL +
                WebServiceURLs.SAVE_CONTRACT_DATA_NEW +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");

        WebService.getInstance().apiPostJsonRequest(url, jsonArray, new WebService.OnServiceResponseListener() {
            @Override
            public void onApiCallResponseSuccess(String url, String object) {
                String message = null;
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    int status = jsonObject.getInt("status");
                    if (status == 200) {
                        message = jsonObject.getString("message");
                        int receiptID = jsonObject.getJSONObject("data").getJSONArray("portfolio")
                                .getJSONObject(0).getInt("receiptID");
                        loansFragmentCallback.onSaveContractData(message, receiptID, (int) totalAmount);
                        return;
                    }
                } catch (JSONException e) {
                    message = null;
                    Log.e("error", "error :" + e);
                    e.printStackTrace();
                }
                loansFragmentCallback.onSaveContractData(message, -1, (int) totalAmount);
                // remote call was successful--the Comment will be updated locally to reflect that sync is no longer pending
            }

            @Override
            public void onApiCallResponseFailure(String errorMessage) {
                loansFragmentCallback.onSaveContractData(null, -1, (int) totalAmount);
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
        String serviceUrl = WebServiceURLs.GET_EVENTS_BY_CONTRACTUUID_URL;
        String url = PreferenceConnector.readString(context, "BASE_URL", "") + serviceUrl +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "") + "&contractUUID=" + contractUUID + "&eventType";
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ProfileCollection>>() {
                        }.getType();
                        List<ProfileCollection> list = gson.fromJson(object, type);
                        loansFragmentCallback.hideProgressBar();
                        loansFragmentCallback.onGetLinkedProfile(null, list);
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
                        loansFragmentCallback.onSaveContractData(null, -1, totalAmount);
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
