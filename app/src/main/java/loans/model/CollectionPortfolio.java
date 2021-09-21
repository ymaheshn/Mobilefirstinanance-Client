package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CollectionPortfolio implements Parcelable {

    @SerializedName("linkedusertoportfolioID")
    @Expose
    public int linkedusertoportfolioID;

    @SerializedName("userid")
    @Expose
    public String userid;

    @SerializedName("rootUserID")
    @Expose
    public String rootUserID;

    @SerializedName("ownerID")
    @Expose
    public String ownerID;

    @SerializedName("contractUUID")
    @Expose
    public String contractUUID;

    @SerializedName("profileID")
    @Expose
    public String profileID;

    @SerializedName("contractCodes")
    @Expose
    public ContractCodes contractCodes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.linkedusertoportfolioID);
        dest.writeString(this.userid);
        dest.writeString(this.rootUserID);
        dest.writeString(this.ownerID);
        dest.writeString(this.contractUUID);
        dest.writeString(this.profileID);
        dest.writeParcelable(this.contractCodes, flags);
    }

    public void readFromParcel(Parcel source) {
        this.linkedusertoportfolioID = source.readInt();
        this.userid = source.readString();
        this.rootUserID = source.readString();
        this.ownerID = source.readString();
        this.contractUUID = source.readString();
        this.profileID = source.readString();
        this.contractCodes = source.readParcelable(ContractCodes.class.getClassLoader());
    }

    public CollectionPortfolio() {
    }

    protected CollectionPortfolio(Parcel in) {
        this.linkedusertoportfolioID = in.readInt();
        this.userid = in.readString();
        this.rootUserID = in.readString();
        this.ownerID = in.readString();
        this.contractUUID = in.readString();
        this.profileID = in.readString();
        this.contractCodes = in.readParcelable(ContractCodes.class.getClassLoader());
    }

    public static final Parcelable.Creator<CollectionPortfolio> CREATOR = new Parcelable.Creator<CollectionPortfolio>() {
        @Override
        public CollectionPortfolio createFromParcel(Parcel source) {
            return new CollectionPortfolio(source);
        }

        @Override
        public CollectionPortfolio[] newArray(int size) {
            return new CollectionPortfolio[size];
        }
    };
}


