package onboard.model;

import com.google.gson.annotations.SerializedName;

public class ProfileDetailsLink {

    @SerializedName("DateofBirth")
    String DateOfBirth;

    @SerializedName("Hierarchy")
    int Hierarchy;

    @SerializedName("branch")
    String branch;

    @SerializedName("Gender")
    String Gender;

    @SerializedName("FormID")
    String FormID;

    @SerializedName("Name")
    String Name;

    @SerializedName("National ID")
    String NationalID;

    @SerializedName("Longitude")
    String Longitude;

    @SerializedName("Latitude")
    String Latitude;

    @SerializedName("Identifier")
    String Identifier;

    @SerializedName("Branch")
    String Branch;

    @SerializedName("branchId")
    int branchId;

    @SerializedName("Group")
    String Group;

    @SerializedName("formLabel")
    String formLabel;


    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setHierarchy(int Hierarchy) {
        this.Hierarchy = Hierarchy;
    }

    public int getHierarchy() {
        return Hierarchy;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getGender() {
        return Gender;
    }

    public void setFormID(String FormID) {
        this.FormID = FormID;
    }

    public String getFormID() {
        return FormID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setNationalID(String NationalID) {
        this.NationalID = NationalID;
    }

    public String getNationalID() {
        return NationalID;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setIdentifier(String Identifier) {
        this.Identifier = Identifier;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public void setBranch(String Branch) {
        this.Branch = Branch;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setGroup(String Group) {
        this.Group = Group;
    }

    public String getGroup() {
        return Group;
    }

    public void setFormLabel(String formLabel) {
        this.formLabel = formLabel;
    }

    public String getFormLabel() {
        return formLabel;
    }
}
