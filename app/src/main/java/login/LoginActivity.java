package login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.odedtech.mff.mffapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Utilities.PreferenceConnector;
import butterknife.BindView;
import butterknife.ButterKnife;
import networking.WebService;
import networking.WebServiceURLs;
import otp.OTPActivity;

import static Utilities.Constants.CONTACT_URL;

public class LoginActivity extends AppCompatActivity implements WebService.OnServiceResponseListener {


    @BindView(R.id.numberET)
    EditText numberET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setting theme of the application
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        // Example of a call to a native method
        String phoneNumber = PreferenceConnector.readString(this, getString(R.string.phoneNumber), "");
        if (!TextUtils.isEmpty(phoneNumber)) {
            numberET.setText(phoneNumber);
            numberET.setSelection(phoneNumber.length());
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    public void nextBtnClicked(View view) {
        loginApiCall(numberET.getText().toString());
    }

    private ProgressDialog progressDialog = null;

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
