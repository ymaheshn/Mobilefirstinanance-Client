package Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.odedtech.mff.mff_android.SplashActivity;
import com.odedtech.mff.mffapp.R;

public class AlertDialogUtils {

    private static AlertDialogUtils alertDialogUtils;

    private AlertDialogUtils() {


    }

    public static AlertDialogUtils getAlertDialogUtils() {
        if (alertDialogUtils == null) {
            alertDialogUtils = new AlertDialogUtils();
        }
        return alertDialogUtils;
    }


    public void showAlert(Activity activityContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setMessage("Your Session has expired, Please login again.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PreferenceConnector.writeBoolean(activityContext, activityContext.getString(R.string.loginStatus), false);
                PreferenceConnector.writeString(activityContext, activityContext.getString(R.string.accessToken), "");
                activityContext.startActivity(new Intent(activityContext, SplashActivity.class));
                activityContext.finish();
                dialog.dismiss();

            }
        });
        AlertDialog alert11 = builder.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.setCancelable(false);
        alert11.show();
    }

    public void showOkAlert(Activity activityContext, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setMessage(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert11 = builder.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.setCancelable(false);
        alert11.show();
    }
}
