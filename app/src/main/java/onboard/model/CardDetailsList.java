package onboard.model;

import com.google.gson.annotations.SerializedName;

public class CardDetailsList {
    @SerializedName("linkCardId")
    int linkCardId;

    @SerializedName("profileUUID")
    String profileUUID;

    @SerializedName("contractUUID")
    String contractUUID;

    @SerializedName("userID")
    String userID;

    @SerializedName("cardDetails")
    CardDetailsItems cardDetails;

    @SerializedName("status")
    String status;


    public void setLinkCardId(int linkCardId) {
        this.linkCardId = linkCardId;
    }
    public int getLinkCardId() {
        return linkCardId;
    }

    public void setProfileUUID(String profileUUID) {
        this.profileUUID = profileUUID;
    }
    public String getProfileUUID() {
        return profileUUID;
    }

    public void setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
    }
    public String getContractUUID() {
        return contractUUID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserID() {
        return userID;
    }

    public void setCardDetails(CardDetailsItems cardDetails) {
        this.cardDetails = cardDetails;
    }
    public CardDetailsItems getCardDetails() {
        return cardDetails;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

}
