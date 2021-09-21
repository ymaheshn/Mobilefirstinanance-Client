package cashflow;

import android.content.Context;
import android.os.AsyncTask;
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

public class CashFlowPresenter implements WebService.OnServiceResponseListener {

    private Context context;
    private ICashFlowCallBacks icashFlowCallBacks;

    public CashFlowPresenter(Context context, ICashFlowCallBacks icashFlowCallBacks) {
        this.context = context;
        this.icashFlowCallBacks = icashFlowCallBacks;
    }

    @Override
    public void onApiCallResponseSuccess(String url, String object) {
        icashFlowCallBacks.hideProgressBar();

    }

    @Override
    public void onApiCallResponseFailure(String errorMessage) {
        icashFlowCallBacks.hideProgressBar();
        if (errorMessage.contains("AuthFailureError")) {
            icashFlowCallBacks.showAlert();
        } else {
            icashFlowCallBacks.showMessage("Something went wrong! Please try after sometime");
        }
    }

    public void saveCashFlowData(String s) {
        if (UtilityMethods.isNetworkAvailable(context)) {
            icashFlowCallBacks.showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    s, new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );
            String url = WebServiceURLs.BASE_URL +
                    WebServiceURLs.CASH_FLOW_POST_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            WebService.getInstance().apiPostRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            icashFlowCallBacks.hideProgressBar();
                            if (!TextUtils.isEmpty(object)) {

                                try {
                                    JSONObject jsonObject = new JSONObject(object);
                                    if (jsonObject.has("message")) {
                                        icashFlowCallBacks.showMessage(jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            icashFlowCallBacks.hideProgressBar();
                            icashFlowCallBacks.showMessage(errorMessage);
                        }
                    });
        }
    }

    public void getCashFlowData(String workFlowId, String workFlowProfileId) {
        if (UtilityMethods.isNetworkAvailable(context)) {
            icashFlowCallBacks.showProgressBar();
            String url = WebServiceURLs.BASE_URL +
                    WebServiceURLs.CASH_FLOW_GET_INFO_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");

            url = url.replace("WORKFLOW_PROFILE_ID", workFlowProfileId);
            url = url.replace("TEMPLATE_ID", workFlowId);
            WebService.getInstance().apiGetRequestCall(url,

                    new WebService.OnServiceResponseListener() {
                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            icashFlowCallBacks.hideProgressBar();
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
                                            icashFlowCallBacks.getCashFlowData(true, jsonObject.getJSONObject("workflowTemplateDetails").toString(), templateDetailsId);
                                        }
                                    } else {
                                        icashFlowCallBacks.getCashFlowData(false, null, "");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    icashFlowCallBacks.showMessage("Something went wrong.");
                                }
                            }
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            icashFlowCallBacks.hideProgressBar();
                            if (errorMessage.contains("AuthFailureError")) {
                                icashFlowCallBacks.showAlert();
                            } else {
                                icashFlowCallBacks.showMessage(errorMessage);
                            }

                        }
                    });
        }
    }

    public void updateCashFlowData(String s, String templateDetialsId) {
        //ClientDataDAO.getInstance().insertOrUpdateClientKyc(DataBaseContext.getDBObject(1), jsonObject, clientId);
        if (UtilityMethods.isNetworkAvailable(context)) {
            icashFlowCallBacks.showProgressBar();

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
                            icashFlowCallBacks.hideProgressBar();
                            icashFlowCallBacks.showMessage("Cash flow updated successfully");
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            icashFlowCallBacks.hideProgressBar();
                            icashFlowCallBacks.showMessage(errorMessage);
                        }
                    });
        } else {
            //iKycFragmentCallback.showMessage("Kyc data has been saved successfully in offline");
        }

    }


}
