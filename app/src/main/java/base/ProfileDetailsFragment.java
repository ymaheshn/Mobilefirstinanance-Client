package base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.mffapp.R;
import com.shufti.shuftipro.Shuftipro;
import com.shufti.shuftipro.listeners.ShuftiVerifyListener;
//import com.yoti.mobile.android.yotisdkcore.YotiSdk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.CustomDialogues;
import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dashboard.DashboardActivity;
import interfaces.IOnDataRetrievedFromGallery;
import networking.VolleyMultipartRequest;
import networking.WebService;
import networking.WebServiceURLs;
import onboard.TabDto;
import onboard.TabFields;
import onboard.WorkFlowTemplateDto;

@SuppressLint("ValidFragment")
public class ProfileDetailsFragment extends BaseFragment implements IOnDataRetrievedFromGallery, ShuftiVerifyListener {

    private int index;

//    private YotiSdk yotiSdk;
    private Integer clientSessionTokenTtl;
    private String clientSessionToken;
    private String sessionId;

    @BindView(R.id.container_values)
    public LinearLayout containerValues;

    @BindView(R.id.btn_save)
    public Button btnSave;

    @BindView(R.id.text_header)
    public TextView textHeader;

    @BindView(R.id.progressBar)
    public View progressBar;

    @BindView(R.id.btn_verify)
    Button btnVerify;


    Shuftipro instance;
    JSONObject jsonObject;

    private WorkFlowTemplateDto workFlowTemplateDt;
    private String templateDetailsId;
    private String profileId;
    private String tabName;
    private String fileNameToUpload;
    private String verifiedStatus;
    private String workFlowId;
    private String workFlowProfileId;

    @SuppressLint("ValidFragment")
    public ProfileDetailsFragment(int index, String tabName) {
        this.index = index;
        this.tabName = tabName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Constants.clientDataDTO != null) {
            profileId = Constants.clientDataDTO.profileId;
        }


        workFlowTemplateDt = Constants.workFlowTemplateDt;
        TabDto tabDto = workFlowTemplateDt.tabDtoArrayList.get(index);
        for (TabFields fields :
                tabDto.tabFieldsArrayList) {
            addFieldsToLayout(fields);
        }

        for (TabFields fields :
                tabDto.tabFieldsBodyArrayList) {
            addFieldsToLayout(fields);
        }

        getCashFlowData(String.valueOf(tabDto.tabId),
                Constants.workFlowTemplateDt.workFlowProfileId);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        int sessionStatusCode = yotiSdk.getSessionStatusCode();
//        String sessionStatusDescription = yotiSdk.getSessionStatusDescription();
//
//        Toast.makeText(getActivity(),sessionStatusCode,Toast.LENGTH_LONG).show();
//        Toast.makeText(getActivity(),sessionStatusDescription,Toast.LENGTH_LONG).show();
//
//    }

