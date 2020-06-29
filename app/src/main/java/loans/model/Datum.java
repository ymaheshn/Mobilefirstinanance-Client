package loans.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Datum implements Parcelable {

    public boolean isPending = true;

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @SerializedName("linkedusertoportfolioID")
    @Expose
    public Integer linkedusertoportfolioID;
    @SerializedName("userid")
    @Expose
    public String userid;
    @SerializedName("rootUserID")
    @Expose
    public String rootUserID;

    @SerializedName("profileID")
    @Expose
    public String profileID;

    @SerializedName("contractUUID")
    @Expose
    public String contractUUID;

    @SerializedName("ownerID")
    @Expose
    public String ownerID;

    @Embedded
    @SerializedName("contractCodes")
    @Expose
    public ContractCodes contractCodes;

    public Datum() {

    }

    protected Datum(Parcel in) {
        linkedusertoportfolioID = in.readByte() == 0x00 ? null : in.readInt();
        userid = in.readString();
        rootUserID = in.readString();
        ownerID = in.readString();
        profileID = in.readString();
        contractUUID = in.readString();
        contractCodes = (ContractCodes) in.readValue(ContractCodes.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (linkedusertoportfolioID == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(linkedusertoportfolioID);
        }
        dest.writeString(userid);
        dest.writeString(rootUserID);
        dest.writeString(ownerID);
        dest.writeString(profileID);
        dest.writeString(contractUUID);
        dest.writeValue(contractCodes);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel in) {
            return new Datum(in);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }
    };
}