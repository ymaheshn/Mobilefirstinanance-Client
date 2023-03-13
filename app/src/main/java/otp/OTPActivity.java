package otp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.odedtech.mff.client.R;

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

public class OTPActivity extends AppCompatActivity implements TextWatcher, WebService.OnServiceResponseListener {

    @BindView(R.id.otpFirstET)
    EditText otpFirstET;
    @BindView(R.id.otpSecondET)
    EditText otpSecondET;
    @BindView(R.id.otpThirdET)
    EditText otpThirdET;
    @BindView(R.id.otpFourthET)
    EditText otpFourthET;
    @BindView(R.id.phoneNumberTV)
    TextView phoneNumberTV;

    private ProgressDialog progressDialog = null;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        Intent getIntent = getIntent();
        if (getIntent != null) {
            phoneNumber = getIntent.getStringExtra(getString(R.string.number_bundle));
            String number = "to " + phoneNumber;
            phoneNumberTV.setText(number);
        }

        otpFirstET.addTextChangedListener(new GenericTextWatcher(otpFirstET));
        otpSecondET.addTextChangedListener(new GenericTextWatcher(otpSecondET));
        otpThirdET.addTextChangedListener(new GenericTextWatcher(otpThirdET));
        otpFourthET.addTextChangedListener(new GenericTextWatcher(otpFourthET));
    }


    public void nextBTNClick(View view) {
        if (!TextUtils.isEmpty(otpFirstET.getText()) && !TextUtils.isEmpty(otpSecondET.getText()) && !TextUtils.isEmpty(otpThirdET.getText()) && !TextUtils.isEmpty(otpFourthET.getText())) {
            String otpValue = otpFirstET.getText() + "" + otpSecondET.getText() + "" + otpThirdET.getText() + "" + otpFourthET.getText();
            if (otpValue.equals("")) {

            }
            if (UtilityMethods.isNetworkAvailable(this)) {
//                loginApiCall();
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
        //post parameters
//        params.put("mobileNumber", phoneNumber);
        String url = WebServiceURLs.PHONENUMBER_OTP + "?phoneNumber=" + phoneNumber + "&otp=" + otp;
        WebService.getInstance().apiGetRequestCall(url, this);
    }

    private void loginApiCall() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        //post parameters
        Map<String, String> params = new HashMap<String, String>();
        params.put("rootUser", "mff");
        params.put("userName", "mff");
        params.put("password", "mff");
//        params.put("mobileNumber", phoneNumber);

        WebService.getInstance().apiPostRequestCall(WebServiceURLs.LOGIN_URL, params, this);
    }

    public void backBTNClicked(View view) {
        onBackPressed();
    }

    public void sendOTP(View view) {
        otpFirstET.setText("");
        otpSecondET.setText("");
        otpThirdET.setText("");
        otpFourthET.setText("");
        otpFirstET.requestFocus();
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
        if (url.equalsIgnoreCase(WebServiceURLs.LOGIN_URL)) {
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
        } else if (url.contains(WebServiceURLs.PHONENUMBER_OTP)) {
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

                    if (jsonObject.has("url")) {

                    }

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
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.LOGIN_URL, params, this);
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otpFirstET:
                    if (text.length() == 1)
                        otpSecondET.requestFocus();
                    break;
                case R.id.otpSecondET:
                    if (text.length() == 1)
                        otpThirdET.requestFocus();
                    else if (text.length() == 0)
                        otpFirstET.requestFocus();
                    break;
                case R.id.otpThirdET:
                    if (text.length() == 1)
                        otpFourthET.requestFocus();
                    else if (text.length() == 0)
                        otpSecondET.requestFocus();
                    break;
                case R.id.otpFourthET:
                    if (text.length() == 0)
                        otpThirdET.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }
}