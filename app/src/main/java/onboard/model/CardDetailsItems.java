package onboard.model;

import com.google.gson.annotations.SerializedName;

public class CardDetailsItems {

    @SerializedName("contractUUID")
    String contractUUID;

    @SerializedName("linkedProfileId")
    String linkedProfileId;

    @SerializedName("ContractID")
    String ContractID;

    @SerializedName("productUUID")
    String productUUID;

    @SerializedName("name")
    String name;

    @SerializedName("cardNumber")
    String cardNumber;

    @SerializedName("cardType")
    String cardType;

    @SerializedName("cardNetwork")
    String cardNetwork;

    @SerializedName("cardIssuer")
    String cardIssuer;

    @SerializedName("expiryDate")
    String expiryDate;

    @SerializedName("cvv")
    int cvv;

    @SerializedName("status")
    String status;

    @SerializedName("profileDetails")
    ProfileDetails profileDetails;


    public void setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
    }

    public String getContractUUID() {
        return contractUUID;
    }

    public void setContractID(String ContractID) {
        this.ContractID = ContractID;
    }

    public String getContractID() {
        return ContractID;
    }

    public void setProductUUID(String productUUID) {
        this.productUUID = productUUID;
    }

    public String getProductUUID() {
        return productUUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardNetwork(String cardNetwork) {
        this.cardNetwork = cardNetwork;
    }

    public String getCardNetwork() {
        return cardNetwork;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public int getCvv() {
        return cvv;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public ProfileDetails getProfileDetails() {
        return profileDetails;
    }

    public String getLinkedProfileId() {
        return linkedProfileId;
    }

    public void setLinkedProfileId(String linkedProfileId) {
        this.linkedProfileId = linkedProfileId;
    }
}
