package signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;
import com.mukesh.OtpView;
import com.odedtech.mff.client.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import network.MFFApiWrapper;
import networking.WebService;
import retrofit2.Call;
import retrofit2.Callback;
import signup.model.Carrier;
import signup.model.Payload;
import signup.model.PayloadVerifyOtp;
import signup.model.SignUpUserForm;
import signup.model.VerifyOtpResponse;

public class SignUpEmailAndMobileActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.send_email_otp_button)
    MaterialButton buttonEmailSendOtp;

    @BindView(R.id.send_email_otp_submit_button)
    MaterialButton buttonEmailVerifyOtp;

    @BindView(R.id.email_edit_text)
    AppCompatEditText emailEditText;

    @BindView(R.id.mobile_edit_text)
    AppCompatEditText mobileEditText;

    @BindView(R.id.text_resend_otp)
    AppCompatTextView resendTextOTP;

    @BindView(R.id.otp_email_view)
    OtpView optViewEmail;

    @BindView(R.id.otp_mobile_view)
    OtpView optViewMobile;

    @BindView(R.id.view_switcher_sign_up)
    ViewSwitcher viewSwitcher;

    @BindView(R.id.sign_up_text_type)
    AppCompatTextView signUpTextType;

    @BindView(R.id.text_invalid_otp)
    AppCompatTextView invalidOtpText;

    @BindView(R.id.enter_text_otp_mobile)
    AppCompatTextView enterOtpTextViewMobile;

    @BindView(R.id.enter_text_otp)
    AppCompatTextView enterOtpTextViewMail;

    @BindView(R.id.linear_otp_resend_text)
    LinearLayout linearLayoutOtpTextView;

    @BindView(R.id.progress_bar_sign_up)
    ProgressBar progressBar;


    @BindView(R.id.country_code_picker)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.back_button_sign_up)
    AppCompatImageButton backImageButton;

    @BindView(R.id.button_signUp)
    AppCompatButton callSignUpApi;

    private PayloadVerifyOtp payloadVerifyOtp;
    private Payload payload;
    private Carrier carrier;
    private String emailToText;
    private String mobileToText;
    private VerifyOtpResponse verifyOtpResponse;
    private Boolean isFromEmail;
    private SignUpUserForm.FormBody formBodies;
    private SignUpUserForm.FormHeader formHeader;
    private ArrayList<SignUpUserForm.UserFormTemplate> userFormTemplate = new ArrayList<>();
    private ArrayList<String> bodyLabelList = new ArrayList<>();
    private ArrayList<String> bodyTypeList = new ArrayList<>();
    private ArrayList<String> bodyValueList = new ArrayList<>();
    private ArrayList<String> descriptionList = new ArrayList<>();
    private String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email_and_mobile);

        ButterKnife.bind(SignUpEmailAndMobileActivity.this);
        buttonEmailSendOtp.setOnClickListener(this);
        viewSwitcher.setOnClickListener(this);
        signUpTextType.setOnClickListener(this);
        buttonEmailVerifyOtp.setOnClickListener(this);
        resendTextOTP.setOnClickListener(this);
        backImageButton.setOnClickListener(this);
        callSignUpApi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.send_email_otp_button) {
            callSendOtpButton();
        }
        if (v.getId() == R.id.text_resend_otp) {
            callSendOtpButton();
        }
        if (v.getId() == R.id.sign_up_text_type) {
            //  optViewMobile.setVisibility(View.GONE);
            if (signUpTextType.getText().toString().equals("Signup using mobile number")) {
                switchSignUpTypeEmail();
                signUpTextType.setText("Signup with email");
            } else if (signUpTextType.getText().toString().equals("Signup with email")) {
                switchSignUpTypeMobile();
                signUpTextType.setText("Signup using mobile number");
            }
        }
        if (v.getId() == R.id.send_email_otp_submit_button) {
            emailOtpVerifyButton();
        }

        if (v.getId() == R.id.back_button_sign_up) {
            onBackPressed();
        }
        if (v.getId() == R.id.button_signUp) {
            callSignUpUserFormApi();
        }

    }

    private void emailOtpVerifyButton() {
        progressBar.setVisibility(View.VISIBLE);
        if (isFromEmail) {
            payloadVerifyOtp = new PayloadVerifyOtp(emailToText, optViewEmail.getText().toString());
            MFFApiWrapper.getInstance().service.setPayloadVerifyEmail(payloadVerifyOtp).enqueue(new Callback<VerifyOtpResponse>() {
                @Override
                public void onResponse(@NonNull Call<VerifyOtpResponse> call, @NonNull retrofit2.Response<VerifyOtpResponse> response) {
                    if (response.body() != null) {
                        progressBar.setVisibility(View.GONE);
                        verifyOtpResponse = response.body();
                        Toast.makeText(getApplicationContext(), "Verification successful", Toast.LENGTH_LONG).show();
                        callSignUpUserFormApi();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        invalidOtpText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<VerifyOtpResponse> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    invalidOtpText.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        } else {
            payloadVerifyOtp = new PayloadVerifyOtp("+91" + mobileToText, optViewMobile.getText().toString());
            MFFApiWrapper.getInstance().service.setPayloadVerifyMobile(payloadVerifyOtp).enqueue(new Callback<VerifyOtpResponse>() {
                @Override
                public void onResponse(@NonNull Call<VerifyOtpResponse> call, @NonNull retrofit2.Response<VerifyOtpResponse> response) {
                    if (response.body() != null) {
                        progressBar.setVisibility(View.GONE);
                        verifyOtpResponse = response.body();
                        Toast.makeText(getApplicationContext(), "Verification successful", Toast.LENGTH_LONG).show();
                        callSignUpUserFormApi();
                    } else if (response.code() == 412) {
                        invalidOtpText.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<VerifyOtpResponse> call, @NonNull Throwable t) {
                    invalidOtpText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void callSignUpUserFormApi() {
        Utilities.ProgressBar.showProgressDialog(this);
        String url = "https://dev.mobilefirstfinance.com:7190/mff/api/clientUserform/Customer";
        WebService.getInstance().apiGetRequestCall(url, new WebService.OnServiceResponseListener() {
            @Override
            public void onApiCallResponseSuccess(String url, String response) {
                Utilities.ProgressBar.dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //JSONObject userFormTemplateID = jsonObject.getJSONObject("userFormTemplateID");
                    int formId = jsonObject.getInt("userFormTemplateID");
                    JSONObject userFormTemplateObject = jsonObject.getJSONObject("userFormTemplate");
                    JSONArray formHeaderJsonArray = userFormTemplateObject.getJSONArray("Form Header");
                    if (formHeaderJsonArray.length() > 0) {
                        for (int i = 0; i < formHeaderJsonArray.length(); i++) {
                            JSONObject jsonObject1 = formHeaderJsonArray.getJSONObject(i);
                            String type = jsonObject1.getString("Type");
                            String value = jsonObject1.getString("Value");
                            String label = jsonObject1.getString("Label");
                            formHeader = new SignUpUserForm.FormHeader(type, value, label);
                        }
                    }
                    JSONArray formBodyJsonArray = userFormTemplateObject.getJSONArray("Form Body");
                    HashMap<Integer, String> map = new HashMap<>();
                    if (formBodyJsonArray.length() > 0) {
                        for (int i = 0; i < formBodyJsonArray.length(); i++) {
                            JSONObject jsonObject1 = formBodyJsonArray.getJSONObject(i);
                            String type = jsonObject1.getString("Type");
                            String value = jsonObject1.getString("Value");
                            String label = jsonObject1.getString("Label");

                            if (jsonObject1.has("Description")) {
                              // String description = jsonObject1.getString("Description");
                                map.put(i, jsonObject1.getString("Description"));
                            }

                           /* if (type.equals("Date") && type.equals("Branch") && type.equals("Phone") && type.equals("Email")) {
                                description = jsonObject1.getString("");
                            } else {
                                description = jsonObject1.getString("Description");
                                descriptionList.add(description);

                            }*/
                            formBodies = new SignUpUserForm.FormBody(type, value, label, map.get(i));
                            bodyLabelList.add(label);
                            bodyTypeList.add(type);
                            bodyValueList.add(value);
                            descriptionList.add(map.get(i));

                        }
                    }
                    // userFormTemplate.add(formBodies);
                    Intent intent = new Intent(SignUpEmailAndMobileActivity.this, SignUpFormActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("formHeader", formHeader);
                    bundle.putParcelable("formBody", formBodies);
                    bundle.putStringArrayList("bodyLabelList", bodyLabelList);
                    bundle.putStringArrayList("bodyTypeList", bodyTypeList);
                    bundle.putStringArrayList("bodyValueList", bodyValueList);
                    bundle.putStringArrayList("bodyDescriptionList", descriptionList);
                    bundle.putString("emailSignUp", emailToText);
                    bundle.putString("mobileSignUp", mobileToText);
                    bundle.putInt("signUpFormId", formId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Error api", e.getMessage());
                }
            }

            @Override
            public void onApiCallResponseFailure(String errorMessage) {
                Utilities.ProgressBar.dismissDialog();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                Log.i("Error api", errorMessage);
            }
        });


    }

    private void switchSignUpTypeEmail() {
        viewSwitcher.showNext();
    }

    private void switchSignUpTypeMobile() {
        viewSwitcher.showPrevious();
    }

    private void callSendOtpButton() {
        Utilities.ProgressBar.showProgressDialog(this);
        progressBar.setVisibility(View.VISIBLE);
        emailToText = Objects.requireNonNull(emailEditText.getText()).toString().trim();
        mobileToText = String.valueOf(mobileEditText.getText());

        Pattern ptrn = Pattern.compile("(0/91)?[6-9][0-9]{9}");
        Matcher match = ptrn.matcher(mobileToText);

        emailEditText.setFocusable(false);
        emailEditText.setFocusableInTouchMode(false);
        emailEditText.setClickable(false);

        mobileEditText.setFocusable(false);
        mobileEditText.setFocusableInTouchMode(false);
        mobileEditText.setClickable(false);

        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            payload = new Payload(emailToText, "email");
            //  Utilities.ProgressBar.showProgressDialog(this);
            // progressBar.setVisibility(View.VISIBLE);
            MFFApiWrapper.getInstance().service.setPayloadEmail(payload).enqueue(new Callback<Carrier>() {
                @Override
                public void onResponse(@NonNull Call<Carrier> call, @NonNull retrofit2.Response<Carrier> response) {
                    Utilities.ProgressBar.dismissDialog();
                    progressBar.setVisibility(View.GONE);
                    if (response.body() != null && response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        carrier = response.body();
                        optViewEmail.setVisibility(View.VISIBLE);
                        enterOtpTextViewMail.setVisibility(View.VISIBLE);
                        buttonEmailSendOtp.setVisibility(View.GONE);
                        buttonEmailVerifyOtp.setVisibility(View.VISIBLE);
                        signUpTextType.setVisibility(View.GONE);
                        isFromEmail = true;
                        linearLayoutOtpTextView.setVisibility(View.VISIBLE);
                        buttonEmailVerifyOtp.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Internal server error", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Carrier> call, @NonNull Throwable t) {
                    Utilities.ProgressBar.dismissDialog();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            emailEditText.setError("Invalid Email Address");
            progressBar.setVisibility(View.GONE);
        }
        if (!mobileToText.isEmpty() && match.find() && match.group().equals(mobileToText)) {
            countryCode = countryCodePicker.getSelectedCountryCode();
            payload = new Payload("+" + countryCode + mobileToText, "sms");
            progressBar.setVisibility(View.VISIBLE);

            MFFApiWrapper.getInstance().service.setPayloadMobile(payload).enqueue(new Callback<Carrier>() {
                @Override
                public void onResponse(@NonNull Call<Carrier> call, @NonNull retrofit2.Response<Carrier> response) {
                    Utilities.ProgressBar.dismissDialog();
                    if (response.body() != null && response.code() == 200) {
                        progressBar.setVisibility(View.GONE);
                        carrier = response.body();
                        optViewMobile.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "SMS send Successfully", Toast.LENGTH_LONG).show();
                        isFromEmail = false;
                        enterOtpTextViewMobile.setVisibility(View.VISIBLE);
                        signUpTextType.setVisibility(View.GONE);
                        buttonEmailVerifyOtp.setVisibility(View.VISIBLE);
                        buttonEmailSendOtp.setVisibility(View.GONE);
                        linearLayoutOtpTextView.setVisibility(View.VISIBLE);
                        buttonEmailVerifyOtp.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                    if (response.code() == 500) {
                        Utilities.ProgressBar.dismissDialog();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Internal Server Error", Toast.LENGTH_LONG).show();
                    }
                    Log.i("EmailResponse", String.valueOf(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<Carrier> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Utilities.ProgressBar.dismissDialog();
                }
            });
        } else {
            mobileEditText.setError("Invalid Mobile number");
            progressBar.setVisibility(View.GONE);
            Utilities.ProgressBar.dismissDialog();
        }

    }

}