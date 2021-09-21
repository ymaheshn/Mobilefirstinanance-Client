package loans.model;


import java.io.Serializable;

public class LoanCollection implements Serializable {


    private String contractCode;

    private String loanAmount;

    private long timestamp;

    private boolean syncPending;


    public String getContractCode() {
        return contractCode;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isSyncPending() {
        return syncPending;
    }

    public LoanCollection(String contractCode, String loanAmount) {
        this.contractCode = contractCode;
        this.loanAmount = loanAmount;
        this.timestamp = System.currentTimeMillis();
        this.syncPending = true;
    }

    public LoanCollection(String contractCode, String loanAmount, long timestamp, boolean syncPending) {
        this.contractCode = contractCode;
        this.loanAmount = loanAmount;
        this.timestamp = timestamp;
        this.syncPending = syncPending;
    }

    @Override
    public String toString() {
        return String.format("Comment id: %s, text: %s, syncPending: %s", loanAmount, syncPending);
    }
}
