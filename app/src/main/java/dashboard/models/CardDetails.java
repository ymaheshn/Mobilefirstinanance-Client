package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class CardDetails {

    @SerializedName("contractUUID")
    private String contractUUID;

    @SerializedName("status")
    private String status;

    @SerializedName("linkedProfileId")
    private String linkedProfileId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("contractID")
    private String contractID;


    public String getContractUUID() {
        return contractUUID;
    }
    public void setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLinkedProfileId() {
        return linkedProfileId;
    }
    public void setLinkedProfileId(String linkedProfileId) {
        this.linkedProfileId = linkedProfileId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getContractID() {
        return contractID;
    }
    public void setContractID(String contractID) {
        this.contractID = contractID;
    }
}
