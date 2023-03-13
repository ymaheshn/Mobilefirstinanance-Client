package vas.models;

import com.google.gson.annotations.SerializedName;

public class BlockedCardDetails {

    @SerializedName("contractUUID")
    String contractUUID;

    @SerializedName("message")
    String message;


    public void setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
    }
    public String getContractUUID() {
        return contractUUID;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
