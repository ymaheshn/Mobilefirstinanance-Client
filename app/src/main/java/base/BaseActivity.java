package base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.odedtech.mff.mffapp.R;

import Utilities.PreferenceConnector;
import login.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    protected void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    protected void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void navigateToLogin() {
        PreferenceConnector.writeString(getApplicationContext(), getString(R.string.accessToken), "");
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
