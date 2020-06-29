package credit_bureau;

/**
 * Created by gufran khan on 20-10-2018.
 */

public interface ICreditBureauCallback {
    void showProgressBar();

    void hideProgressBar();

    void showMessage(String message);

    void showLogoutAlert();

    void showAlert();

    void getCreditBureauData(boolean status, String string, String templateDetailsId);
}
