package client.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mukesh.OtpView;
import com.odedtech.mff.mffapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import butterknife.BindView;
import butterknife.ButterKnife;
import dashboard.DashboardActivity;
import login.dto.LoginResponse;
import networking.WebService;
import networking.WebServiceURLs;

public class ClientOtpScrren extends AppCompatActivity implements TextWatcher, WebService.OnServiceResponseListener {


    private ProgressDialog progressDialog = null;
    private String phoneNumber;

    @BindView(R.id.text_help_phone)
    TextView phoneNumberTV;


    @BindView(R.id.otp_view)
    OtpView optView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_otp);
        ButterKnife.bind(this);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            phoneNumber = getIntent.getStringExtra(getString(R.string.number_bundle));
            String number = "to " + phoneNumber;
            phoneNumberTV.setText(number);
        }

    }


    public void nextBTNClick(View view) {
        if (!TextUtils.isEmpty(optView.getText().toString())) {
            String otpValue = optView.getText().toString();
            if (UtilityMethods.isNetworkAvailable(this)) {
                verifyOtp(otpValue);
            } else {
                Toast.makeText(this, "Please check your internet", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter valid otp", Toast.LENGTH_SHORT).show();
        }

    }

    private void verifyOtp(String otp) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        String url = WebServiceURLs.CLIENT_VERIFY_PHONE + "?phoneNumber=" + phoneNumber + "&otp=" + otp;
        WebService.getInstance().apiGetRequestCall(url, this);
    }


    public void backBTNClicked(View view) {
        onBackPressed();
    }

    public void sendOTP(View view) {
        sendOtpApiCall();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        if (otpFirstET.isFocused()) {
//            otpSecondET.requestFocus();
//        } else if (otpSecondET.isFocused()) {
//            otpThirdET.requestFocus();
//        } else if (otpThirdET.isFocused()) {
//            otpFourthET.requestFocus();
//        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onApiCallResponseSuccess(String url, String object) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (url.equalsIgnoreCase(WebServiceURLs.CLIENT_LOGIN_URL)) {
            try {
                JSONObject jsonObject = new JSONObject(object);
                if (jsonObject != null && jsonObject.getBoolean("success")) {
                    Toast.makeText(this, "OTP sent to your mobile", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Could't start session.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show();
            }
        } else if (url.contains(WebServiceURLs.CLIENT_VERIFY_PHONE)) {
            Gson gson = new GsonBuilder().create();
            LoginResponse loginResponse = gson.fromJson(object, LoginResponse.class);
            if (loginResponse != null && loginResponse.login != null && !TextUtils.isEmpty(loginResponse.login.token.accessToken)) {
                PreferenceConnector.writeString(getApplicationContext(), getString(R.string.accessToken), loginResponse.login.token.accessToken);
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                String portUrl = WebServiceURLs.PORT_URL + loginResponse.login.token.accessToken;
                WebService.getInstance().apiGetRequestCall(portUrl, this);
            } else {
                Toast.makeText(this, "Could't start session.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!TextUtils.isEmpty(object)) {
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    if (jsonObject.has("url") && jsonObject.has("portNumber")) {
                        PreferenceConnector.writeBoolean(getApplicationContext(), getString(R.string.loginStatus), true);
                        String baseUrl = "https://" + jsonObject.getString("url") + ":" + jsonObject.getString("portNumber");
                        PreferenceConnector.writeString(getApplicationContext(), "BASE_URL", baseUrl);
                        Intent intent = new Intent(this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Something went wrong. Please try after some time.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onApiCallResponseFailure(String errorMessage) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(this, TextUtils.isEmpty(errorMessage) ? "Something went wrong. Please try after some time." : errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void sendOtpApiCall() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileNumber", phoneNumber);
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.CLIENT_LOGIN_URL, params, this);
    }
}