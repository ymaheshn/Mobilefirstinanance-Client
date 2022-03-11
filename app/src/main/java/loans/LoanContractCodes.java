package loans;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoanContractCodes implements Parcelable {

    @SerializedName("Identifier")
    @Expose
    private String identifier;
    @SerializedName("NationalID")
    @Expose
    private Long nationalID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("branchId")
    @Expose
    private Integer branchId;
    @SerializedName("Hierarchy")
    @Expose
    private Integer hierarchy;
    @SerializedName("profileID")
    @Expose
    private String profileID;
    @SerializedName("contractUUID")
    @Expose
    private String contractUUID;
    @SerializedName("ContractID")
    @Expose
    private String contractID;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("branchName")
    @Expose
    private String branchName;

    protected LoanContractCodes(Parcel in) {
        identifier = in.readString();
        if (in.readByte() == 0) {
            nationalID = null;
        } else {
            nationalID = in.readLong();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            branchId = null;
        } else {
            branchId = in.readInt();
        }
        if (in.readByte() == 0) {
            hierarchy = null;
        } else {
            hierarchy = in.readInt();
        }
        profileID = in.readString();
        contractUUID = in.readString();
        contractID = in.readString();
        groupName = in.readString();
        branchName = in.readString();
    }

    public static final Creator<LoanContractCodes> CREATOR = new Creator<LoanContractCodes>() {
        @Override
        public LoanContractCodes createFromParcel(Parcel in) {
            return new LoanContractCodes(in);
        }

        @Override
        public LoanContractCodes[] newArray(int size) {
            return new LoanContractCodes[size];
        }
    };

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Long getNationalID() {
        return nationalID;
    }

    public void setNationalID(Long nationalID) {
        this.nationalID = nationalID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public Integer getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(Integer hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getContractUUID() {
        return contractUUID;
    }

    public void setContractUUID(String contractUUID) {
        this.contractUUID = contractUUID;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identifier);
        if (nationalID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(nationalID);
        }
        dest.writeString(name);
        if (branchId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(branchId);
        }
        if (hierarchy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(hierarchy);
        }
        dest.writeString(profileID);
        dest.writeString(contractUUID);
        dest.writeString(contractID);
        dest.writeString(groupName);
        dest.writeString(branchName);
    }
}


