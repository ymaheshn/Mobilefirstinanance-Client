package onboard.model;

import com.google.gson.annotations.SerializedName;

public class ProfileDetails {

    @SerializedName("branchid")
    String branchid;

    @SerializedName("name")
    String name;

    @SerializedName("identifier")
    String identifier;

    @SerializedName("hierarchy")
    String hierarchy;

    @SerializedName("nationalid")
    String nationalid;


    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }
    public String getBranchid() {
        return branchid;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }
    public String getHierarchy() {
        return hierarchy;
    }

    public void setNationalid(String nationalid) {
        this.nationalid = nationalid;
    }
    public String getNationalid() {
        return nationalid;
    }
}
