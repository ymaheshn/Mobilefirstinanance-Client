package loans.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class LoanCollection implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "contract_code")
    private String contractCode;

    @ColumnInfo(name = "loan_amount")
    private String loanAmount;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "sync_pending")
    private boolean syncPending;

    public long getId() {
        return id;
    }

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

    @Ignore
    public LoanCollection(String contractCode, String loanAmount) {
        this.contractCode = contractCode;
        this.loanAmount = loanAmount;
        this.timestamp = System.currentTimeMillis();
        this.syncPending = true;
    }

    public LoanCollection(long id, String contractCode, String loanAmount, long timestamp, boolean syncPending) {
        this.id = id;
        this.contractCode = contractCode;
        this.loanAmount = loanAmount;
        this.timestamp = timestamp;
        this.syncPending = syncPending;
    }

    @Override
    public String toString() {
        return String.format("Comment id: %s, text: %s, syncPending: %s", id, loanAmount, syncPending);
    }
}
