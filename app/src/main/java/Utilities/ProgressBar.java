package Utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.odedtech.mff.client.R;

public class ProgressBar {
    // public static AlertDialog progressDialog;


    static Dialog dialog;


    public static void showProgressDialog(Context title) {
        if (dialog == null) {
            dialog = new Dialog(title);
            dialog.setContentView(R.layout.dialogbox);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            /*AppCompatTextView textView = dialog.findViewById(R.id.loading_text);
            textView.setText(title);*/
            dialog.create();
            dialog.show();
        }
    }

    public static void dismissDialog() {
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            dialog = null;
        }
    }
}
