package credit_score;

import android.view.View;

/**
 * Created by gufran khan on 20-10-2018.
 */

public interface ICreditScoreCallback {
    void showProgressBar();

    void hideProgressBar();

    void addView(View view);

    void showMessage(String message);

    void showAlert();

    void getCreditScoreData(boolean status, String string, String templateDetailsId);
}
