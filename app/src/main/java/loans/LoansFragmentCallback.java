package loans;

import base.BasePresenterCallback;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import loans.model.ProfileCollectionResponse;


public interface LoansFragmentCallback extends BasePresenterCallback {

    void loadRecyclerView(LinkedProfilesResponse profilesResponse);

    void linkedProfileCollection(Datum datum, ProfileCollectionResponse collectionResponse);
}
