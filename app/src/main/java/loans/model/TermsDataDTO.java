package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TermsDataDTO implements Parcelable {

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public DataTerms data;

    protected TermsDataDTO(Parcel in) {
        status = in.readInt();
        message = in.readString();
    }

    public static final Creator<TermsDataDTO> CREATOR = new Creator<TermsDataDTO>() {
        @Override
        public TermsDataDTO createFromParcel(Parcel in) {
            return new TermsDataDTO(in);
        }

        @Override
        public TermsDataDTO[] newArray(int size) {
            return new TermsDataDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(message);
    }

  /*  @SerializedName("profilejson")
    @Expose
    private Profilejson profilejson;*//*


    // Getter Methods

    public float getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    // Setter Methods

    public void setStatus(float status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setdata(Data data) {
        this.data = data;
    }

    public void setData(Data data) {
        this.data = data;
    }

  *//*  public Profilejson getProfilejson() {
        return profilejson;
    }

    public void setProfilejson(Profilejson profilejson) {
        this.profilejson = profilejson;
    }*//*
}

*//*class Data {
   Portfolio PortfolioObject;


   // Getter Methods

   public Portfolio getPortfolio() {
       return PortfolioObject;
   }

   // Setter Methods

   public void setPortfolio(Portfolio portfolioObject) {
       this.PortfolioObject = portfolioObject;
   }
}*//*
*//* public class Portfolio {

     // Setter Methods


}*//*
class Profilejson {
    private String EducationalQualifications;
    private String Group;
    private float workflowprofileid;
    private String Address;
    private String RelationName;
    private String Latitude;
    private String Gender;
    private String contractflag;
    private String branch;
    private String Name;
    private float Hierarchy;
    private float MobileNumber;
    private String approved;
    private String Identifier;
    private String Branch;
    private float NationalID;
    private String workflowid;
    private float branchId;
    private String TertiaryID;
    // private String Village/Town/City;
    // private String Father Name/SpouseName;
    private String ResidenceAccommodation;
    private String SecondaryID;
    private String MaritalStatus;
    private String Longitude;
    private String Occupation;
    private String TertiaryIDType;
    private String State;
    private String profileId;
    private String SecondaryIDType;
    private String DateofBirth;
    private String FormID;
    private String District;
    private float Pincode;


    // Getter Methods

    public String getEducationalQualifications() {
        return EducationalQualifications;
    }

    public String getGroup() {
        return Group;
    }

    public float getWorkflowprofileid() {
        return workflowprofileid;
    }

    public String getAddress() {
        return Address;
    }

    public String getRelationName() {
        return RelationName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getGender() {
        return Gender;
    }

    public String getContractflag() {
        return contractflag;
    }


    public String getName() {
        return Name;
    }

    public float getHierarchy() {
        return Hierarchy;
    }

    public float getMobileNumber() {
        return MobileNumber;
    }

    public String getApproved() {
        return approved;
    }

    public String getIdentifier() {
        return Identifier;
    }

    public String getBranch() {
        return Branch;
    }

    public float getNationalID() {
        return NationalID;
    }

    public String getWorkflowid() {
        return workflowid;
    }

    public float getBranchId() {
        return branchId;
    }

    public String getTertiaryID() {
        return TertiaryID;
    }


    public String getResidenceAccommodation() {
        return ResidenceAccommodation;
    }

    public String getSecondaryID() {
        return SecondaryID;
    }

    public String getMaritalStatus() {
        return MaritalStatus;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getOccupation() {
        return Occupation;
    }

    public String getTertiaryIDType() {
        return TertiaryIDType;
    }

    public String getState() {
        return State;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getSecondaryIDType() {
        return SecondaryIDType;
    }

    public String getDateofBirth() {
        return DateofBirth;
    }

    public String getFormID() {
        return FormID;
    }

    public String getDistrict() {
        return District;
    }

    public float getPincode() {
        return Pincode;
    }

    // Setter Methods

    public void setEducationalQualifications(String EducationalQualifications) {
        this.EducationalQualifications = EducationalQualifications;
    }

    public void setGroup(String Group) {
        this.Group = Group;
    }

    public void setWorkflowprofileid(float workflowprofileid) {
        this.workflowprofileid = workflowprofileid;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setRelationName(String RelationName) {
        this.RelationName = RelationName;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setContractflag(String contractflag) {
        this.contractflag = contractflag;
    }


    public void setName(String Name) {
        this.Name = Name;
    }

    public void setHierarchy(float Hierarchy) {
        this.Hierarchy = Hierarchy;
    }

    public void setMobileNumber(float MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public void setIdentifier(String Identifier) {
        this.Identifier = Identifier;
    }

    public void setBranch(String Branch) {
        this.Branch = Branch;
    }

    public void setNationalID(float NationalID) {
        this.NationalID = NationalID;
    }

    public void setWorkflowid(String workflowid) {
        this.workflowid = workflowid;
    }

    public void setBranchId(float branchId) {
        this.branchId = branchId;
    }

    public void setTertiaryID(String TertiaryID) {
        this.TertiaryID = TertiaryID;
    }


    public void setResidenceAccommodation(String ResidenceAccommodation) {
        this.ResidenceAccommodation = ResidenceAccommodation;
    }

    public void setSecondaryID(String SecondaryID) {
        this.SecondaryID = SecondaryID;
    }

    public void setMaritalStatus(String MaritalStatus) {
        this.MaritalStatus = MaritalStatus;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public void setOccupation(String Occupation) {
        this.Occupation = Occupation;
    }

    public void setTertiaryIDType(String TertiaryIDType) {
        this.TertiaryIDType = TertiaryIDType;
    }

    public void setState(String State) {
        this.State = State;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setSecondaryIDType(String SecondaryIDType) {
        this.SecondaryIDType = SecondaryIDType;
    }

    public void setDateofBirth(String DateofBirth) {
        this.DateofBirth = DateofBirth;
    }

    public void setFormID(String FormID) {
        this.FormID = FormID;
    }

    public void setDistrict(String District) {
        this.District = District;
    }

    public void setPincode(float Pincode) {
        this.Pincode = Pincode;
    }*/
}
