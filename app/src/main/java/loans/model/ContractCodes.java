package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractCodes implements Parcelable {

    @SerializedName("Hierarchy")
    @Expose
    public String hierarchy;

    @SerializedName("Identifier")
    @Expose
    public String identifier;

    @SerializedName("profileID")
    @Expose
    public String contractprofileID;

    @SerializedName("branchId")
    @Expose
    public String branchId;

    @SerializedName("contractUUID")
    @Expose
    public String contractUUID;

    @SerializedName("Name")
    @Expose
    public String name;

    @SerializedName("NationalID")
    @Expose
    public String nationalID;

    @SerializedName("groupName")
    @Expose
    public String groupName;

    @SerializedName("branchName")
    @Expose
    public String branchName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hierarchy);
        dest.writeString(this.identifier);
        dest.writeString(this.contractprofileID);
        dest.writeString(this.branchId);
        dest.writeString(this.contractUUID);
        dest.writeString(this.name);
        dest.writeString(this.nationalID);
        dest.writeString(this.groupName);
        dest.writeString(this.branchName);
    }

    public void readFromParcel(Parcel source) {
        this.hierarchy = source.readString();
        this.identifier = source.readString();
        this.contractprofileID = source.readString();
        this.branchId = source.readString();
        this.contractUUID = source.readString();
        this.name = source.readString();
        this.nationalID = source.readString();
        this.groupName = source.readString();
        this.branchName = source.readString();
    }

    public ContractCodes() {
    }

    protected ContractCodes(Parcel in) {
        this.hierarchy = in.readString();
        this.identifier = in.readString();
        this.contractprofileID = in.readString();
        this.branchId = in.readString();
        this.contractUUID = in.readString();
        this.name = in.readString();
        this.nationalID = in.readString();
        this.groupName = in.readString();
        this.branchName = in.readString();
    }

    public static final Parcelable.Creator<ContractCodes> CREATOR = new Parcelable.Creator<ContractCodes>() {
        @Override
        public ContractCodes createFromParcel(Parcel source) {
            return new ContractCodes(source);
        }

        @Override
        public ContractCodes[] newArray(int size) {
            return new ContractCodes[size];
        }
    };
}