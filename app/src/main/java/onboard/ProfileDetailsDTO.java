package onboard;

import com.google.gson.annotations.SerializedName;

import loans.model.Data;


public class ProfileDetailsDTO {
    @SerializedName("FormID")
    public String formID;
    public String image;
    @SerializedName("National ID")
    public int nationalID;
    @SerializedName("Name")
    public String name;
    @SerializedName("Secondary ID Type")
    public String secondaryIDType;
    @SerializedName("Secondary ID")
    public String secondaryID;
    @SerializedName("Tertiary ID Type")
    public String tertiaryIDType;
    @SerializedName("Tertiary ID ")
    public String tertiaryID;
    @SerializedName("Gender")
    public String gender;
    @SerializedName("Date of Birth")
    public String dateOfBirth;
    @SerializedName("Marital Status")
    public String maritalStatus;
    @SerializedName("Type of relation")
    public String typeOfRelation;
    @SerializedName("Relation Name")
    public String relationName;
    @SerializedName("Nominee Name")
    public String nomineeName;
    @SerializedName("Nominee Age")
    public String nomineeAge;
    @SerializedName("Nominee Relationship")
    public String nomineeRelationship;
    @SerializedName("Address")
    public String address;
    @SerializedName("State")
    public String state;
    @SerializedName("District")
    public String district;
    @SerializedName("Village/Town/City")
    public String villageTownCity;
    @SerializedName("Pincode")
    public String pincode;
    @SerializedName("Email")
    public String email;
    @SerializedName("Residence Accommodation")
    public String residenceAccommodation;
    @SerializedName("Educational Qualifications")
    public String educationalQualifications;
    @SerializedName("Occupation")
    public String occupation;
    @SerializedName("Mobile Number")
    public String mobileNumber;
    @SerializedName("Latitude")
    public String latitude;
    @SerializedName("Longitude")
    public String longitude;
    @SerializedName("Hierarchy")
    public int hierarchy;
    public int branchId;
    @SerializedName("Branch")
    public String branch;
    @SerializedName("Group")
    public String group;
    @SerializedName("Identifier")
    public String identifier;
    public String profileId;
    @SerializedName("Age")
    public int age;
    public String profilePicture;

}
