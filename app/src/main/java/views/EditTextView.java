package views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.odedtech.mff.mffapp.R;

import interfaces.IOnKeyBoardDownListener;

public class EditTextView extends AppCompatEditText {

    IOnKeyBoardDownListener iOnKeyBoardDownListener;

    public EditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextView(Context context) {
        super(context);
    }

    private void init() {
        super.setBackgroundResource(R.drawable.login_btn_selector);
        super.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        super.setLayoutParams(getLayoutHeight());
    }

    private ViewGroup.LayoutParams getLayoutHeight() {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50); // Width , height
        lparams.setMargins(10, 10, 10, 0);
        return lparams;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        iOnKeyBoardDownListener.OnKeyEventCalled(keyCode, event);
        return super.dispatchKeyEvent(event);

    }

    public void setListener(IOnKeyBoardDownListener iOnKeyBoardDownListener) {
        this.iOnKeyBoardDownListener = iOnKeyBoardDownListener;
    }

    //Setting Font Type...........
    @Override
    public void setTypeface(Typeface tf, int style) {
        super.setTypeface(tf, style);
        if (style == Typeface.BOLD) {
            //  super.setTypeface(Typeface.createFromAsset(getContext().getAssets() , ""));
        } else {
            //super.setTypeface(Typeface.createFromAsset(getContext().getAssets() , ""));
        }

    }
}
