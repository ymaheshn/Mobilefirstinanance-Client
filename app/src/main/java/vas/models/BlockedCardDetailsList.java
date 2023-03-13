package vas.models;

import com.google.gson.annotations.SerializedName;

public class BlockedCardDetailsList {

    @SerializedName("contractUUID")
    private String contractUUID;

    @SerializedName("status")
    private String status;

    public String getContractUUID() {
        return contractUUID;
    }

    public String setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
        return contractUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
