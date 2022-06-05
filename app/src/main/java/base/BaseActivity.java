package base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.odedtech.mff.mffapp.R;

import java.io.IOException;

import Utilities.PreferenceConnector;
import login.LoginActivity;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    public void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        if (progressDialog == null) {
            assert false;
            progressDialog.show();
        }
    }

    public void dismissLoading() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    protected void navigateToLogin() {
        PreferenceConnector.writeString(getApplicationContext(), getString(R.string.accessToken), "");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void checkResponseError(Response response) {
        if (response.code() == 401 && response.body() != null) {
            try {
                assert response.errorBody() != null;
                MFFErrorResponse errorResponse = new Gson()
                        .fromJson(response.errorBody().string(), MFFErrorResponse.class);
                if (errorResponse.error != null && errorResponse.error.equals("invalid_token")) {
                    navigateToLogin();
                }
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this,
                getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
    }
}
