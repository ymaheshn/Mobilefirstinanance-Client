package loans;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.odedtech.mff.mffapp.R;

public class SignatureDialog extends Dialog {


    public SignatureDialog(Context context) {
        super(context);
        setContentView(R.layout.alert_signature);
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