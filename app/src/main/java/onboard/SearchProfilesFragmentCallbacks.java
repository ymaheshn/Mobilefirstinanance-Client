package onboard;

import base.BasePresenterCallback;
import loans.model.SearchData;

import java.util.ArrayList;
import java.util.List;

public interface SearchProfilesFragmentCallbacks extends BasePresenterCallback {

    void loadRecyclerView(List<SearchData> clients);
    void loadProfilesRecyclerView(ArrayList<ClientDataDTO> clients);
}
