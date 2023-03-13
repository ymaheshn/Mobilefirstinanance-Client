package appclient.odedtech.mff.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.odedtech.mff.client.BuildConfig;
import com.odedtech.mff.client.R;

import Utilities.Constants;
import Utilities.PreferenceConnector;
import base.OnBoardingLauncherActivity;
import dashboard.DashboardActivity;
import login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String accessToken = PreferenceConnector.readString(this, getString(R.string.accessToken), "");
        if (!TextUtils.isEmpty(accessToken)) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                finish();
            }, 3000);
        } else {
            new Handler().postDelayed(() -> {
                if (Constants.FLAVOR_CLIENT.equalsIgnoreCase(BuildConfig.FLAVOR)) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, OnBoardingLauncherActivity.class));
                }
                finish();
            }, 3000);
        }
    }
}
