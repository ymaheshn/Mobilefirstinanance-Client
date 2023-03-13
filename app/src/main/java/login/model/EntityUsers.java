package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dashboard.models.ThemeColorModel;

public class EntityUsers {

    public String rootUserID;

    @SerializedName("profileID")
    @Expose
    public String profileID;

    @SerializedName("profileDetails")
    @Expose
    public EntityProfileDetails profileDetails;

    public ThemeColorModel themeColorModel;


    public String getRootUserID() {
        return rootUserID;
    }

    public void setRootUserID(String rootUserID) {
        this.rootUserID = rootUserID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public EntityProfileDetails getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(EntityProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public ThemeColorModel getThemeColorModel() {
        return themeColorModel;
    }

    public void setThemeColorModel(ThemeColorModel themeColorModel) {
        this.themeColorModel = themeColorModel;
    }
}
