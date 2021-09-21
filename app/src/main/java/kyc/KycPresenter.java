package kyc;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import database.dao.ClientFormsDAO;
import database.db.DataBaseContext;
import kyc.dto.KycDynamicForm;
import kyc.dto.ProfileForm;
import login.LoginActivity;
import networking.WebService;
import networking.WebServiceURLs;

/**
 * Created by gufran khan on 28-07-2018.
 */

public class KycPresenter {
    private Context context;
    private IKycFragmentCallback iKycFragmentCallback;

    public KycPresenter(Context context, IKycFragmentCallback iKycFragmentCallback) {
        this.context = context;
        this.iKycFragmentCallback = iKycFragmentCallback;
    }

    public void getAllDynamicSelectedFormFromServer(String formId) {
        iKycFragmentCallback.showProgressBar();
        String formBodyUrl = WebServiceURLs.ALL_DYNAMIC_FORM_BODY_URL.replace("FORM_ID", formId);
        String url = WebServiceURLs.BASE_URL +
                formBodyUrl +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        iKycFragmentCallback.hideProgressBar();
                        getDynamicFormDetailsParsing(object);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        iKycFragmentCallback.hideProgressBar();
                        if (errorMessage.contains("AuthFailureError")) {
                            iKycFragmentCallback.showAlert();
                        } else {
                            iKycFragmentCallback.showMessage(errorMessage);
                        }
                    }
                });
    }

    public void getAllDynamicFormsFromServer() {
        iKycFragmentCallback.showProgressBar();
        String url = WebServiceURLs.BASE_URL +
                WebServiceURLs.ALL_DYNAMIC_FORMS_URL +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        iKycFragmentCallback.hideProgressBar();
                        getDynamicFormsFromResponse(object);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        iKycFragmentCallback.hideProgressBar();
                        if (errorMessage.contains("AuthFailureError")) {
                            iKycFragmentCallback.showAlert();
                        } else {
                            iKycFragmentCallback.showMessage(errorMessage);
                        }
                    }
                });
    }


    public void getDynamicFormDetailsParsing(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 200) {
                ArrayList<KycDynamicForm> allDynamicFormList = new ArrayList<>();
                HashMap<String, KycDynamicForm> allDynamicFormHashMap = new HashMap<>();
                JSONObject formIdJson = jsonObject.getJSONObject("data").getJSONObject("profileForm");
                /*delete old forms to dataBase*/
                ClientFormsDAO.getInstance().deleteOldForms(DataBaseContext.getDBObject(1));
                KycDynamicForm kycDynamicForm = new KycDynamicForm();
                if (formIdJson.has("profileFormID")) {
                    kycDynamicForm.profileFormId = formIdJson.getString("profileFormID");
                }

                if (formIdJson.has("profileFormDetails")) {
                    JSONObject jsonProfileObj = formIdJson.getJSONObject("profileFormDetails");
                    JSONArray jsonArrayHeader = jsonProfileObj.getJSONArray("Form Header");
                    if (jsonArrayHeader.length() > 0) {
                        JSONObject jObj = jsonArrayHeader.getJSONObject(0);
                        if (jObj.has("Value")) {
                            kycDynamicForm.formName = jObj.getString("Value");
                        }
                        for (int i = 0; i < jsonArrayHeader.length(); i++) {
                            ProfileForm profileform = new ProfileForm();
                            JSONObject jobject = jsonArrayHeader.getJSONObject(i);
                            if (jobject.has("Label"))
                                profileform.name = jobject.getString("Label");
                            if (profileform.name.contains("Form Label")) {
                                continue;
                            }

                            if (jobject.has("Type"))
                                profileform.type = jobject.getString("Type");
                            if (jobject.has("Value")) {
                                if (jobject.get("Value") instanceof String) {
                                    profileform.value.add((String) jobject.get("Value"));
                                } else {
                                    JSONArray jsonValues = jobject.getJSONArray("Value");
                                    if (jsonValues != null && jsonValues.length() > 0) {
                                        for (int k = 0; k < jsonValues.length(); k++) {
                                            profileform.value.add(jsonValues.getString(k));
                                        }
                                    }
                                }
                            }
                            kycDynamicForm.allProfileFormsList.add(profileform);
                        }
                    }
                    JSONArray jsonArrayBody = jsonProfileObj.getJSONArray("Form Body");
                    if (jsonArrayBody.length() > 0) {
                        for (int i = 0; i < jsonArrayBody.length(); i++) {
                            ProfileForm profileform = new ProfileForm();
                            JSONObject jObject = jsonArrayBody.getJSONObject(i);
                            if (jObject.has("Label"))
                                profileform.name = jObject.getString("Label");
                            if (jObject.has("Type"))
                                profileform.type = jObject.getString("Type");
                            if (jObject.has("Value")) {
                                if (jObject.get("Value") instanceof String) {
                                    profileform.value.add((String) jObject.get("Value"));
                                } else {
                                    JSONArray jsonValues = jObject.getJSONArray("Value");
                                    if (jsonValues != null && jsonValues.length() > 0) {
                                        for (int k = 0; k < jsonValues.length(); k++) {
                                            profileform.value.add(jsonValues.getString(k));
                                        }
                                    }
                                }
                            }

                            kycDynamicForm.allProfileFormsList.add(profileform);
                        }
                        //getting form type name
                    }
                }

                allDynamicFormList.add(kycDynamicForm);
                allDynamicFormHashMap.put(kycDynamicForm.formName, kycDynamicForm);

                if (allDynamicFormList.size() > 0) {
                    iKycFragmentCallback.loadDynamicFormToView(allDynamicFormList, allDynamicFormHashMap);
                }
            } else {
                Toast.makeText(context,
                        context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void getDynamicFormsFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 200) {
                ArrayList<KycDynamicForm> allDynamicFormList = new ArrayList<>();
                HashMap<String, KycDynamicForm> allDynamicFormHashMap = new HashMap<>();
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("profileForms");
                if (jsonArray.length() > 0) {
                    /*delete old forms to dataBase*/
                    ClientFormsDAO.getInstance().deleteOldForms(DataBaseContext.getDBObject(1));

                    for (int m = 0; m < jsonArray.length(); m++) {
                        JSONObject formIdJson = jsonArray.getJSONObject(m);
                        KycDynamicForm kycDynamicForm = new KycDynamicForm();
                        if (formIdJson.has("profileFormID")) {
                            kycDynamicForm.profileFormId = formIdJson.getString("profileFormID");
                        }

                        if (formIdJson.has("profileFormDetails")) {
                            JSONArray jsonArrayHeader = formIdJson.getJSONArray("profileFormDetails");
//                            JSONArray jsonArrayHeader = jsonProfileObj.getJSONArray("Form Header");
                            if (jsonArrayHeader != null && jsonArrayHeader.length() > 0) {
                                JSONObject jObj = jsonArrayHeader.getJSONObject(0);
                                if (jObj.has("Value")) {
                                    kycDynamicForm.formName = jObj.getString("Value");
                                }
                                for (int i = 0; i < jsonArrayHeader.length(); i++) {
                                    ProfileForm profileform = new ProfileForm();
                                    JSONObject jobject = jsonArrayHeader.getJSONObject(i);
                                    if (jobject.has("Label"))
                                        profileform.name = jobject.getString("Label");
                                    if (profileform.name.contains("Form Label")) {
                                        continue;
                                    }

                                    if (jobject.has("Type"))
                                        profileform.type = jobject.getString("Type");
                                    if (jobject.has("Value")) {
                                        if (jobject.get("Value") instanceof String) {
                                            profileform.value.add((String) jobject.get("Value"));
                                        } else {
                                            JSONArray jsonValues = jobject.getJSONArray("Value");
                                            if (jsonValues != null && jsonValues.length() > 0) {
                                                for (int k = 0; k < jsonValues.length(); k++) {
                                                    profileform.value.add(jsonValues.getString(k));
                                                }
                                            }
                                        }
                                    }
                                    kycDynamicForm.allProfileFormsList.add(profileform);
                                }
                            }
//                            JSONArray jsonArrayBody = jsonProfileObj.getJSONArray("Form Body");
//                            if (jsonArrayBody != null && jsonArrayBody.length() > 0) {
//                                for (int i = 0; i < jsonArrayBody.length(); i++) {
//                                    ProfileForm profileform = new ProfileForm();
//                                    JSONObject jObject = jsonArrayBody.getJSONObject(i);
//                                    if (jObject.has("Label"))
//                                        profileform.name = jObject.getString("Label");
//                                    if (jObject.has("Type"))
//                                        profileform.type = jObject.getString("Type");
//                                    if (jObject.has("Value")) {
//                                        if (jObject.get("Value") instanceof String) {
//                                            profileform.value.add((String) jObject.get("Value"));
//                                        } else {
//                                            JSONArray jsonValues = jObject.getJSONArray("Value");
//                                            if (jsonValues != null && jsonValues.length() > 0) {
//                                                for (int k = 0; k < jsonValues.length(); k++) {
//                                                    profileform.value.add(jsonValues.getString(k));
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    kycDynamicForm.allProfileFormsList.add(profileform);
//                                }
//                                //getting form type name
//                            }
                        }

                        allDynamicFormList.add(kycDynamicForm);
                        allDynamicFormHashMap.put(kycDynamicForm.formName, kycDynamicForm);
                    }

                    /*save the forms to dataBase*/
                    ClientFormsDAO.getInstance().insertOrUpdateForm(DataBaseContext.getDBObject(1), jsonArray);

                    if (allDynamicFormList.size() > 0) {
                        iKycFragmentCallback.loadDynamicSpinner(allDynamicFormList, allDynamicFormHashMap);
                    }
                }
            } else {
                Toast.makeText(context,
                        context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }


    public void createClientKycData(String jsonObject) {
        //ClientDataDAO.getInstance().insertOrUpdateClientKyc(DataBaseContext.getDBObject(1), jsonObject, clientId);
        if (UtilityMethods.isNetworkAvailable(context)) {
            iKycFragmentCallback.showProgressBar();
            Map<String, String> params = new Gson().fromJson(
                    jsonObject, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );
            String url = WebServiceURLs.BASE_URL +
                    WebServiceURLs.SAVE_KYC_DATA_URL +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            WebService.getInstance().apiPostRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iKycFragmentCallback.hideProgressBar();
                            iKycFragmentCallback.showMessage("Profile created successfully");
                            iKycFragmentCallback.navigateToOnBoard();
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iKycFragmentCallback.hideProgressBar();
                            iKycFragmentCallback.showMessage(errorMessage);
                        }
                    });
        } else {
            iKycFragmentCallback.showMessage("Kyc data has been saved successfully in offline");
        }

    }

    public void updateClientKycData(String jsonObject, String profileId) {
        //ClientDataDAO.getInstance().insertOrUpdateClientKyc(DataBaseContext.getDBObject(1), jsonObject, clientId);
        if (UtilityMethods.isNetworkAvailable(context)) {
            iKycFragmentCallback.showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    jsonObject, new TypeToken<HashMap<String, Object>>() {
                    }.getType()
            );

            String urlWithProfileId = WebServiceURLs.UPDATE_KYC_DATA_URL.replace("PROFILE_ID", profileId);
            String url = WebServiceURLs.BASE_URL +
                    urlWithProfileId +
                    PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
            WebService.getInstance().apiPutRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            iKycFragmentCallback.hideProgressBar();
                            iKycFragmentCallback.showMessage("Profile updated successfully");
                            iKycFragmentCallback.navigateToOnBoard();
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            iKycFragmentCallback.hideProgressBar();
                            iKycFragmentCallback.showMessage(errorMessage);
                        }
                    });
        } else {
            //iKycFragmentCallback.showMessage("Kyc data has been saved successfully in offline");
        }

    }

    public void verifyWorkFlow(String jsonPayload, WebService.OnServiceResponseListener responseListener) {
        iKycFragmentCallback.showProgressBar();
        String url = PreferenceConnector.readString(context, "BASE_URL", "") + WebServiceURLs.VERIFY_WORKFLOW +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        try {
            WebService.getInstance().apiPutRequestCallJSON(url, new JSONObject(jsonPayload), responseListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
