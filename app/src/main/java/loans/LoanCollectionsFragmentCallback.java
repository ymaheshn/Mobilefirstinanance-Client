package loans;

import java.util.List;

import base.BasePresenterCallback;
import loans.model.Datum;
import loans.model.ProfileCollection;


public interface LoanCollectionsFragmentCallback extends BasePresenterCallback {

    void onSaveContractData(String message, int recieptId, int i);

    void onGetLinkedProfile(Datum datum, List<ProfileCollection> profileCollections);
}
