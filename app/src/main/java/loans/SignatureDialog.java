package loans;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;

import com.odedtech.mff.client.R;

import Utilities.PreferenceConnector;

public class SignatureDialog extends Dialog {

    int colorCode;

    public SignatureDialog(Context context) {
        super(context);
        setContentView(R.layout.alert_signature);

        String colorTheme = PreferenceConnector.getThemeColor(getContext());
        colorCode = Color.parseColor(colorTheme);
        findViewById(R.id.btn_cancel).setBackgroundColor(colorCode);
        findViewById(R.id.btn_submit).setBackgroundColor(colorCode);
        findViewById(R.id.btn_submit).setOnClickListener(view -> dismiss());
        findViewById(R.id.btn_cancel).setOnClickListener(view -> dismiss());


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.9), (int) (size.y * 0.60));
        window.setGravity(Gravity.CENTER);
    }
}