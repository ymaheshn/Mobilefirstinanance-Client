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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mukesh.OtpView;
import com.odedtech.mff.client.R;

import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import base.MFFResponse;
import base.MFFResponseNew;
import butterknife.BindView;
import butterknife.ButterKnife;
import dashboard.DashboardActivity;
import login.model.EntityResponse;
import login.model.LoginResponse;
import login.model.MobileNumberLoginRequest;
import login.model.MobileOTPResponse;
import login.model.PayloadOTPResponse;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientOtpScrren extends AppCompatActivity implements TextWatcher {


    private ProgressDialog progressDialog = null;
    private String phoneNumber;

    @BindView(R.id.phoneNumberTV)
    TextView phoneNumberTV;

    @BindView(R.id.otp_mobile_view_login)
    OtpView optView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_otp);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("number");
        String number = "to " + phoneNumber;
        phoneNumberTV.setText(number);

    }


    public void nextBTNClick(View view) throws NoSuchAlgorithmException, KeyManagementException {
        if (!TextUtils.isEmpty(Objects.requireNonNull(optView.getText()).toString())) {
            String otpValue = optView.getText().toString();
            if (UtilityMethods.isNetworkAvailable(this)) {
                verifyOtp(otpValue);
            } else {
                Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.enter_valid_otp, Toast.LENGTH_SHORT).show();
        }

    }

    private void verifyOtp(String otp) throws NoSuchAlgorithmException, KeyManagementException {
        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();*/
       /* String url = WebServiceURLs.CLIENT_VERIFY_PHONE + "?phoneNumber=" + phoneNumber + "&otp=" + otp;
        WebService.getInstance().apiGetRequestCall(url, this);*/
        PayloadOTPResponse payloadOTPResponse = new PayloadOTPResponse(phoneNumber, otp);
        loginApiCallWithMobileNumber(phoneNumber, otp);
       /* MFFApiWrapper.getInstance().service.setOTPMobile(payloadOTPResponse).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();

                    finish();
                } else {
                    progressDialog.dismiss();
                    optView.setError("Invalid OTP");
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/
    }


    public void backBTNClicked(View view) {
        onBackPressed();
    }

    public void sendOTP(View view) throws NoSuchAlgorithmException, KeyManagementException {
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

    /*  @Override
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
  */
    private void loginApiCallWithMobileNumber(String mobileNUmber, String otp) throws NoSuchAlgorithmException, KeyManagementException {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        MobileNumberLoginRequest loginRequest = new MobileNumberLoginRequest();
        loginRequest.mobileNumber = mobileNUmber;
        loginRequest.otp = otp;

        MFFApiWrapper.getInstance().service.loginMobileNumber(loginRequest).enqueue(new Callback<MFFResponse<LoginResponse>>() {
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
                    try {
                        getEntityDetails();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (KeyManagementException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(ClientOtpScrren.this,
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MFFResponse<LoginResponse>> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ClientOtpScrren.this,
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEntityDetails() throws NoSuchAlgorithmException, KeyManagementException {
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
                    Intent intent = new Intent(ClientOtpScrren.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ClientOtpScrren.this,
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MFFResponseNew<EntityResponse>> call, @NonNull Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ClientOtpScrren.this,
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtpApiCall() throws KeyManagementException {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
       /* Map<String, String> params = new HashMap<String, String>();
        params.put("mobileNumber", phoneNumber);
        WebService.getInstance().apiPostRequestCall(WebServiceURLs.CLIENT_LOGIN_URL, params, this);*/
        String number = phoneNumber.substring(1, 13);
        MFFApiWrapper.getInstance().service.sendOtpToUser(number).enqueue(new Callback<MobileOTPResponse>() {
            @Override
            public void onResponse(@NonNull Call<MobileOTPResponse> call, @NonNull Response<MobileOTPResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200 && response.body() != null) {
                } else {

                    Toast.makeText(getApplicationContext(), "Please Enter Valid OTP", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MobileOTPResponse> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}