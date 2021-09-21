package credit_bureau;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

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
import kyc.IKycFragmentCallback;
import networking.WebService;
import networking.WebServiceURLs;

/**
 * Created by gufran khan on 20-10-2018.
 */

public class CreditBureauPresenter {
    private Context context;
    private ICreditBureauCallback iCreditBureauCallback;

    public CreditBureauPresenter(Context context, ICreditBureauCallback iCreditBureauCallback) {
        this.context = context;
        this.iCreditBureauCallback = iCreditBureauCallback;
    }

    public void saveCreditBureauData(String jsonObject) {
        //ClientDataDAO.getInstance().insertOrUpdateClientKyc(DataBaseContext.getDBObject(1), jsonObject, clientId);
        if (UtilityMethods.isNetworkAvailable(context)) {
            iCreditBureauCallback.showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    jsonObject, new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );
            String url = PreferenceConnector.readString(context, "BASE_URL", "") +
                    WebServiceURLs.CREDIT_BUREAU_POST_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            WebService.getInstance().apiPostRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iCreditBureauCallback.hideProgressBar();
                            if (!TextUtils.isEmpty(object)) {

                                try {
                                    JSONObject jsonObject = new JSONObject(object);
                                    if (jsonObject.has("message")) {
                                        iCreditBureauCallback.showMessage(jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            //iCreditBureauCallback.showMessage("Data saved successfully");
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iCreditBureauCallback.hideProgressBar();
                            iCreditBureauCallback.showMessage(errorMessage);
                        }
                    });
        } else {
            iCreditBureauCallback.showMessage("Please check your internet");
        }

    }

    public void getCreditBureauData(String workFlowId, String workFlowProfileId) {
        if (UtilityMethods.isNetworkAvailable(context)) {
            iCreditBureauCallback.showProgressBar();
            String url = PreferenceConnector.readString(context, "BASE_URL", "") +
                    WebServiceURLs.CREDIT_BUREAU_GET_INFO_URL+
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");

            url = url.replace("WORKFLOW_PROFILE_ID", workFlowProfileId);
            url = url.replace("TEMPLATE_ID", workFlowId);
            WebService.getInstance().apiGetRequestCall(url,

                    new WebService.OnServiceResponseListener() {
                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iCreditBureauCallback.hideProgressBar();
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
                                            iCreditBureauCallback.getCreditBureauData(true,jsonObject.getJSONObject("workflowTemplateDetails").toString(),templateDetailsId);
                                        }
                                    }else {
                                        iCreditBureauCallback.getCreditBureauData(false, null, "");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    iCreditBureauCallback.showMessage("Something went wrong.");
                                }
                            }
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iCreditBureauCallback.hideProgressBar();
                            if (errorMessage.contains("AuthFailureError")) {
                                iCreditBureauCallback.showAlert();
                            } else {
                                iCreditBureauCallback.showMessage(errorMessage);
                            }

                        }
                    });
        }
    }
    public void updateCreditBureauData(String s, String templateDetialsId) {
        //ClientDataDAO.getInstance().insertOrUpdateClientKyc(DataBaseContext.getDBObject(1), jsonObject, clientId);
        if (UtilityMethods.isNetworkAvailable(context)) {
            iCreditBureauCallback.showProgressBar();

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
                            iCreditBureauCallback.hideProgressBar();
                            iCreditBureauCallback.showMessage("Credit Bureau updated successfully");
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iCreditBureauCallback.hideProgressBar();
                            iCreditBureauCallback.showMessage(errorMessage);
                        }
                    });
        } else {
            //iKycFragmentCallback.showMessage("Kyc data has been saved successfully in offline");
        }

    }
}
