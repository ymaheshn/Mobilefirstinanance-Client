package login;

import static Utilities.Constants.CONTACT_URL;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;

import com.odedtech.mff.mffapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import Utilities.PreferenceConnector;
import base.MFFResponse;
import base.MFFResponseNew;
import butterknife.BindView;
import butterknife.ButterKnife;
import dashboard.DashboardActivity;
import login.model.EntityResponse;
import login.model.LoginRequest;
import login.model.LoginResponse;
import network.MFFApiWrapper;
import networking.WebService;
import networking.WebServiceURLs;
import otp.OTPActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements WebService.OnServiceResponseListener {


    @BindView(R.id.numberET)
    EditText numberET;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etUserName)
    EditText etUserName;

    @BindView(R.id.view_switcher)
    ViewSwitcher viewSwitcher;

    @BindView(R.id.loginWithMobileNumber)
    TextView loginWithMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setting theme of the application
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_with_username);
        ButterKnife.bind(LoginActivity.this);
        // Example of a call to a native method
        String phoneNumber = PreferenceConnector.readString(this, getString(R.string.phoneNumber), "");
        if (!TextUtils.isEmpty(phoneNumber)) {
            numberET.setText(phoneNumber);
            numberET.setSelection(phoneNumber.length());
        }

        loginWithMobileNumber.setOnClickListener(v -> {
            if (loginWithMobileNumber.getText().toString().equals("Login with Mobile Number")) {
                loginWithMobileNumber.setText("Login with User name and Password");
            } else if (loginWithMobileNumber.getText().toString().equals("Login with User name and Password")) {
                loginWithMobileNumber.setText("Login with Mobile Number");
            }
            viewSwitcher.showNext();
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public void nextBtnClicked(View view) {
        if (viewSwitcher.getCurrentView().getId() == R.id.containerUsername) {
            loginApiCall(numberET.getText().toString());
        } else {
            loginApiCallWithUsername(etUserName.getText().toString(), etPassword.getText().toString());
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
            public void onResponse(Call<MFFResponse<LoginResponse>> call, Response<MFFResponse<LoginResponse>> response) {
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
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MFFResponse<LoginResponse>> call, Throwable t) {
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
            public void onResponse(Call<MFFResponseNew<EntityResponse>> call, Response<MFFResponseNew<EntityResponse>> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                MFFResponseNew<EntityResponse> body = response.body();
                if (body.status == HttpURLConnection.HTTP_OK) {
                    PreferenceConnector.writeString(getApplicationContext(), getString(R.string.entityname),
                            body.data.users.get(0).profileDetails.entityName);
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
            public void onFailure(Call<MFFResponseNew<EntityResponse>> call, Throwable t) {
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileNumber", phoneNumber);
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.LOGIN_URL, params, this);
    }

    @Override
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

    public void supportClick(View view) {
        Uri webpage = Uri.parse(CONTACT_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
