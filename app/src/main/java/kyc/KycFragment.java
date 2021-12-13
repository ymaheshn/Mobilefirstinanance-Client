package kyc;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.CustomDialogues;
import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import addclient.AddClientFragment;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dashboard.DashboardActivity;
import database.dao.ClientFormsDAO;
import database.db.DataBaseContext;
import interfaces.IOnDataRetrievedFromAdharScannerListener;
import interfaces.IOnDataRetrievedFromGallery;
import interfaces.IOnFragmentChangeListener;
import interfaces.IOnHeaderItemsClickListener;
import kyc.dto.AdharDTO;
import kyc.dto.BranchTree;
import kyc.dto.KycDynamicForm;
import kyc.dto.ProfileForm;
import kyc.utils.DataAttributes;
import kyc.utils.MySupportMapFragment;
import networking.MyApplication;
import networking.VolleyMultipartRequest;
import networking.WebService;
import networking.WebServiceURLs;
import onboard.TabDto;
import onboard.WorkFlowTemplateDto;

import static Utilities.CustomDialogues.showBranch;

/**
 * A simple {@link Fragment} subclass.
 */
public class KycFragment extends BaseFragment implements IOnHeaderItemsClickListener, IOnDataRetrievedFromAdharScannerListener, AdapterView.OnItemSelectedListener,
        IKycFragmentCallback, OnMapReadyCallback, IOnDataRetrievedFromGallery {
    public final static String UUID_STR = "00001101-0000-1000-8000-00805F9B34FB";
    private static final String TAG = "KycFragment";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";


    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private Unbinder unbinder;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @BindView(R.id.applicantPictureLL)
    LinearLayout applicantPictureLL;
    @BindView(R.id.coApplicantPictureLL)
    LinearLayout coApplicantPictureLL;
    @BindView(R.id.saveBTN)
    Button saveBTN;

    @BindView(R.id.btn_verify)
    Button btnVerify;

    @BindView(R.id.typeSelectionSP)
    Spinner typeSelectionSP;
    @BindView(R.id.radio_type_selection_sp)
    RadioGroup radioTypeSelection;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;

    @BindView(R.id.container_map)
    View containerMap;

    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    @BindView(R.id.progressBar)
    View progressBar;

    @BindView(R.id.container_add_documents)
    View containerAddDocuments;

    @BindView(R.id.text_applicant_picture_name)
    TextView textApplicantPictureName;

    @BindView(R.id.profile_image)
    ImageView imgProfile;

    @BindView(R.id.img_camera)
    ImageView imgCamera;

    private HashMap<String, KycDynamicForm> allDynamicFormHashMap;
    private HashMap<String, KycDynamicForm> allDynamicSpinnerHashMap;
    private ProgressDialog progressDialog = null;
    private KycPresenter kycPresenter;
    private String profileId = "";
    private String profileFormId = "1";
    private String selectedFormId = "";
    private String profileJsonString = "";
    private int formId;
    private EditText longitudeEditText;
    private EditText latitudeEditText;
    private String url;
    private double longitude;
    private double latitude;
    private GoogleMap googleMap;
    private String fileNameToUpload;
    private int index;
    private String templateDetailsId;
    private String workFlowId;
    private String workFlowProfileId;
    private List<BranchTree> branchTrees;
    private List<String> branchNamesInString = new ArrayList<>();

    public KycFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kyc, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            formId = bundle.getInt(AddClientFragment.KEY_FORM_ID);
        }

        NestedScrollView scrollView = view.findViewById(R.id.scrollView);

        MySupportMapFragment mapFragment =
                (MySupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        mapFragment.setListener(new MySupportMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((DashboardActivity) getActivity()).setToolBarBackVisible(true);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        // iOnFragmentChangeListener.onHeaderUpdate(Constants.KYC_FRAGMENT, "KYC");
        iOnFragmentChangeListener.setHeaderItemClickListener(this);
        kycPresenter = new KycPresenter(getActivity(), this);
        if (radioTypeSelection != null) {
            radioTypeSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                    CharSequence selectedText = radioButton.getText();
                    if (selectedText.equals("Personal")) {
                        coApplicantPictureLL.setVisibility(View.VISIBLE);
                    } else {
                        coApplicantPictureLL.setVisibility(View.GONE);
                    }
                    if (allDynamicFormHashMap != null) {
                        loadDynamicForm(allDynamicFormHashMap.get(selectedText).allProfileFormsList);
                        selectedFormId = allDynamicFormHashMap.get(selectedText).profileFormId;
                        if (!TextUtils.isEmpty(profileId) && !TextUtils.isEmpty(profileFormId)) {
                            if (profileFormId.equals(selectedFormId))
                                mapSavedDataToViews(profileJsonString);
                            else {
//                            for (int j = 0; j < adapterView.getCount(); j++) {
//                                if (profileFormId.equals(allDymamicFormHashmap.get(adapterView.getItemAtPosition(i)).profileFormId)) {
//                                    typeSelectionSP.setSelection(j);
//                                    break;
//                                }
//                            }
                            }
                        }
                    }
                }
            });
        }
        typeSelectionSP.setOnItemSelectedListener(KycFragment.this);

        if (Constants.clientDataDTO != null) {
            profileId = Constants.clientDataDTO.profileId;
            profileFormId = String.valueOf(Constants.clientDataDTO.formId);
            profileJsonString = Constants.clientDataDTO.kycData;
        }

        if (TextUtils.isEmpty(profileId)) {
            saveBTN.setText("Create");
            btnVerify.setVisibility(View.GONE);
            typeSelectionSP.setVisibility(View.VISIBLE);
            containerAddDocuments.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(profileId) && Constants.isFromAddClient) {
            saveBTN.setText("Update");
            btnVerify.setVisibility(View.GONE);
            typeSelectionSP.setVisibility(View.GONE);
            containerAddDocuments.setVisibility(View.VISIBLE);
        } else {
            saveBTN.setText("Update");
            typeSelectionSP.setVisibility(View.GONE);
            btnVerify.setVisibility(View.VISIBLE);
            containerAddDocuments.setVisibility(View.VISIBLE);
        }

        // sets the colors used in the refresh animation
        pullToRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent,
                R.color.colorAccent, R.color.colorAccent);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);
                if (UtilityMethods.isNetworkAvailable(getActivity())) {
                    if (TextUtils.isEmpty(profileId)) {
                        kycPresenter.getAllDynamicFormsFromServer();
                    } else {
                        kycPresenter.getAllDynamicSelectedFormFromServer(profileFormId);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            getBranchTrees(new WebService.OnServiceResponseListener() {
                @Override
                public void onApiCallResponseSuccess(String url, String object) {
                    if (!TextUtils.isEmpty(object)) {
                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            int status = jsonObject.getInt("status");
                            if (status == 200) {
                                JSONArray structureArray = jsonObject.getJSONObject("data").getJSONObject("branches").getJSONArray("structure");
                                Type typeMyType = new TypeToken<ArrayList<BranchTree>>() {
                                }.getType();
                                branchTrees = new Gson().fromJson(structureArray.toString(), typeMyType);
                                prepareBranchData(branchTrees);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (TextUtils.isEmpty(profileId)) {
                        kycPresenter.getAllDynamicFormsFromServer();
                    } else {
                        kycPresenter.getAllDynamicSelectedFormFromServer(profileFormId);
                    }
                }

                @Override
                public void onApiCallResponseFailure(String errorMessage) {
                    if (TextUtils.isEmpty(profileId)) {
                        kycPresenter.getAllDynamicFormsFromServer();
                    } else {
                        kycPresenter.getAllDynamicSelectedFormFromServer(profileFormId);
                    }
                }
            });

            WorkFlowTemplateDto workFlowTemplateDt = Constants.workFlowTemplateDt;
            if (!(TextUtils.isEmpty(profileId) || Constants.isFromAddClient)) {
                TabDto tabDto = workFlowTemplateDt.tabDtoArrayList.get(index);
                getCashFlowData(String.valueOf(tabDto.tabId), workFlowTemplateDt.workFlowProfileId);
            }
        } else {
            final String response = ClientFormsDAO.getInstance().getOfflineForms(DataBaseContext.getDBObject(0));
            if (!TextUtils.isEmpty(response) && response.length() > 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        kycPresenter.getDynamicFormsFromResponse(response);
                    }
                }, 100);
                Toast.makeText(getActivity(), "Offline form", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }

    public void getCashFlowData(String workFlowId, String workFlowProfileId) {
        this.workFlowId = workFlowId;
        this.workFlowProfileId = workFlowProfileId;
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            showProgressBar();
            String url = WebServiceURLs.BASE_URL +
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
                                        templateDetailsId = "";
                                        if (jsonObject.has("templateDetailsID")) {
                                            templateDetailsId = jsonObject.getString("templateDetailsID");
                                        }
                                        if (jsonObject.has("verified")) {
                                            String verifiedStatus = jsonObject.getString("verified");
                                            if (!TextUtils.isEmpty(verifiedStatus) && verifiedStatus.equalsIgnoreCase(getString(R.string.yes))) {
                                                btnVerify.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showMessage("Something went wrong.");
                                }
                            }
                            /*showProgressBar();
                            getBranchTrees(new WebService.OnServiceResponseListener() {
                                @Override
                                public void onApiCallResponseSuccess(String url, String object) {
                                    hideProgressBar();
                                    if (!TextUtils.isEmpty(object)) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(object);
                                            JSONArray structureArray = jsonObject.getJSONArray("structure");
                                            Type typeMyType = new TypeToken<ArrayList<BranchTree>>() {
                                            }.getType();
                                            branchTrees = new Gson().fromJson(structureArray.toString(), typeMyType);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onApiCallResponseFailure(String errorMessage) {
                                    hideProgressBar();
                                }
                            });*/
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

    public void getBranchTrees(WebService.OnServiceResponseListener responseListener) {
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            showProgressBar();
            String url = WebServiceURLs.BASE_URL +
                    WebServiceURLs.VERIFY_BRANCHES +
                    PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
            WebService.getInstance().apiGetRequestCall(url, responseListener);
        }
    }


    @OnClick({R.id.saveBTN, R.id.applicantPictureLL, R.id.coApplicantPictureLL, R.id.container_add_documents, R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBTN:
                validateKycAndSave();
                break;
            case R.id.btn_verify:
                verifyWorkFlow();
                break;
            case R.id.applicantPictureLL:
                url = WebServiceURLs.BASE_URL +
                        WebServiceURLs.UPLOAD_PROFILE +
                        PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(i, DashboardActivity.IMAGE_PICK_GALLERY);
                break;
            case R.id.coApplicantPictureLL:
            case R.id.container_add_documents:
                url = WebServiceURLs.BASE_URL +
                        WebServiceURLs.UPLOAD_PERSONAL +
                        PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
                i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                fileNameToUpload = view.getId() == R.id.coApplicantPictureLL ? "NationalID" : "Documents";
                getActivity().startActivityForResult(i, DashboardActivity.IMAGE_PICK_GALLERY);
                break;
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
                kycPresenter.verifyWorkFlow(finalJSON.toString(), new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        try {
                            JSONObject jsonObject = new JSONObject(object);
                            int status = jsonObject.getInt("status");
                            if (status == 200) {
                                Toast.makeText(getActivity(), "Verified Successfully!!", Toast.LENGTH_SHORT).show();
                                btnVerify.setVisibility(View.GONE);
                                saveBTN.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideProgressBar();
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        hideProgressBar();
                        showMessage(errorMessage);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (typeSelectionSP != null && adapterView != null) {
            typeSelectionSP.setSelection(i);
            if (adapterView.getChildAt(0) != null && adapterView.getChildAt(0) != null) {
//                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.BLACK);
//                ((TextView) adapterView.getChildAt(0)).setTypeface(Typeface.DEFAULT_BOLD);

                if (!adapterView.getItemAtPosition(i).equals("Personal")) {
                    coApplicantPictureLL.setVisibility(View.VISIBLE);
                } else {
                    coApplicantPictureLL.setVisibility(View.GONE);
                }

                if (allDynamicSpinnerHashMap != null) {
                    kycPresenter.getAllDynamicSelectedFormFromServer(allDynamicSpinnerHashMap.get(adapterView.getItemAtPosition(i)).profileFormId);
//                    loadDynamicForm(allDynamicFormHashMap.get(adapterView.getItemAtPosition(i)).allProfileFormsList);
//                    selectedFormId = allDynamicFormHashMap.get(adapterView.getItemAtPosition(i)).profileFormId;
//                    if (!TextUtils.isEmpty(profileId) && !TextUtils.isEmpty(profileFormId)) {
//                        if (profileFormId.equals(selectedFormId))
//                            mapSavedDataToViews(profileJsonString);
//                        else {
//                            for (int j = 0; j < adapterView.getCount(); j++) {
//                                if (profileFormId.equals(allDynamicFormHashMap.get(adapterView.getItemAtPosition(i)).profileFormId)) {
//                                    typeSelectionSP.setSelection(j);
//                                    break;
//                                }
//                            }
//                        }
//                    }
                }
            }
        }

    }

    private void mapSavedDataToViews(String jsonObjectString) {
        Map<String, String> valuesHM = new Gson().fromJson(
                jsonObjectString, new TypeToken<HashMap<String, String>>() {
                }.getType()
        );

        /*Map<String, String> valuesHM = new HashMap<String, String>();
        valuesHM.put("Email", "mff");
        valuesHM.put("Address", "mff");
        valuesHM.put("Country", "mff");
        valuesHM.put("FormID", "mff");
        valuesHM.put("Gender", "mff");
        valuesHM.put("Last Name", "mff");
        valuesHM.put("Name", "mff");*/

        if (valuesHM.containsKey("profilePicture")) {
            String[] pictures = valuesHM.get("profilePicture").split("/");
            textApplicantPictureName.setText(pictures[pictures.length - 1]);
            String imageUrl = valuesHM.get("profilePicture") + "?access_token=" + PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
            Glide.with(getActivity()).load(imageUrl).into(imgProfile);
            imgCamera.setVisibility(View.GONE);
            applicantPictureLL.setClickable(false);
        }
        if (mainLayout.getChildCount() > 0) {
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View view = mainLayout.getChildAt(i);
                if (view instanceof FrameLayout) {
                    FrameLayout frameLayout = (FrameLayout) view;
                    TextInputLayout viewGroup = (TextInputLayout) frameLayout.getChildAt(0);
                    EditText editText = viewGroup.getEditText();
                    editText.setText("");
                    String fieldName = (String) editText.getTag(R.id.editText);
                    if (valuesHM.containsKey(fieldName)) {
                        String value = valuesHM.get(fieldName);
                        if (fieldName.equalsIgnoreCase(LATITUDE) && !TextUtils.isEmpty(value)) {
                            try {
                                latitude = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                            }
                        }
                        if (fieldName.equalsIgnoreCase(LONGITUDE) && !TextUtils.isEmpty(value)) {
                            try {
                                longitude = Double.parseDouble(value);
                            } catch (NumberFormatException e) {
                            }
                        }
                        if (latitude != 0 && longitude != 0 && googleMap != null) {
                            LatLng latLng = new LatLng(latitude, longitude);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, calculateZoomLevel()));
                        }
                        if (fieldName.equalsIgnoreCase("Hierarchy")) {
                            try {
                                branchName = null;
                                String branch = searchBranch(branchTrees, Integer.parseInt(value));
                                if (!TextUtils.isEmpty(branch)) {
                                    editText.setText(branch);
                                } else {
                                    editText.setText(value);
                                }
                            } catch (NumberFormatException ignored) {
                                editText.setText(value);
                            }
                        } else {
                            editText.setText(value);
                        }
                    }
                }
            }
        }
    }

    String branchName = null;

    private void prepareBranchData(List<BranchTree> branchTree) {
        for (BranchTree tree : branchTree) {
            if (!TextUtils.isEmpty(tree.name)) {
                branchNamesInString.add(tree.name);
            }
            if (tree.children != null && tree.children.size() > 0) {
                prepareBranchData(tree.children);
            }
        }
    }

    private String searchBranch(List<BranchTree> branchTree, int branch) {
        for (BranchTree tree : branchTree) {
            if (tree.id != null && tree.id == branch) {
                branchName = tree.name;
                break;
            }
            if (tree.children != null && tree.children.size() > 0) {
                searchBranch(tree.children, branch);
            }
        }
        return branchName;
    }

    String branchId = null;

    private String searchBranchByName(List<BranchTree> branchTree, String name) {
        for (BranchTree tree : branchTree) {
            if (tree.name != null && tree.name.equalsIgnoreCase(name)) {
                branchId = String.valueOf(tree.id);
                break;
            }
            if (tree.children != null && tree.children.size() > 0) {
                searchBranchByName(tree.children, name);
            }
        }
        return branchId;
    }

    private JSONObject validateData() {
        boolean allFieldsValidated = true;
        // this json object will be used for post service
        JSONObject jsonObject = new JSONObject();
        if (!TextUtils.isEmpty(selectedFormId)) {
            try {
                jsonObject.put("FormID", selectedFormId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View view = mainLayout.getChildAt(i);
                if (view instanceof FrameLayout) {
                    FrameLayout frameLayout = (FrameLayout) view;
                    TextInputLayout viewGroup = (TextInputLayout) frameLayout.getChildAt(0);
                    EditText editText = viewGroup.getEditText();
                    try {
                        branchId = null;
                        if (editText.getTag(R.id.editText).equals("Hierarchy")) {
                            String branchByName = searchBranchByName(branchTrees, editText.getText().toString());
                            if (!TextUtils.isEmpty(branchByName)) {
                                jsonObject.put(editText.getTag(R.id.editText).toString(), Integer.parseInt(branchByName));
                            } else {
                                jsonObject.put(editText.getTag(R.id.editText).toString(), editText.getText().toString());
                            }
                        } else {
                            jsonObject.put(editText.getTag(R.id.editText).toString(), editText.getText().toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        allFieldsValidated = false;
                        editText.requestFocus();
                        Toast.makeText(getActivity(), "Please enter " + editText.getTag(R.id.editText), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText.getTag(R.id.editText).equals("Email") && !UtilityMethods.emailValidate(editText.getText().toString())) {
                        allFieldsValidated = false;
                        editText.requestFocus();
                        Toast.makeText(getActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (editText.getTag(R.id.editText).equals("Phone") && !UtilityMethods.phoneNumberValidate((editText.getText().toString()))) {
                        allFieldsValidated = false;
                        editText.requestFocus();
                        Toast.makeText(getActivity(), "Please enter valid Phone Number", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
            if (allFieldsValidated) {
                return jsonObject;
            } else {
                return null;
            }
        }
        return null;
    }

    private void validateKycAndSave() {
        JSONObject jsonObject1 = validateData();
        Log.i("request payload", "request payload :" + jsonObject1);
        if (jsonObject1 != null) {
            if (TextUtils.isEmpty(profileId))
                kycPresenter.createClientKycData(jsonObject1.toString());
            else
                kycPresenter.updateClientKycData(jsonObject1.toString(), profileId);
        }
    }

    @Override
    public void onHeaderItemClick(String type) {
        switch (type) {
            case Constants.SCAN:
                scanAdhaar();
                break;
        }
    }

    public void scanAdhaar() {
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a Aadharcard QR Code");
        integrator.setResultDisplayDuration(500);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    protected void processScannedData(String scanData) {
        XmlPullParserFactory pullParserFactory;
        AdharDTO adharDTO = new AdharDTO();
        try {
            // init the parser factory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol", "Start document");
                } else if (eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {

                    adharDTO.setUid(parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR));
                    adharDTO.setName(parser.getAttributeValue(null, DataAttributes.AADHAR_NAME_ATTR));
                    adharDTO.setGender(parser.getAttributeValue(null, DataAttributes.AADHAR_GENDER_ATTR));
                    adharDTO.setYearOfBirth(parser.getAttributeValue(null, DataAttributes.AADHAR_YOB_ATTR));
                    adharDTO.setCareOf(parser.getAttributeValue(null, DataAttributes.AADHAR_CO_ATTR));
                    adharDTO.setHouse(parser.getAttributeValue(null, DataAttributes.AADHAR_HOUSE_ATTR));
                    adharDTO.setStreet(parser.getAttributeValue(null, DataAttributes.AADHAR_STREET_ATTR));
                    adharDTO.setLoc(parser.getAttributeValue(null, DataAttributes.AADHAR_LOC_ATTR));
                    adharDTO.setVillageTehsil(parser.getAttributeValue(null, DataAttributes.AADHAR_VTC_ATTR));
                    adharDTO.setPostOffice(parser.getAttributeValue(null, DataAttributes.AADHAR_PO_ATTR));
                    adharDTO.setDistrict(parser.getAttributeValue(null, DataAttributes.AADHAR_DIST_ATTR));
                    adharDTO.setSubDistrict(parser.getAttributeValue(null, DataAttributes.AADHAR_SUBDIST_ATTR));
                    adharDTO.setState(parser.getAttributeValue(null, DataAttributes.AADHAR_STATE_ATTR));
                    adharDTO.setPostCode(parser.getAttributeValue(null, DataAttributes.AADHAR_PC_ATTR));

                } else if (eventType == XmlPullParser.END_TAG) {

                } else if (eventType == XmlPullParser.TEXT) {

                }
                // update eventType
                eventType = parser.next();
            }
            displayScannedData(adharDTO);
        } catch (XmlPullParserException e) {
            Toast.makeText(getActivity(), "The new digitally signed QR code can be read by ONLY using UIDAI's " +
                    "windows based application and validate it against UIDAI digital signatures or its a invalid QR code", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void displayScannedData(AdharDTO adharDTO) {
        if (mainLayout.getChildCount() > 0) {
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View view = mainLayout.getChildAt(i);
                if (view instanceof FrameLayout) {
                    FrameLayout frameLayout = (FrameLayout) view;
                    TextInputLayout viewGroup = (TextInputLayout) frameLayout.getChildAt(0);
                    EditText editText = viewGroup.getEditText();
                    String fieldName = (String) editText.getTag(R.id.editText);
                    if (fieldName.equalsIgnoreCase("Name")) {
                        editText.setText(adharDTO.getName());
                    } else if (fieldName.equalsIgnoreCase("Address")) {
                        editText.setText(adharDTO.getVillageTehsil());
                    } else if (fieldName.equalsIgnoreCase("District")) {
                        editText.setText(adharDTO.getDistrict());
                    } else if (fieldName.equalsIgnoreCase("State")) {
                        editText.setText(adharDTO.getState());
                    } else if (fieldName.equalsIgnoreCase("Gender")) {
                        if (adharDTO.getGender().equalsIgnoreCase("M")) {
                            editText.setText("Male");
                        } else {
                            editText.setText("Female");
                        }
                    } else if (fieldName.equalsIgnoreCase("Aadhaar")) {
                        editText.setText(adharDTO.getUid());
                    } else if (fieldName.equalsIgnoreCase("YOB")) {
                        editText.setText(adharDTO.getYearOfBirth());
                    } else if (fieldName.equalsIgnoreCase("CO")) {
                        editText.setText(adharDTO.getCareOf());
                    } else if (fieldName.equalsIgnoreCase("PO")) {
                        editText.setText(adharDTO.getPostOffice());
                    } else if (fieldName.equalsIgnoreCase("DIST")) {
                        editText.setText(adharDTO.getDistrict());
                    } else if (fieldName.equalsIgnoreCase("SUBDIST")) {
                        editText.setText(adharDTO.getSubDistrict());
                    } else if (fieldName.equalsIgnoreCase("PINCODE")) {
                        editText.setText(adharDTO.getPostCode());
                    } else if (fieldName.equalsIgnoreCase("Street")) {
                        editText.setText(adharDTO.getStreet());
                    } else if (fieldName.equalsIgnoreCase("House")) {
                        editText.setText(adharDTO.getHouse());
                    } else if (fieldName.equalsIgnoreCase("LOC")) {
                        editText.setText(adharDTO.getLoc());
                    } else if (fieldName.equalsIgnoreCase("VTC")) {
                        editText.setText(adharDTO.getVillageTehsil());
                    }
                }
            }
        }
    }

    @Override
    public void onAdharDataRetrieved(int requestCode, int resultCode, Intent data) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            // process received data
            if (scanContent != null && !scanContent.isEmpty()) {
                processScannedData(scanContent);
            } else {
                Toast toast = Toast.makeText(getActivity(), "Scan Cancelled", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void loadDynamicForm(ArrayList<ProfileForm> dynamicForm) {
        mainLayout.removeAllViews();
        latitudeEditText = null;
        longitudeEditText = null;
        // addIdentifierField();
        for (ProfileForm profileform : dynamicForm) {
            addFieldToLayout(profileform);
        }
        containerMap.setVisibility(View.VISIBLE);
//        if (TextUtils.isEmpty(profileId)) {
//        }
    }

    private void addIdentifierField() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_edit_text, null);
        EditText editText = view.findViewById(R.id.editText);
        editText.setMaxLines(1);
        editText.setHint("Identifier");
        editText.setTag(R.id.editText, "Identifier");
        mainLayout.addView(view);
    }

    private void addFieldToLayout(final ProfileForm profileform) {
        if (profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_VARCHAR)
                || profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_NUMBER) ||
                profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_PHONE)) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_edit_text, null);
            EditText editText = view.findViewById(R.id.editText);
            View imgDropDown = view.findViewById(R.id.img_drop_down);
            imgDropDown.setVisibility(View.GONE);
            TextInputLayout textInputLayout = view.findViewById(R.id.text_inout_layout);
            editText.setMaxLines(1);
            if (profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_NUMBER)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            } else if (profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_PHONE)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }

            textInputLayout.setHint(profileform.name);
            if (MyApplication.getInstance().getCurrentLocation() != null) {
                if (profileform.name.equalsIgnoreCase("latitude")) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    editText.setClickable(false);
                    editText.setText(String.valueOf(MyApplication.getInstance().getCurrentLocation().getLatitude()));
                } else if (profileform.name.equalsIgnoreCase("longitude")) {
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    editText.setClickable(false);
                    editText.setText(String.valueOf(MyApplication.getInstance().getCurrentLocation().getLongitude()));
                }
            }
            editText.setTag(R.id.editText, profileform.name);
            if (profileform.name.equalsIgnoreCase("age")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (profileform.name.equalsIgnoreCase("Phone") ||
                    profileform.name.equalsIgnoreCase("Phone2")) {
                //setting max length of phone number as 10 character....
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(10);
                editText.setFilters(filterArray);
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            } else if (profileform.name.equalsIgnoreCase("Email")) {
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            mainLayout.addView(view);
        } else if (profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_LIST) ||
                profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_DATE)) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_edit_text, null);
            EditText editText = view.findViewById(R.id.editText);
            View imgDropDown = view.findViewById(R.id.img_drop_down);
            imgDropDown.setVisibility(View.VISIBLE);
            TextInputLayout textInputLayout = view.findViewById(R.id.text_inout_layout);
            editText.setMaxLines(1);
            textInputLayout.setHint(profileform.name);
            editText.setTag(R.id.editText, profileform.name);
            editText.setCursorVisible(false);
            editText.setFocusableInTouchMode(false);
            editText.setOnClickListener(view1 -> {
                if (profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_LIST)) {
                    CustomDialogues.showListPop(getActivity(), profileform.name, (EditText) view1, profileform.value);
                } else {
                    showDatePickerDialog((EditText) view1);
                }
            });
            mainLayout.addView(view);
        } else if (profileform.type.equalsIgnoreCase(Constants.DynamicUiTypes.TYPE_BRANCH)) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_edit_text, null);
            EditText editText = view.findViewById(R.id.editText);
            View imgDropDown = view.findViewById(R.id.img_drop_down);
            imgDropDown.setVisibility(View.VISIBLE);
            TextInputLayout textInputLayout = view.findViewById(R.id.text_inout_layout);
            editText.setMaxLines(1);
            textInputLayout.setHint(profileform.name);
            editText.setTag(R.id.editText, profileform.name);
            editText.setCursorVisible(false);
            editText.setFocusableInTouchMode(false);

            for (BranchTree bt : branchTrees) {
                Log.i("1", " " + bt.id);
                Log.i("2", " " + bt.branchCode);
                Log.i("3", " " + bt.name);
                Log.i("4", " " + bt.treeLevel);
                Log.i("5", " " + bt.children);
                Log.i("6", " " + bt.currentBranchId);
                Log.i("7", " " + bt.parentBranchId);
            }

            editText.setOnClickListener(view1 -> {
               showBranch(getActivity(), profileform.name, (EditText) view1, branchTrees);

//                getBranchTrees(new WebService.OnServiceResponseListener() {
//                    @Override
//                    public void onApiCallResponseSuccess(String url, String object) {
//                        hideProgressBar();
//                        if (!TextUtils.isEmpty(object)) {
//                            Type typeMyType = new TypeToken<ArrayList<BranchTree>>() {
//                            }.getType();
//                            List<BranchTree> branchTree = new Gson().fromJson(object, typeMyType);
//                            showBranch(getActivity(), profileform.name, (EditText) view1, branchTree.get(0).children);
//                        }
//                    }
//
//                    @Override
//                    public void onApiCallResponseFailure(String errorMessage) {
//                        hideProgressBar();
//                    }
//                });

                Log.i("after", "getBranchTrees");
            });
            mainLayout.addView(view);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToOnBoard() {
        iOnFragmentChangeListener.onFragmentChanged(Constants.ONBOARD_FRAGMENT, null);
    }

    @Override
    public void showAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
    }

    @Override
    public void loadDynamicSpinner(ArrayList<KycDynamicForm> allDynamicFormList, HashMap<String, KycDynamicForm> allDymamicFormHashmap) {
        if (typeSelectionSP.isShown()) {
            loadSpinner(allDynamicFormList);
        }
        this.allDynamicSpinnerHashMap = allDymamicFormHashmap;
        KycDynamicForm selectedForm = allDynamicFormList.get(0);
        kycPresenter.getAllDynamicSelectedFormFromServer(selectedForm.profileFormId);
    }

    @Override
    public void loadDynamicFormToView
            (ArrayList<KycDynamicForm> allDynamicFormList, HashMap<String, KycDynamicForm> allDynamicFormHashMap) {
        this.allDynamicFormHashMap = allDynamicFormHashMap;
        loadTypeSelectionGroup(allDynamicFormList);
        if (radioTypeSelection != null && radioTypeSelection.isShown()) {
            radioTypeSelection.check(0);
        }
//        if (typeSelectionSP.isShown()) {
//            loadSpinner(allDynamicFormList);
//        }
        KycDynamicForm selectedForm = allDynamicFormList.get(0);
        for (int i = 0; i < allDynamicFormList.size(); i++) {
            KycDynamicForm kycDynamicForm = allDynamicFormList.get(i);
            if (formId != 0 && Integer.parseInt(kycDynamicForm.profileFormId) == formId) {
                selectedForm = kycDynamicForm;
            }
        }
        loadDynamicForm(selectedForm.allProfileFormsList);
        selectedFormId = selectedForm.profileFormId;
        if (!TextUtils.isEmpty(profileJsonString)) {
            Log.i("profileJsonString", "profileJsonString :" + profileJsonString);
            mapSavedDataToViews(profileJsonString);
        }
    }

    private void loadSpinner(ArrayList<KycDynamicForm> allDynamicFormList) {
        String[] country = new String[allDynamicFormList.size()];
        ArrayList<Integer> profileFormIds = new ArrayList<>();
        for (int i = 0; i < allDynamicFormList.size(); i++) {
            country[i] = allDynamicFormList.get(i).formName;
            profileFormIds.add(Integer.parseInt(allDynamicFormList.get(i).profileFormId));
        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.item_spinner, android.R.id.text1, country);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSelectionSP.setAdapter(spinnerArrayAdapter);
        if (formId != 0) {
            typeSelectionSP.setSelection(profileFormIds.indexOf(formId));
        }
    }

    private void loadTypeSelectionGroup(ArrayList<KycDynamicForm> allDynamicFormList) {
        if (radioTypeSelection == null) {
            return;
        }
        for (int i = 0; i < allDynamicFormList.size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            int dimension = (int) getActivity().getResources().getDimension(R.dimen._10dp);
            radioButton.setPadding(dimension, dimension, dimension, dimension);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setId(i);
            radioButton.setButtonDrawable(null);
            radioButton.setBackgroundResource(R.drawable.radio_bg_selector);
            radioButton.setTextColor(getResources().getColorStateList(R.color.radio_text_selector));
            radioButton.setText(allDynamicFormList.get(i).formName);
            radioButton.setLayoutParams(params);
            radioTypeSelection.addView(radioButton);
        }
    }

    private int calculateZoomLevel() {
        double equatorLength = 10075004; // in meters
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double metersPerPixel = equatorLength / 2000;
        int zoomLevel = 1;
        while ((metersPerPixel * width) > 2000) {
            metersPerPixel /= 2;
            ++zoomLevel;
        }
        return zoomLevel;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (mainLayout == null) {
            return;
        }
        Location location = MyApplication.getInstance().getCurrentLocation();
        if (latitude != 0 && longitude != 0) {
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, calculateZoomLevel()));
        } else if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, calculateZoomLevel()));
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnCameraIdleListener(() -> {
            LatLng target = googleMap.getCameraPosition().target;
            double targetLat = target.latitude, targetLong = target.longitude;
            if (target.latitude < 0.5) {
                targetLat = 0.0;
            }
            if (target.longitude < 0.5) {
                targetLong = 0.0;
            }
//            if (latitude != 0 && longitude != 0) {
//                targetLat = latitude;
//                targetLong = longitude;
//            }
            if (latitudeEditText != null && longitudeEditText != null) {
                latitudeEditText.setText(String.valueOf(targetLat));
                longitudeEditText.setText(String.valueOf(targetLong));
                return;
            }
            for (int i = 0; i < mainLayout.getChildCount(); i++) {
                View view = mainLayout.getChildAt(i);
                if (view instanceof FrameLayout) {
                    FrameLayout frameLayout = (FrameLayout) view;
                    TextInputLayout viewGroup = (TextInputLayout) frameLayout.getChildAt(0);
                    EditText edittext = viewGroup.getEditText();
                    if (edittext.getTag(R.id.editText) != null) {
                        if (edittext.getTag(R.id.editText).equals("longitude") || edittext.getTag(R.id.editText).equals("Longitude")) {
                            longitudeEditText = edittext;
                            longitudeEditText.setText(String.valueOf(targetLong));
                        } else if (edittext.getTag(R.id.editText).equals("latitude") || edittext.getTag(R.id.editText).equals("Latitude")) {
                            latitudeEditText = edittext;
                            latitudeEditText.setText(String.valueOf(targetLat));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDataRetrieved(int requestCode, int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        try {
            showLoading();
            String filename = !TextUtils.isEmpty(fileNameToUpload) ? fileNameToUpload + "_" + UtilityMethods.getDateFormat() + ".jpeg" : UtilityMethods.getDateFormat() + ".jpeg";
            InputStream iStream = getActivity().getContentResolver().openInputStream(selectedImage);
            byte[] inputData = UtilityMethods.getBytes(iStream);
            HashMap<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
            params.put("files", new VolleyMultipartRequest.DataPart(filename, inputData, "image/jpeg"));
            url = url.replace("WORKFLOW_PROFILE_ID", profileId);
            WebService.getInstance().apiMultipart(url, params, new WebService.OnServiceResponseListener() {
                @Override
                public void onApiCallResponseSuccess(String url, String object) {
                    if (url.contains("profilePicture")) {
                        try {
                            JSONArray jsonArray = new JSONArray(object);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String downloadUri = jsonObject.getString("fileDownloadUri");
                            String fileName = jsonObject.getString("fileName");
                            String imageUrl = downloadUri + "?access_token=" + PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
                            Glide.with(getActivity()).load(imageUrl).into(imgProfile);
                            imgCamera.setVisibility(View.GONE);
                            textApplicantPictureName.setText(fileName);
                            applicantPictureLL.setClickable(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    }

    public void setTabIndex(int index) {
        this.index = index;
    }
}
