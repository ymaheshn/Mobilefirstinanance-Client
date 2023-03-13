package login;

import static Utilities.Constants.CONTACT_URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.hbb20.CountryCodePicker;
import com.mukesh.OtpView;
import com.odedtech.mff.client.R;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Objects;

import Utilities.PreferenceConnector;
import base.MFFResponse;
import base.MFFResponseNew;
import butterknife.BindView;
import butterknife.ButterKnife;
import client.login.ClientOtpScrren;
import dashboard.DashboardActivity;
import login.model.EntityResponse;
import login.model.LoginRequest;
import login.model.LoginResponse;
import login.model.MobileOTPResponse;
import login.model.PayloadOTPResponse;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import signup.SignUpEmailAndMobileActivity;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.etPassword)
    OtpView etPassword;

    @BindView(R.id.etUserName_login)
    AppCompatEditText etUserName;

    @BindView(R.id.numberETLogin)
    AppCompatEditText numberETLogin;

    @BindView(R.id.country_code_picker_login)
    CountryCodePicker countryCodePicker;

    @BindView(R.id.signUpText)
    TextView signUpText;

    @BindView(R.id.view_switcher)
    ViewSwitcher viewSwitcher;

    @BindView(R.id.loginWithMobileNumber)
    TextView loginWithMobileNumber;

    private ArrayList<String> lableList;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String linkBranches;
    private String linkedBranchesName;
    private String email;
    private PayloadOTPResponse payloadOTPResponse;
    private String countryCode;
    public MobileOTPResponse mobileOTPResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setting theme of the application
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_username);
        ButterKnife.bind(LoginActivity.this);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("lableList", lableList);
        bundle.putString("lastName", lastName);
        bundle.putString("firstName", firstName);
        bundle.putString("mobileNumber", mobileNumber);
        bundle.putString("linkedBranches", linkedBranchesName);
        bundle.putString("email", email);

        // Example of a call to a native method
        /*String phoneNumber = PreferenceConnector.readString(this, getString(R.string.phoneNumber), "");
        if (!TextUtils.isEmpty(phoneNumber)) {
            numberET.setText(phoneNumber);
            numberET.setSelection(phoneNumber.length());
        }*/

        loginWithMobileNumber.setOnClickListener(v -> {
            if (loginWithMobileNumber.getText().toString().equals("Login with Mobile Number")) {
                loginWithMobileNumber.setText("Login with User name and Password");
            } else if (loginWithMobileNumber.getText().toString().equals("Login with User name and Password")) {
                loginWithMobileNumber.setText("Login with Mobile Number");
            }
            viewSwitcher.showNext();
        });
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpEmailAndMobileActivity.class);
            startActivity(intent);
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public void nextBtnClicked(View view) {
        if (viewSwitcher.getCurrentView().getId() == R.id.containerUsername) {
            loginApiCall(Objects.requireNonNull(numberETLogin.getText()).toString());
        } else {
            loginApiCallWithUsername(String.valueOf(etUserName.getText()), String.valueOf(etPassword.getText()));
        }
    }

    private ProgressDialog progressDialog = null;

    private void loginApiCallWithUsername(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this,
                    "Details can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.password = password;
        loginRequest.userName = username;
        loginRequest.rootUser = "DEV";

        MFFApiWrapper.getInstance().service.login(loginRequest).enqueue(new Callback<MFFResponse<LoginResponse>>() {
            @Override
            public void onResponse(@NonNull Call<MFFResponse<LoginResponse>> call, @NonNull Response<MFFResponse<LoginResponse>> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                MFFResponse<LoginResponse> responseBody = response.body();
                if (responseBody != null && responseBody.statusCodeValue == HttpURLConnection.HTTP_OK && !TextUtils.isEmpty(responseBody.body.accessToken)) {
                    PreferenceConnector.writeString(getApplicationContext(), getString(R.string.accessToken),
                            responseBody.body.accessToken);
                    PreferenceConnector.writeBoolean(getApplicationContext(), getString(R.string.loginStatus), true);
                    getEntityDetails();
                } else {
                    etUserName.setError("Invalid");
                    etPassword.setError("Invalid");
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.valid_credentials), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MFFResponse<LoginResponse>> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(LoginActivity.this,
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEntityDetails() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String accessToken = PreferenceConnector.readString(this, getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getEntityDetails(accessToken).enqueue(new Callback<MFFResponseNew<EntityResponse>>() {
            @Override
            public void onResponse(@NonNull Call<MFFResponseNew<EntityResponse>> call, @NonNull Response<MFFResponseNew<EntityResponse>> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                MFFResponseNew<EntityResponse> body = response.body();
                if (body.status == HttpURLConnection.HTTP_OK) {
                    PreferenceConnector.writeString(getApplicationContext(), getString(R.string.entityname),
                            body.data.users.get(0).profileDetails.entityName);

                    PreferenceConnector.themeColor(getApplicationContext(), body.data.users.get(0).profileDetails.themecolor);

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MFFResponseNew<EntityResponse>> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(LoginActivity.this,
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginApiCall(String phoneNumber) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
      /*  Map<String, String> params = new HashMap<String, String>();
        params.put("mobileNumber", phoneNumber);
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.LOGIN_URL, params, this);*/
        countryCode = countryCodePicker.getSelectedCountryCode();
        MFFApiWrapper.getInstance().service.sendOtpToUser(countryCode + phoneNumber).enqueue(new Callback<MobileOTPResponse>() {
            @Override
            public void onResponse(@NonNull Call<MobileOTPResponse> call, @NonNull Response<MobileOTPResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                    mobileOTPResponse = response.body();
                    Intent intent = new Intent(LoginActivity.this, ClientOtpScrren.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("number",mobileOTPResponse.to);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MobileOTPResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    public void onApiCallResponseSuccess(String url, String object) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(object);
            if (jsonObject != null && jsonObject.has("status") && !TextUtils.isEmpty(jsonObject.getString("status"))
                    && jsonObject.getString("status").equalsIgnoreCase("success")) {
                if (!TextUtils.isEmpty(numberET.getText().toString()) && numberET.getText().toString().length() == 10) {
                    PreferenceConnector.writeString(getApplicationContext(), getString(R.string.phoneNumber), numberET.getText().toString());
                    Intent intent = new Intent(this, OTPActivity.class);
                    intent.putExtra(getString(R.string.number_bundle), numberET.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show();
                }
            } else if (jsonObject != null && jsonObject.has("message") && !TextUtils.isEmpty(jsonObject.getString("message"))) {
                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could't start session.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onApiCallResponseFailure(String errorMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show();
    }
*/
    public void supportClick(View view) {
        Uri webpage = Uri.parse(CONTACT_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
