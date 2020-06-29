package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractCodes implements Parcelable {

    @SerializedName("Hierarchy")
    @Expose
    public String group;
    @SerializedName("Identifier")
    @Expose
    public String identifier;
    @SerializedName("profileID")
    @Expose
    public String contractprofileID;
    @SerializedName("branchId")
    @Expose
    public String branch;
    @SerializedName("contractUUID")
    @Expose
    public String contractCodeUUID;
    @SerializedName("Name")
    @Expose
    public String name;

    public ContractCodes() {

    }

    public ContractCodes(Parcel in) {
        group = in.readString();
        identifier = in.readString();
        contractprofileID = in.readString();
        branch = in.readString();
        contractCodeUUID = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group);
        dest.writeString(identifier);
        dest.writeString(contractprofileID);
        dest.writeString(branch);
        dest.writeString(contractCodeUUID);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ContractCodes> CREATOR = new Parcelable.Creator<ContractCodes>() {
        @Override
        public ContractCodes createFromParcel(Parcel in) {
            return new ContractCodes(in);
        }

        @Override
        public ContractCodes[] newArray(int size) {
            return new ContractCodes[size];
        }
    };
}