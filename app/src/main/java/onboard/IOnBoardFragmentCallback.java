package onboard;

import java.util.ArrayList;

import base.BasePresenterCallback;

/**
 * Created by gufran khan on 09-09-2018.
 */

public interface IOnBoardFragmentCallback extends BasePresenterCallback {

    void loadRecyclerView(ArrayList<ClientDataDTO> clients, boolean isFromSearch, boolean clearAll);

    void profileLinkStatusFromApi(boolean status, WorkFlowTemplateDto workFlowTemplateDto);

   // void loadSearchedRecyclerView(ArrayList<Profile> searchedClients, boolean isFromSearch);

}
