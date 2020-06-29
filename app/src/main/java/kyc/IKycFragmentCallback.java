package kyc;

import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import kyc.dto.KycDynamicForm;

/**
 * Created by gufran khan on 28-07-2018.
 */

public interface IKycFragmentCallback {
    void showProgressBar();

    void hideProgressBar();

    void showMessage(String message);

    void navigateToOnBoard();

    void showAlert();

    void loadDynamicFormToView(ArrayList<KycDynamicForm> allDynamicFormList, HashMap<String, KycDynamicForm> allDymamicFormHashmap);
}
