package Utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.appcompat.widget.AppCompatTextView;

import com.odedtech.mff.client.R;

public class LoadingDialogBar {

    Context context;
    Dialog dialog;

    public LoadingDialogBar(Context context) {
        this.context = context;
    }

    public void ShowDialog(String title) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        AppCompatTextView textView = dialog.findViewById(R.id.loading_text);
        textView.setText(title);
        dialog.create();
        dialog.show();
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
