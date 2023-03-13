package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class ProfileJSON {

    @SerializedName("image")
    String image;

    @SerializedName("branchId")
    int branchId;

    @SerializedName("Group")
    String Group;

    @SerializedName("Latitude")
    String Latitude;

    @SerializedName("Gender")
    String Gender;

    @SerializedName("Longitude")
    String Longitude;

    @SerializedName("branch")
    String branch;

    @SerializedName("Name")
    String Name;

    @SerializedName("Hierarchy")
    int Hierarchy;

    @SerializedName("Identifier")
    String Identifier;

    @SerializedName("Branch")
    String Branch;

    @SerializedName("National ID")
    String NationalID;

    @SerializedName("Date of Birth")
    String dateOfBirth;

    @SerializedName("FormID")
    String FormID;

    @SerializedName("Age")
    int Age;

    @SerializedName("Pincode")
    String Pincode;


    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
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

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getGender() {
        return Gender;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setHierarchy(int Hierarchy) {
        this.Hierarchy = Hierarchy;
    }

    public int getHierarchy() {
        return Hierarchy;
    }

    public void setIdentifier(String Identifier) {
        this.Identifier = Identifier;
    }

    public String getIdentifier() {
        return Identifier;
    }


    public void setNationalID(String NationalID) {
        this.NationalID = NationalID;
    }

    public String getNationalID() {
        return NationalID;
    }

    public void setDateofBirth(String DateofBirth) {
        this.dateOfBirth = DateofBirth;
    }

    public String getDateofBirth() {
        return dateOfBirth;
    }

    public void setFormID(String FormID) {
        this.FormID = FormID;
    }

    public String getFormID() {
        return FormID;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public int getAge() {
        return Age;
    }

    public void setPincode(String Pincode) {
        this.Pincode = Pincode;
    }

    public String getPincode() {
        return Pincode;
    }
}
