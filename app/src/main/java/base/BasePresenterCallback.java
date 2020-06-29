package base;

import java.util.ArrayList;

import onboard.ClientDataDTO;
import onboard.WorkFlowTemplateDto;

/**
 * Created by gufran khan on 09-09-2018.
 */

public interface BasePresenterCallback {

    void showProgressBar();

    void hideProgressBar();

    void showMessage(String message);

    void showLogoutAlert();
}
