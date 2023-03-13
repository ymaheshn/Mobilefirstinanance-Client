package shufpti;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class Helper {

    public static void displayAlertMessage(Context context, String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
    }
}
