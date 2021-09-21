package credit_score;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import networking.WebService;
import networking.WebServiceURLs;

/**
 * Created by gufran khan on 20-10-2018.
 */

public class CreditScorePresenter {
    private Context context;
    private ICreditScoreCallback iCreditScoreCallback;

    public CreditScorePresenter(Context context, ICreditScoreCallback iCreditScoreCallback) {
        this.context = context;
        this.iCreditScoreCallback = iCreditScoreCallback;
    }

    public void getCreditScoreData(String creditScoreId, String workFlowProfileId) {
        if (UtilityMethods.isNetworkAvailable(context)) {
            iCreditScoreCallback.showProgressBar();
            String url = PreferenceConnector.readString(context, "BASE_URL", "") +
                    WebServiceURLs.CREDIT_BUREAU_GET_INFO_URL+
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");

            url = url.replace("WORKFLOW_PROFILE_ID", workFlowProfileId);
            url = url.replace("TEMPLATE_ID", creditScoreId);
            WebService.getInstance().apiGetRequestCall(url,

                    new WebService.OnServiceResponseListener() {
                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iCreditScoreCallback.hideProgressBar();
                            if (!TextUtils.isEmpty(object)) {
                                try {
                                    JSONArray jsonArray = new JSONArray(object);
                                    if (jsonArray.length() > 0) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String templateDetailsId = "";
                                        if (jsonObject.has("templateDetailsID")) {
                                            templateDetailsId = jsonObject.getString("templateDetailsID");
                                        }
                                        if (jsonObject.has("workflowTemplateDetails")) {
                                            iCreditScoreCallback.getCreditScoreData(true,jsonObject.getJSONObject("workflowTemplateDetails").toString(),templateDetailsId);
                                        }
                                    }else {
                                        iCreditScoreCallback.getCreditScoreData(false, null, "");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    iCreditScoreCallback.showMessage("Something went wrong.");
                                }
                            }
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iCreditScoreCallback.hideProgressBar();
                            if (errorMessage.contains("AuthFailureError")) {
                                iCreditScoreCallback.showAlert();
                            } else {
                                iCreditScoreCallback.showMessage(errorMessage);
                            }

                        }
                    });
        }
    }

    public void saveCreditScoreData(String string) {
        if (UtilityMethods.isNetworkAvailable(context)) {
            iCreditScoreCallback.showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    string, new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );
            String url = PreferenceConnector.readString(context, "BASE_URL", "") +
                    WebServiceURLs.CREDIT_BUREAU_POST_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            WebService.getInstance().apiPostRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iCreditScoreCallback.hideProgressBar();
                            if (!TextUtils.isEmpty(object)) {

                                try {
                                    JSONObject jsonObject = new JSONObject(object);
                                    if (jsonObject.has("message")) {
                                        iCreditScoreCallback.showMessage(jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            //iCreditBureauCallback.showMessage("Data saved successfully");
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iCreditScoreCallback.hideProgressBar();
                            iCreditScoreCallback.showMessage(errorMessage);
                        }
                    });
        } else {
            iCreditScoreCallback.showMessage("Please check your internet");
        }
    }
    public void updateCreditScoreData(String s, String templateDetialsId) {
        //ClientDataDAO.getInstance().insertOrUpdateClientKyc(DataBaseContext.getDBObject(1), jsonObject, clientId);
        if (UtilityMethods.isNetworkAvailable(context)) {
            iCreditScoreCallback.showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    s, new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );
            String url = WebServiceURLs.BASE_URL +
                    WebServiceURLs.CASH_FLOW_UPDATE_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            url = url.replace("WORKFLOW_PROFILE_ID", templateDetialsId);
            WebService.getInstance().apiPutRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iCreditScoreCallback.hideProgressBar();
                            iCreditScoreCallback.showMessage("Credit Score updated successfully");
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iCreditScoreCallback.hideProgressBar();
                            iCreditScoreCallback.showMessage(errorMessage);
                        }
                    });
        } else {
            //iKycFragmentCallback.showMessage("Kyc data has been saved successfully in offline");
        }

    }
}
