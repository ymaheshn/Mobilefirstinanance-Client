package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

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

    @SerializedName("ContractID")
    @Expose
    public String contractID;

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


    protected ContractCodes(Parcel in) {
        hierarchy = in.readString();
        identifier = in.readString();
        contractprofileID = in.readString();
        branchId = in.readString();
        contractUUID = in.readString();
        contractID = in.readString();
        name = in.readString();
        nationalID = in.readString();
        groupName = in.readString();
        branchName = in.readString();
    }

    public static final Creator<ContractCodes> CREATOR = new Creator<ContractCodes>() {
        @Override
        public ContractCodes createFromParcel(Parcel in) {
            return new ContractCodes(in);
        }

        @Override
        public ContractCodes[] newArray(int size) {
            return new ContractCodes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hierarchy);
        dest.writeString(identifier);
        dest.writeString(contractprofileID);
        dest.writeString(branchId);
        dest.writeString(contractUUID);
        dest.writeString(contractID);
        dest.writeString(name);
        dest.writeString(nationalID);
        dest.writeString(groupName);
        dest.writeString(branchName);
    }


}