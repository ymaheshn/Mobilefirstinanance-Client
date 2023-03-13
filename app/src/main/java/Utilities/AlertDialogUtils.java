package Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import com.odedtech.mff.client.R;

import appclient.odedtech.mff.client.SplashActivity;

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
        builder.setPositiveButton("OK", (dialog, id) -> {
            PreferenceConnector.writeBoolean(activityContext, activityContext.getString(R.string.loginStatus), false);
            PreferenceConnector.writeString(activityContext, activityContext.getString(R.string.accessToken), "");
            activityContext.startActivity(new Intent(activityContext, SplashActivity.class));
            activityContext.finish();
            dialog.dismiss();

        });
        AlertDialog alert11 = builder.create();
        alert11.setCanceledOnTouchOutside(false);
        alert11.setCancelable(false);
        alert11.show();
    }
    public void showSuccessAlert(Activity activityContext, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Success");
        builder.setMessage(title).setIcon(R.drawable.success_icon);
        builder.setPositiveButton("OK", (dialog, id) -> {

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showAlreadyAppliedAlert(Activity activityContext, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Status");
        builder.setMessage(title).setIcon(R.drawable.failure_icon);
        builder.setPositiveButton("OK", (dialog, id) -> {

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showSuccessBlockCardAlert(Activity activityContext, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Success");
        builder.setMessage(title).setIcon(R.drawable.success_icon);
        builder.setPositiveButton("OK", (dialog, id) -> {
            activityContext.finish();
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showAlreadyLinkedAlert(Activity activityContext, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Status");
        builder.setMessage(title).setIcon(R.drawable.face_icon);
        builder.setPositiveButton("OK", (dialog, id) -> {

        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void showOkAlert(Activity activityContext, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle("Warning!!");
        builder.setMessage(title);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public  void displayAlertMessage(Activity activity, String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setTitle("Alert");
        builder1.setMessage(message);
        builder1.setCancelable(false);
        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }

}
