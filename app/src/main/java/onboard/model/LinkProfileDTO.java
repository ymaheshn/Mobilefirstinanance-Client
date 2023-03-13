package onboard.model;

import java.util.ArrayList;

public class LinkProfileDTO {
    ArrayList <ProfileDetail> profileDetails = new ArrayList<>();
    private String workFlowID;


    // Getter Methods

    public String getWorkFlowID() {
        return workFlowID;
    }

    // Setter Methods

    public void setWorkFlowID(String workFlowID) {
        this.workFlowID = workFlowID;
    }

    public ArrayList<ProfileDetail> getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ArrayList<ProfileDetail> profileDetails) {
        this.profileDetails = profileDetails;
    }

    /* // public ArrayList<ProfileDetail> profileDetails;
   @SerializedName("workFlowID")
   @Expose
    public String workFlowID;

    @SerializedName("profileID")
    @Expose
    public String profileID;
*//*
    public ArrayList<ProfileDetail> getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ArrayList<ProfileDetail> profileDetails) {
        this.profileDetails = profileDetails;
    }*//*

    public String getWorkFlowID() {
        return workFlowID;
    }

    public void setWorkFlowID(String workFlowID) {
        this.workFlowID = workFlowID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }*/
}
