package views;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import Utilities.ViewType;

public class ViewsFactory {

    private static ViewsFactory viewsFactory = null;
    private EditTextView editTextView;
    private TextView textView;

    //singleton object
    private ViewsFactory() {

    }

    public static ViewsFactory getViewFactoryObject() {

        if (viewsFactory == null) {
            viewsFactory = new ViewsFactory();
        }
        return viewsFactory;
    }

    public View getViewTypeObject(Context context, ViewType viewType, String placeHolder, String textToDisplay, int moveToNext) {
        switch (viewType) {
            case EDITTEXT:
                editTextView = new EditTextView(context);
                //Hide Keyboard if clicked back
                editTextView.setListener(((keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.hideSoftInputFromWindow(editTextView.getWindowToken(), 0);
                    }
                }));

                if (!TextUtils.isEmpty(placeHolder))
                    editTextView.setHint(placeHolder);
                if (!TextUtils.isEmpty(textToDisplay))
                    editTextView.setText(textToDisplay);
                return editTextView;
            default:
                textView = new TextView(context);
                if (!TextUtils.isEmpty(placeHolder))
                    textView.setHint(placeHolder);
                if (!TextUtils.isEmpty(textToDisplay))
                    textView.setText(textToDisplay);
                return textView;
        }
    }
}