    private void addFieldsToLayout(TabFields tabFields) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = (int) UtilityMethods.convertDpToPixel(15, getActivity());
        if (tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_LIST) ||
                tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_DATE)) {
            View view = layoutInflater.inflate(R.layout.view_edit_text, null, false);
            EditText editText = view.findViewById(R.id.editText);
            View imgDropDown = view.findViewById(R.id.img_drop_down);
            imgDropDown.setVisibility(View.VISIBLE);
            editText.setText("");
            TextInputLayout textInputLayout = view.findViewById(R.id.text_inout_layout);
            textInputLayout.setHint(tabFields.name);
            editText.setSingleLine(true);
            editText.setCursorVisible(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            view.setTag(tabFields);
            view.setLayoutParams(params);
            editText.setOnClickListener(view1 -> {
                if (tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_LIST)) {
                    CustomDialogues.showListPop(getActivity(), tabFields.name, (EditText) view1, tabFields.valuesList);
                } else {
                    showDatePickerDialog((EditText) view1);
                }
            });
            containerValues.addView(view);
        } else if (tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_IMAGE)) {
            View view = layoutInflater.inflate(R.layout.item_add_image, null, false);
            TextView titleTV = view.findViewById(R.id.text_add_image_title);
            titleTV.setText(tabFields.name);
            view.setTag(tabFields);
            view.setOnClickListener(v -> {
                fileNameToUpload = tabFields.name;
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(galleryIntent, DashboardActivity.IMAGE_PICK_GALLERY);
            });
            view.setLayoutParams(params);
            containerValues.addView(view);
        } else {
            View view = layoutInflater.inflate(R.layout.view_edit_text, null, false);
            EditText editText = view.findViewById(R.id.editText);
            editText.setText("");
            TextInputLayout textInputLayout = view.findViewById(R.id.text_inout_layout);
            textInputLayout.setHint(tabFields.name);
            View imgDropDown = view.findViewById(R.id.img_drop_down);
            imgDropDown.setVisibility(View.GONE);
            if (tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            } else if (tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_PHONE)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            view.setTag(tabFields);
            view.setLayoutParams(params);
            containerValues.addView(view);
        }
    }

    private ArrayList<TabFields> getWorkFlowDetails(int workFlowIndex) {
        return workFlowTemplateDt.tabDtoArrayList.get(workFlowIndex).tabFieldsArrayList;
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
    }

    private String getCashFlowId() {
        String id = "";
        for (TabDto tabDto : workFlowTemplateDt.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CASH_FLOW)) {
                id = String.valueOf(tabDto.tabId);
            }
        }
        return id;
    }

    public void getCashFlowData(String workFlowId, String workFlowProfileId) {
        this.workFlowId = workFlowId;
        this.workFlowProfileId = workFlowProfileId;
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            showProgressBar();
            String url = PreferenceConnector.readString(getActivity(), "BASE_URL", "") +
                    WebServiceURLs.CASH_FLOW_GET_INFO_URL +
                    PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");

            url = url.replace("WORKFLOW_PROFILE_ID", workFlowProfileId);
            url = url.replace("TEMPLATE_ID", workFlowId);
            WebService.getInstance().apiGetRequestCall(url,

                    new WebService.OnServiceResponseListener() {
                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            hideProgressBar();
                            if (!TextUtils.isEmpty(object)) {
                                try {
                                    JSONArray jsonArray = new JSONArray(object);
                                    if (jsonArray.length() > 0) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        if (jsonObject.has("templateDetailsID")) {
                                            templateDetailsId = jsonObject.getString("templateDetailsID");
                                        }
                                        if (jsonObject.has("workflowTemplateDetails")) {
                                            getCashFlowData(true, jsonObject.getJSONObject("workflowTemplateDetails").toString(), templateDetailsId);
                                        }
                                        if (jsonObject.has("verified")) {
                                            verifiedStatus = jsonObject.getString("verified");
                                            if (!TextUtils.isEmpty(verifiedStatus) && verifiedStatus.equalsIgnoreCase(getString(R.string.yes))) {
                                                btnVerify.setVisibility(View.GONE);
                                                btnSave.setVisibility(View.GONE);
                                            }
                                        }
                                    } else {
                                        getCashFlowData(false, null, "");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showMessage("Something went wrong.");
                                }
                            }
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            hideProgressBar();
                            if (errorMessage.contains("AuthFailureError")) {
                                showAlert();
                            } else {
                                showMessage(errorMessage);
                            }

                        }
                    });
        }
    }

    public void getCashFlowData(boolean status, String string, String templateDetailsId) {
        btnSave.setVisibility(View.VISIBLE);
        if (status) {
            this.templateDetailsId = templateDetailsId;
            mapSavedDataToViews(string);
            if (!TextUtils.isEmpty(templateDetailsId)) {
                btnSave.setText("Update");

            } else {
                btnSave.setText("Save");
            }
        }
    }

    @OnClick({R.id.btn_save, R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                validateCashFlowAndSave();
                break;
            case R.id.btn_verify:
                verifyWorkFlow();
                break;
        }
    }

    private void validateCashFlowAndSave() {
        JSONObject jsonObject = validateData();
        Log.d("jsonObject", "jsonObject :" + jsonObject);

//        if (jsonObject != null) {
//            if (!TextUtils.isEmpty(templateDetailsId)) {
//                Log.d("templateDetailsId","templateDetailsId :"+templateDetailsId);
//                updateCashFlowData(jsonObject.toString(), templateDetailsId);
//            } else {
//                saveCashFlowData(jsonObject.toString());
//            }
//        }

        if (jsonObject != null) {
            String selectIdentityType = null;
            try {
                selectIdentityType = jsonObject.getString("Select Identity type");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (selectIdentityType) {
                case "Shufti":
                    launchShufptiProSdk();
                    break;


                case "Yoti":
                    launchYotiSdk();
                    break;
            }

        }


    }

    private void launchShufptiProSdk() {

        jsonObject = new JSONObject();
        try {
            jsonObject.put("reference", UUID.randomUUID());
            jsonObject.put("country", "IN");
            jsonObject.put("language", "EN");
            jsonObject.put("email", "kellavivek@gmail.com");
            jsonObject.put("callback_url", "https://www.yourdomain.com");
            jsonObject.put("redirect_url", "");
            jsonObject.put("verification_mode", "any");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Creating face object
        JSONObject faceObject = new JSONObject();
        try {
            faceObject.put("proof", "");
            jsonObject.put("face", faceObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Creating document object
        JSONObject documentationObject = new JSONObject();
        ArrayList<String> doc_supported_types = new ArrayList<String>();

        doc_supported_types.add("passport");
        doc_supported_types.add("id_card");
        doc_supported_types.add("driving_license");
        doc_supported_types.add("credit_or_debit_card");

        try {
            documentationObject.put("proof", "");
            documentationObject.put("supported_types", new JSONArray(doc_supported_types));

            //Set parameters in the requested object
            documentationObject.put("name", "");
            documentationObject.put("dob", "");
//            documentationObject.put("document_number", "");
//            documentationObject.put("expiry_date", "");
//            documentationObject.put("issue_date", "");
            documentationObject.put("backside_proof_required", "1");

            jsonObject.put("document", documentationObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject phoneObject = new JSONObject();
        try {
            phoneObject.put("proof", "");
            phoneObject.put("phone_number", "");
            phoneObject.put("random_code", "");
            phoneObject.put("text", "");

            jsonObject.put("phone", phoneObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        instance = Shuftipro.getInstance("cc30e2766c4594ad53396175b6ca3cda24fff531ce98bf48aaff0f936dbdc480", "xuSH1Ip2YG8BS4ekks2Mo1CWkwY1UjAe");
        instance.setCaptureEnabled(true);
        instance.shuftiproVerification(jsonObject, getActivity(), this);


    }

    private void launchYotiSdk() {

//        yotiSdk = new YotiSdk(getActivity());
//        String url = "https://";
//
//        WebService.getInstance().apiPostRequestCallJSON(url, null, new WebService.OnServiceResponseListener() {
//            @Override
//            public void onApiCallResponseSuccess(String url, String object) {
//                try {
//                    JSONObject yotiResponse = new JSONObject(object);
//                    clientSessionTokenTtl = yotiResponse.getInt("client_session_token_ttl");
//                    clientSessionToken = yotiResponse.getString("client_session_token");
//                    sessionId = yotiResponse.getString("session_id");
//                    yotiSdk.setSessionId(sessionId);
//                    yotiSdk.setClientSessionToken(clientSessionToken);
//                    yotiSdk.start(getActivity(),9001);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onApiCallResponseFailure(String errorMessage) {
//                showMessage(errorMessage);
//            }
//        });


    }


    public void updateCashFlowData(String s, String templateDetailsId) {
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    s, new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );
            String url = PreferenceConnector.readString(getActivity(), "BASE_URL", "") +
                    WebServiceURLs.CASH_FLOW_UPDATE_URL +
                    PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
            url = url.replace("WORKFLOW_PROFILE_ID", templateDetailsId);
            WebService.getInstance().apiPutRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            hideProgressBar();
                            if (TextUtils.isEmpty(tabName)) {
                                tabName = "";
                            }
                            Log.d("tabName", "tabName :" + tabName);
                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            hideProgressBar();
                            showMessage(errorMessage);
                        }
                    });
        }
    }


    private void verifyWorkFlow() {
        if (TextUtils.isEmpty(templateDetailsId)) {
            Toast.makeText(getActivity(), R.string.details_are_missing, Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject workflowJson = validateData();
        if (workflowJson != null) {
            try {
                if (workflowJson != null) {
                    jsonObject.put("workflowTemplateDetails", workflowJson);
                }
                jsonObject.put("workflowProfileID", workFlowProfileId);
                jsonObject.put("templateID", workFlowId);
                jsonObject.put("templateDetailsID", templateDetailsId);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);

                JSONObject finalJSON = new JSONObject();
                finalJSON.put("workflowTemplateDetails", jsonArray);
                verifyApi(finalJSON.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject validateData() {
        try {
            boolean allFieldsValidated = true;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("workflowProfileID", workFlowTemplateDt.workFlowProfileId);
                jsonObject.put("Id", getCashFlowId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < containerValues.getChildCount(); i++) {
                View view = containerValues.getChildAt(i);
                TabFields tabFields = (TabFields) view.getTag();

                if (view.getId() == R.id.container_input_type) {
                    EditText editText = view.findViewById(R.id.editText);
                    if (editText != null) {
                        try {
                            jsonObject.put(tabFields.name, editText.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            allFieldsValidated = false;
                            editText.requestFocus();
                            Toast.makeText(getActivity(), "Please enter '" + tabFields.name + "'", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else if (view.getId() == R.id.container_radio_type) {
                    RadioGroup radioGroup = view.findViewById(R.id.radio_group_list);
                    TextView textView = view.findViewById(R.id.titleTV);
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        allFieldsValidated = false;
                        Toast.makeText(getActivity(), "Please check '" + tabFields.name + "'", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    RadioButton selectedButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                    try {
                        jsonObject.put(textView.getText().toString(), selectedButton.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (view.getId() == R.id.container_image_type) {

                }
            }

            if (allFieldsValidated) {
                return jsonObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void verifyApi(String jsonPayload) {
        showProgressBar();
        String url = PreferenceConnector.readString(getActivity(), "BASE_URL", "") + WebServiceURLs.VERIFY_WORKFLOW +
                PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
        try {
            WebService.getInstance().apiPutRequestCallJSON(url, new JSONObject(jsonPayload), new WebService.OnServiceResponseListener() {
                @Override
                public void onApiCallResponseSuccess(String url, String object) {
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            Toast.makeText(getActivity(), "Verified Successfully!!", Toast.LENGTH_SHORT).show();
                            btnVerify.setVisibility(View.GONE);
                            btnSave.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    hideProgressBar();
                }

                @Override
                public void onApiCallResponseFailure(String errorMessage) {
                    hideProgressBar();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void saveCashFlowData(String s) {
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            showProgressBar();

            Map<String, String> params = new Gson().fromJson(
                    s, new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );
            String url = PreferenceConnector.readString(getActivity(), "BASE_URL", "") +
                    WebServiceURLs.CASH_FLOW_POST_URL +
                    PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
            WebService.getInstance().apiPostRequestCall(url,
                    params, new WebService.OnServiceResponseListener() {

                        @Override
                        public void onApiCallResponseSuccess(String url, String object) {
                            hideProgressBar();
                            if (!TextUtils.isEmpty(object)) {

                                try {
                                    JSONObject jsonObject = new JSONObject(object);
                                    if (jsonObject.has("message")) {
                                        showMessage(jsonObject.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        @Override
                        public void onApiCallResponseFailure(String errorMessage) {
                            hideProgressBar();
                            showMessage(errorMessage);
                        }
                    });
        }
    }

    private void mapSavedDataToViews(String jsonObjectString) {
        Map<String, Object> valuesHM = new Gson().fromJson(
                jsonObjectString, new TypeToken<HashMap<String, Object>>() {
                }.getType()
        );
        if (containerValues.getChildCount() > 0) {
            for (int i = 0; i < containerValues.getChildCount(); i++) {
                View childAt = containerValues.getChildAt(i);
                TabFields tabFields = (TabFields) childAt.getTag();
                if (tabFields != null) {
                    if (tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_VARCHAR) ||
                            tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_NUMBER) ||
                            tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_PHONE) ||
                            tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_DATE) ||
                            tabFields.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_LIST)) {
                        FrameLayout frameLayout = (FrameLayout) childAt;
                        TextInputLayout viewGroup = (TextInputLayout) frameLayout.getChildAt(0);
                        EditText editText = viewGroup.getEditText();
                        editText.setText("");
                        if (valuesHM.containsKey(tabFields.name)) {
                            if (!TextUtils.isEmpty(String.valueOf(valuesHM.get(tabFields.name)))) {
                                if (valuesHM.get(tabFields.name) instanceof String) {
                                    editText.setText(String.valueOf(valuesHM.get(tabFields.name)).trim());
                                } else if (valuesHM.get(tabFields.name) instanceof ArrayList) {
                                    ArrayList<String> values = (ArrayList<String>) valuesHM.get(tabFields.name);
                                    if (values.size() > 0) {
                                        editText.setText(values.get(0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDataRetrieved(int requestCode, int resultCode, Intent data) {
        // retrieve scan result
        Uri selectedImage = data.getData();
        try {
            showLoading();
            String filename = !TextUtils.isEmpty(fileNameToUpload) ? fileNameToUpload + "_" + UtilityMethods.getDateFormat() + ".jpeg" : UtilityMethods.getDateFormat() + ".jpeg";
            InputStream iStream = getActivity().getContentResolver().openInputStream(selectedImage);
            byte[] inputData = UtilityMethods.getBytes(iStream);
            String url = PreferenceConnector.readString(getActivity(), "BASE_URL", "") +
                    WebServiceURLs.UPLOAD_BUSINESS +
                    PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
            HashMap<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
            params.put("files", new VolleyMultipartRequest.DataPart(filename, inputData, "image/jpeg"));
            url = url.replace("WORKFLOW_PROFILE_ID", profileId);
            WebService.getInstance().apiMultipart(url, params, new WebService.OnServiceResponseListener() {
                @Override
                public void onApiCallResponseSuccess(String url, String object) {
                    try {
                        JSONArray jsonArray = new JSONArray(object);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String downloadUri = jsonObject.getString("fileDownloadUri");
                        String fileName = jsonObject.getString("fileName");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismissLoading();
                    Toast.makeText(getActivity(), "Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onApiCallResponseFailure(String errorMessage) {
                    dismissLoading();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (FileNotFoundException e) {
            dismissLoading();
            e.printStackTrace();
        } catch (IOException e) {
            dismissLoading();
            e.printStackTrace();
        }
        // String picturePath contains the path of selected Image
    }

    @Override
    public void verificationStatus(HashMap<String, String> responseSet) {
        Log.d("Response Set", responseSet.toString());
        Log.d("response", responseSet.get("response"));
        showMessage(responseSet.get("event"));
    }
}
