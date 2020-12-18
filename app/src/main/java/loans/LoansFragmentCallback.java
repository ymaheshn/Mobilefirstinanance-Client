package loans;

import java.util.List;

import base.BasePresenterCallback;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import loans.model.ProfileCollection;


public interface LoansFragmentCallback extends BasePresenterCallback {

    void loadRecyclerView(LinkedProfilesResponse profilesResponse);

    void linkedProfileCollection(Datum datum, List<ProfileCollection> profileCollections);
}
