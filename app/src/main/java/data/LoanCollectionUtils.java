package data;

import loans.model.LoanCollection;

public class LoanCollectionUtils {
    public static LoanCollection clone(LoanCollection from, boolean syncPending) {
        return new LoanCollection(from.getId(), from.getContractCode(), from.getLoanAmount(),
                from.getTimestamp(), syncPending);
    }

    public static LoanCollection clone(LoanCollection from, long id) {
        return new LoanCollection(id, from.getContractCode(), from.getLoanAmount(),
                from.getTimestamp(), from.isSyncPending());
    }
}
