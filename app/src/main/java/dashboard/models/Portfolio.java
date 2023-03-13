package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class Portfolio {

    @SerializedName("productCategory")
    String productCategory;

    @SerializedName("profileIdentifier")
    String profileIdentifier;

    @SerializedName("eventValue")
    int eventValue;

    @SerializedName("syncDate")
    String syncDate;

    @SerializedName("nationalID")
    String nationalID;

    @SerializedName("rePayment")
    String rePayment;

    @SerializedName("processTime")
    String processTime;

    @SerializedName("productName")
    String productName;

    @SerializedName("summeryID")
    String summeryID;

    @SerializedName("units")
    String units;

    @SerializedName("eventType")
    String eventType;

    @SerializedName("contractJSON")
    ContractJSON contractJSON;

    @SerializedName("profileJSON")
    ProfileJSON profileJSON;

    @SerializedName("eventJSON")
    EventJSON eventJSON;

    @SerializedName("status")
    String status;

    @SerializedName("productUUID")
    String productUUID;

    @SerializedName("profileID")
    String profileID;

    @SerializedName("eventID")
    String eventID;

    @SerializedName("eventTime")
    String eventTime;

    @SerializedName("rootUserID")
    String rootUserID;

    @SerializedName("contractUUID")
    String contractUUID;

    @SerializedName("contractID")
    String contractID;


    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    public String getProductCategory() {
        return productCategory;
    }

    public void setProfileIdentifier(String profileIdentifier) {
        this.profileIdentifier = profileIdentifier;
    }
    public String getProfileIdentifier() {
        return profileIdentifier;
    }

    public void setEventValue(int eventValue) {
        this.eventValue = eventValue;
    }
    public int getEventValue() {
        return eventValue;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }
    public String getSyncDate() {
        return syncDate;
    }

    public void setNationalID(String nationalID) {
        this.nationalID = nationalID;
    }
    public String getNationalID() {
        return nationalID;
    }

    public void setRePayment(String rePayment) {
        this.rePayment = rePayment;
    }
    public String getRePayment() {
        return rePayment;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
    public String getProcessTime() {
        return processTime;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductName() {
        return productName;
    }

    public void setSummeryID(String summeryID) {
        this.summeryID = summeryID;
    }
    public String getSummeryID() {
        return summeryID;
    }

    public void setUnits(String units) {
        this.units = units;
    }
    public String getUnits() {
        return units;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public String getEventType() {
        return eventType;
    }

    public void setContractJSON(ContractJSON contractJSON) {
        this.contractJSON = contractJSON;
    }
    public ContractJSON getContractJSON() {
        return contractJSON;
    }

    public void setProfileJSON(ProfileJSON profileJSON) {
        this.profileJSON = profileJSON;
    }
    public ProfileJSON getProfileJSON() {
        return profileJSON;
    }

    public void setEventJSON(EventJSON eventJSON) {
        this.eventJSON = eventJSON;
    }
    public EventJSON getEventJSON() {
        return eventJSON;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setProductUUID(String productUUID) {
        this.productUUID = productUUID;
    }
    public String getProductUUID() {
        return productUUID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }
    public String getProfileID() {
        return profileID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public String getEventID() {
        return eventID;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }
    public String getEventTime() {
        return eventTime;
    }

    public void setRootUserID(String rootUserID) {
        this.rootUserID = rootUserID;
    }
    public String getRootUserID() {
        return rootUserID;
    }

    public void setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
    }
    public String getContractUUID() {
        return contractUUID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }
    public String getContractID() {
        return contractID;
    }

}
