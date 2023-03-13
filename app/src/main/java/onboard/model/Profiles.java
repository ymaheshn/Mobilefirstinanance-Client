package onboard.model;

import com.google.gson.annotations.SerializedName;

public class Profiles {

    @SerializedName("status")
    String status;

    @SerializedName("rootUserID")
    String rootUserID;

    @SerializedName("recordCreatorID")
    String recordCreatorID;

    @SerializedName("profileFormID")
    int profileFormID;

    @SerializedName("profileDetails")
    ProfileDetails profileDetails;

    @SerializedName("profileCreationTime")
    String profileCreationTime;

    @SerializedName("profileID")
    String profileID;

    @SerializedName("linkedRootUserProfileID")
    String linkedRootUserProfileID;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("linkedProfileID")
    String linkedProfileID;


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setRootUserID(String rootUserID) {
        this.rootUserID = rootUserID;
    }

    public String getRootUserID() {
        return rootUserID;
    }

    public void setRecordCreatorID(String recordCreatorID) {
        this.recordCreatorID = recordCreatorID;
    }

    public String getRecordCreatorID() {
        return recordCreatorID;
    }

    public void setProfileFormID(int profileFormID) {
        this.profileFormID = profileFormID;
    }

    public int getProfileFormID() {
        return profileFormID;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public ProfileDetails getProfileDetails() {
        return profileDetails;
    }

    public void setProfileCreationTime(String profileCreationTime) {
        this.profileCreationTime = profileCreationTime;
    }

    public String getProfileCreationTime() {
        return profileCreationTime;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setLinkedRootUserProfileID(String linkedRootUserProfileID) {
        this.linkedRootUserProfileID = linkedRootUserProfileID;
    }

    public String getLinkedRootUserProfileID() {
        return linkedRootUserProfileID;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setLinkedProfileID(String linkedProfileID) {
        this.linkedProfileID = linkedProfileID;
    }

    public String getLinkedProfileID() {
        return linkedProfileID;
    }
}
