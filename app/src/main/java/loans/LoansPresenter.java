package loans;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.mffapp.R;

import java.lang.reflect.Type;
import java.util.List;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import data.ContractsDatabase;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import loans.model.ProfileCollection;
import networking.WebService;
import networking.WebServiceURLs;

public class LoansPresenter implements WebService.OnServiceResponseListener {
    private Context context;
    private LoansFragmentCallback loansFragmentCallback;
    private final ContractsDatabase contractsDatabase;

    public LoansPresenter(Context context, LoansFragmentCallback loansFragmentCallback) {
        this.context = context;
        this.loansFragmentCallback = loansFragmentCallback;
        contractsDatabase = ContractsDatabase.getInstance(context);
        getLinkedProfiles();
    }


    public void getLinkedProfiles() {
        loansFragmentCallback.showProgressBar();

        if (!UtilityMethods.isNetworkAvailable(context)) {
            new Thread(() -> {
                List<Datum> data = contractsDatabase.daoAccess().getContracts();
                ((Activity) context).runOnUiThread(() -> {
                    if (data != null && data.size() > 0) {
                        LinkedProfilesResponse profilesResponse = new LinkedProfilesResponse();
                        profilesResponse.data = data;
                        loansFragmentCallback.loadRecyclerView(profilesResponse);
                        loansFragmentCallback.hideProgressBar();
                    }
                });
            }).start();
        } else {
            String url = PreferenceConnector.readString(context, "BASE_URL", "") +
                    WebServiceURLs.GET_LINKED_PORTFOLIO_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            WebService.getInstance().apiGetRequestCall(url,
                    new WebService.OnServiceResponseListener() {
                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {

                            Gson gson = new Gson();
                            LinkedProfilesResponse profilesResponse = gson.fromJson(object, LinkedProfilesResponse.class);
                            new Thread(() -> {
                                contractsDatabase.daoAccess().deleteAll();
                                for (Datum datum : profilesResponse.data) {
                                    contractsDatabase.daoAccess().insertDatum(datum);
                                }
                                ((Activity) context).runOnUiThread(() -> {
                                    loansFragmentCallback.loadRecyclerView(profilesResponse);
                                    loansFragmentCallback.hideProgressBar();
                                });
                            }).start();
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            loansFragmentCallback.hideProgressBar();
                            if (!TextUtils.isEmpty(errorMessage) && errorMessage.contains("AuthFailureError")) {
                                loansFragmentCallback.showLogoutAlert();
                            } else {
                                loansFragmentCallback.showMessage(errorMessage);
                            }
                        }
                    });
        }
    }


    public void getLinkedProfileDetails(Datum datum) {
        loansFragmentCallback.showProgressBar();

        String serviceUrl = WebServiceURLs.GET_EVENTS_BY_CONTRACTUUID_URL;
        String url = PreferenceConnector.readString(context, "BASE_URL", "") + serviceUrl + PreferenceConnector.readString(context, context.getString(R.string.accessToken), "") + "&contractUUID=" + datum.contractUUID+"&eventType";
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<ProfileCollection>>(){}.getType();
                        List<ProfileCollection> list = gson.fromJson(object, type);
                        loansFragmentCallback.hideProgressBar();
                        loansFragmentCallback.linkedProfileCollection(datum, list);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        Log.e("errorMessage", "errorMessage :" + errorMessage);
                    }
                });


//        String serviceUrl = WebServiceURLs.GET_LINKED_PORTFOLIO_PROFILE_URL.replace("PROFILE_ID", datum.contractUUID);
//        String url = PreferenceConnector.readString(context, "BASE_URL", "") + serviceUrl +
//                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
//        WebService.getInstance().apiGetRequestCall(url,
//                new WebService.OnServiceResponseListener() {
//                    @Override
//                    public void onApiCallResponseSuccess(String url, String object) {
//                        Gson gson = new Gson();
//                        ProfileCollectionResponse profileCollectionResponse = gson.fromJson(object, ProfileCollectionResponse.class);
////                        new Thread(() -> {
////                            contractsDatabase.daoAccess().deleteAll();
////                            for (Datum datum : profilesResponse.data) {
////                                contractsDatabase.daoAccess().insertDatum(datum);
////                            }
////                            ((Activity) context).runOnUiThread(() -> {
////                                loansFragmentCallback.loadRecyclerView(profilesResponse);
//                        loansFragmentCallback.hideProgressBar();
//                        loansFragmentCallback.linkedProfileCollection(datum, profileCollectionResponse);
////                            });
////                        }).start();
//                    }
//
//                    @Override
//                    public void onApiCallResponseFailure(String errorMessage) {
//                        loansFragmentCallback.hideProgressBar();
//                        loansFragmentCallback.showMessage(errorMessage);
////                        if (!TextUtils.isEmpty(errorMessage) && errorMessage.contains("AuthFailureError")) {
////                            loansFragmentCallback.showLogoutAlert();
////                        } else {
////                            loansFragmentCallback.showMessage(errorMessage);
////                        }
//                    }
//                });
    }

    @Override
    public void onApiCallResponseSuccess(String url, String object) {

    }

    @Override
    public void onApiCallResponseFailure(String errorMessage) {

    }

}
