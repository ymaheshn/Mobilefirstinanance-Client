package cashflow;

import android.view.View;

public interface ICashFlowCallBacks {

    void showProgressBar();

    void hideProgressBar();

    void addView(View view);

    void showMessage(String message);

    void showAlert();

    void getCashFlowData(boolean status,String string,String templateDetailsId);

}
