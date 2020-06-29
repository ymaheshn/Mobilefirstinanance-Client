package loans;

import base.BasePresenterCallback;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import loans.model.ProfileCollectionResponse;


public interface LoanCollectionsFragmentCallback extends BasePresenterCallback {
    void onSaveContractData(String message, int i);

    void onGetLinkedProfile(Datum datum, ProfileCollectionResponse collectionResponse);
}
