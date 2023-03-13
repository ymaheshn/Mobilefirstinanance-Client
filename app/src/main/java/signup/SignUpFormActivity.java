package signup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;
import com.odedtech.mff.client.R;
import com.odedtech.mff.client.databinding.ActivitySignUpFormBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Utilities.AlertDialogUtils;
import Utilities.DateUtil;
import Utilities.PreferenceConnector;
import Utilities.ProgressBar;
import adapters.SignUpViewPagerAdapter;
import butterknife.ButterKnife;
import login.LoginActivity;
import networking.WebService;
import shufpti.ShuftiModel;
import signup.listener.OnBranchInfoListener;
import signup.listener.OnContinueButtonClickListener;
import signup.listener.SelectedBranchInterface;
import signup.listener.SignUpDetailsInterface;
import signup.model.Branches;
import signup.model.SignUpFormResult;


public class SignUpFormActivity extends AppCompatActivity implements View.OnClickListener, SelectedBranchInterface, OnContinueButtonClickListener, OnBranchInfoListener, SignUpDetailsInterface {

    private SignUpViewPagerAdapter signUpViewPagerAdapter;
    private ArrayList<String> bodyLabelList = new ArrayList<>();
    private ArrayList<String> bodyTypeList = new ArrayList<>();
    private ArrayList<String> bodyValueList = new ArrayList<>();
    private ArrayList<String> bodyDescriptionList = new ArrayList<>();
    private ActivitySignUpFormBinding activitySignUpFormBinding;
    private ArrayList<String> signUpData = new ArrayList<>();
    private String signUpLabelText;
    private String enteredEditText;
    int position;
    private SignUpViewPagerAdapter.ViewPagerHolder viewPagerHolder;
    private String branchName;
    private View.OnClickListener onClickListener;
    private ArrayList<String> branchNameList = new ArrayList<>();
    private String branchNameItem;
    private String branchTextName;
    public static final int REQUEST_CODE = 1;
    private String branchName1;
    private String branchItem = null;
    private int branchId;
    private RecyclerView.ViewHolder holder;
    private static HashMap<String, Object> signupFormPayload = new HashMap<>();
    private int formId;
    private String emailSignUp;
    private String mobileSignUp;
    private SignUpFormResult signUpFormResult;
    private Map<String, String> paramsJsonObject;
    private boolean isFromDateOfBirth;
    private Activity activity;
    private ShuftiModel shuftiModel;

