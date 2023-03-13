package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class EventJSON {

    @SerializedName("Transaction")
    Transaction Transaction;


    public void setTransaction(Transaction Transaction) {
        this.Transaction = Transaction;
    }

    public Transaction getTransaction() {
        return Transaction;
    }
}
