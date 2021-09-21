package com.odedtech.mff.mff_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.odedtech.mff.mffapp.BuildConfig;
import com.odedtech.mff.mffapp.R;

import Utilities.Constants;
import Utilities.PreferenceConnector;
import client.login.ClientLoginActivity;
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
                    startActivity(new Intent(SplashActivity.this, ClientLoginActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }, 3000);
        }
    }
}