    private final Map<String, Object> paramsIdentifier = new HashMap<>();
    private final Map<String, Object> paramsNationalID = new HashMap<>();
    private final Map<String, Object> paramsEmail = new HashMap<>();
    private final Map<String, Object> paramsMobileNumber = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpFormBinding = ActivitySignUpFormBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpFormBinding.getRoot());
        ButterKnife.bind(SignUpFormActivity.this);

        //   activitySignUpFormBinding.continueButton.setOnClickListener(this);
        activitySignUpFormBinding.backButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        bodyLabelList = bundle.getStringArrayList("bodyLabelList");
        bodyTypeList = bundle.getStringArrayList("bodyTypeList");
        bodyValueList = bundle.getStringArrayList("bodyValueList");
        bodyDescriptionList = bundle.getStringArrayList("bodyDescriptionList");
        emailSignUp = bundle.getString("emailSignUp");
        mobileSignUp = bundle.getString("mobileSignUp");
        formId = bundle.getInt("signUpFormId");

        activitySignUpFormBinding.viewPagerSignUp.setUserInputEnabled(false);

        branchNameList.addAll(bodyLabelList);
        signUpViewPagerAdapter = new SignUpViewPagerAdapter(getApplicationContext(), bodyLabelList, bodyValueList, bodyTypeList, bodyDescriptionList, emailSignUp, mobileSignUp, this, this);
        activitySignUpFormBinding.viewPagerSignUp.setAdapter(signUpViewPagerAdapter);


    }

    private void submitSignUpFormDetails(SignUpFormResult signUpFormResult) {

        PreferenceConnector.clearShuftiResponse(getApplicationContext());

        String shuftiResponse = PreferenceConnector.getShuftiResponse(this);
        shuftiModel = new Gson().fromJson(shuftiResponse, ShuftiModel.class);
        Log.e("Response", shuftiResponse);

        this.signUpFormResult = signUpFormResult;
        String url = "https://dev.mobilefirstfinance.com:7190/mff/api/createClientUser";
        Map<String, Object> params = new HashMap<>();
        params.put("Is your age above 18", signUpFormResult.ageAbove18);
        params.put("FATCA Declaration", signUpFormResult.fatcaDeclaration);
        params.put("Domicile", signUpFormResult.domicile);
        params.put("ID Verification", signUpFormResult.idVerification);
        params.put("Identifier", signUpFormResult.identifier);
        params.put("User Password", signUpFormResult.userPassword);
        params.put("First Name", signUpFormResult.firstName);
        params.put("Last Name", signUpFormResult.lastName);
        params.put("National ID", signUpFormResult.nationalID);
        params.put("Date of Birth", signUpFormResult.dateOfBirth);
        params.put("Mobile Number", signUpFormResult.mobileNumber);
        params.put("Email", signUpFormResult.email);
        params.put("Hierarchy", signUpFormResult.hierarchy);
        params.put("FormID", signUpFormResult.formID);
        params.put("Choose your plan", signUpFormResult.choosePlan);
        ProgressBar.showProgressDialog(this);
        try {
            activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.VISIBLE);
            WebService.getInstance().apiPostRequestObjectCall(url, params, new WebService.OnServiceResponseListener() {
                @Override
                public void onApiCallResponseSuccess(String url, String object) {
                    ProgressBar.dismissDialog();
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        int status = jsonObject.getInt("status");
                        if (status == 200) {
                            activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Signed Successfully!! Please login here", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            ProgressBar.dismissDialog();
                        }
                    } catch (JSONException e) {
                        ProgressBar.dismissDialog();
                        e.printStackTrace();
                    }
                    activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.GONE);
                }
                @Override
                public void onApiCallResponseFailure(String errorMessage) {
                    ProgressBar.dismissDialog();
                    activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    showAlert(errorMessage);
                    Log.i("Error api", errorMessage);
                }
            });
        } catch (Exception e) {
            ProgressBar.dismissDialog();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        ViewPager2 viewPager = activitySignUpFormBinding.viewPagerSignUp;
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private int getItem(int i) {
        return activitySignUpFormBinding.viewPagerSignUp.getCurrentItem() + i;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_button) {
            onBackPressed();
        }
    }

    @Override
    public void getSignUpData(ArrayList<String> signUpData, SignUpViewPagerAdapter.ViewPagerHolder viewPagerHolder, int position, String signUpLabelText, String enteredEditText) {
        this.signUpData.addAll(signUpData);
        this.signUpLabelText = signUpLabelText;
        this.enteredEditText = enteredEditText;
        this.position = position;
        this.viewPagerHolder = viewPagerHolder;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == 11 && data != null) {
                activitySignUpFormBinding.linearSelectedBranch.setVisibility(View.VISIBLE);
                branchItem = data.getStringExtra("branchName");
                branchId = data.getIntExtra("branchId", 0);
                activitySignUpFormBinding.textSelectedBranch.setText(branchItem);
            }
            else if (resultCode == 12){
                activitySignUpFormBinding.viewPagerSignUp.setCurrentItem(getItem(+1), true);
            }
            else {
                activitySignUpFormBinding.linearSelectedBranch.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void selectedBranch(Integer position, Branches branches) {
        this.branchName = branches.getBranchName();
    }

    @Override
    public void onClick(View view, int position, String branchName, String label, Object value, SignUpViewPagerAdapter.ViewPagerHolder holder,
                        boolean isFromBranch, boolean isFromDateOFBirth, boolean isFromIdVerification, boolean isFromSkip) {
        signupFormPayload.put(label, value);

        if (view.getId() == R.id.continue_button) {
            if (activitySignUpFormBinding.viewPagerSignUp.getCurrentItem() == bodyLabelList.size() - 1) {

                Object ageAbove18 = signupFormPayload.get("Is your age above 18");
                Object fatcaDeclaration = signupFormPayload.get("FATCA Declaration");
                Object domicile = signupFormPayload.get("Domicile");
                Object idVerification = signupFormPayload.get("ID Verification");
                Object identifier = signupFormPayload.get("Identifier");
                Object userPassword = signupFormPayload.get("User Password");
                Object firstName = signupFormPayload.get("First Name");
                Object lastName = signupFormPayload.get("Last Name");
                Object nationalId = signupFormPayload.get("National ID");
                Object dataOfBirth = signupFormPayload.get("Date of Birth");
                Object mobileNumber = signupFormPayload.get("Mobile Number");
                Object choosePlan = signupFormPayload.get("Choose your plan");

                signupFormPayload.put("BranchId", branchId);
                Object linkBranches = signupFormPayload.get("BranchId");

                signupFormPayload.put("FormID", formId);
                Object formID = signupFormPayload.get("FormID");

                Object email = signupFormPayload.get("Email");
                Object hierarchy = signupFormPayload.get("BranchId");

                Log.i("signUp_values", signupFormPayload.values().toString());

                signUpFormResult = new SignUpFormResult();
                signUpFormResult.setAgeAbove18(ageAbove18);
                signUpFormResult.setFatcaDeclaration(fatcaDeclaration);
                signUpFormResult.setDomicile(domicile);
                signUpFormResult.setIdVerification(idVerification);
                signUpFormResult.setIdentifier(identifier);
                signUpFormResult.setUserPassword(userPassword);
                signUpFormResult.setFirstName(firstName);
                signUpFormResult.setLastName(lastName);
                signUpFormResult.setNationalID(nationalId);
                signUpFormResult.setDateOfBirth(dataOfBirth);
                signUpFormResult.setMobileNumber(mobileNumber);
                signUpFormResult.setEmail(email);
                signUpFormResult.setHierarchy(hierarchy);
                signUpFormResult.setFormID(formID);
                signUpFormResult.setChoosePlan(choosePlan);

                submitSignUpFormDetails(signUpFormResult);
            } else {

                activitySignUpFormBinding.linearSelectedBranch.setVisibility(View.GONE);
                if (!Objects.requireNonNull(holder.textInputEditText.getText()).toString().isEmpty() ||
                        !Objects.requireNonNull(holder.completeTextView.getText()).toString().isEmpty() ||
                        !Objects.requireNonNull(holder.dateOfBirthEditText.getText()).toString().isEmpty()) {

                    switch (label) {
                        case "FATCA Declaration":
                            try {
                                paramsIdentifier.put("FATCA Declaration", value.toString());
                                validateEnteredDetails(paramsIdentifier);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Identifier":
                            try {
                                paramsIdentifier.put("Identifier", value.toString());
                                validateEnteredDetails(paramsIdentifier);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "National ID":
                            try {
                                paramsNationalID.put("National ID", value.toString());
                                validateEnteredDetails(paramsNationalID);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Email":
                            try {
                                paramsEmail.put("Email", value.toString());
                                validateEnteredDetails(paramsEmail);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "Mobile Number":
                            try {
                                paramsMobileNumber.put("Mobile Number", value.toString());
                                validateEnteredDetails(paramsMobileNumber);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            activitySignUpFormBinding.viewPagerSignUp.setCurrentItem(getItem(+1), true);
                            break;
                    }

                } else if (branchItem != null) {
                    activitySignUpFormBinding.linearSelectedBranch.setVisibility(View.GONE);
                    activitySignUpFormBinding.viewPagerSignUp.setCurrentItem(getItem(+1), true);
                } else {
                    AlertDialogUtils.getAlertDialogUtils().showOkAlert(this, "Please enter valid " + bodyLabelList.get(position));
                }
            }
        }
        if (view.getId() == R.id.date_of_birth_edit_text) {
            getDate(holder);
        }
        if (view.getId() == R.id.branch_text_input_edit_text) {
            Intent intent = new Intent(SignUpFormActivity.this, BranchSearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

        if (view.getId() == R.id.skip_text) {
            activitySignUpFormBinding.viewPagerSignUp.setCurrentItem(getItem(+1), true);
        }
    }

    private void validateEnteredDetails(Map<String, Object> paramsIdentifier) throws JSONException {

        activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.VISIBLE);
        String url = "https://dev.mobilefirstfinance.com:7190/mff/api/userValidation";
        WebService.getInstance().apiPostRequestObjectCall(url, paramsIdentifier, new WebService.OnServiceResponseListener() {
            @Override
            public void onApiCallResponseSuccess(String url, String object) {
                activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    int status = jsonObject.getInt("status");
                    String message = jsonObject.getString("message");
                    if (status == 200) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        activitySignUpFormBinding.viewPagerSignUp.setCurrentItem(getItem(+1), true);
                    } else if (status == 412) {
                        showAlert(message);
                     //   AlertDialogUtils.getAlertDialogUtils().showOkAlert(activity, message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.GONE);
                    showAlert(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiCallResponseFailure(String errorMessage) {
                activitySignUpFormBinding.progressBarSignUpForm.setVisibility(View.GONE);
                showAlert(errorMessage);
            }
        });

    }

    private void showAlert(String message) {
        AlertDialogUtils.getAlertDialogUtils().displayAlertMessage(this, message);
    }

    private void getDate(SignUpViewPagerAdapter.ViewPagerHolder holder) {
        Calendar dateAndTime = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpFormActivity.this, (view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String date = DateUtil.format(calendar.getTime(), "YYYY-MM-dd");
            holder.dateOfBirthEditText.setText(date);

        }, dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onBranchInfoListener(String branch) {
        this.branchItem = branch;
    }
}