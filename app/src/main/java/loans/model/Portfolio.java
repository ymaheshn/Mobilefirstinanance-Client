package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Portfolio implements Parcelable {
    @SerializedName("profilejson")
    @Expose
    public Profilejson ProfilejsonObject;

    @SerializedName("eventid")
    public String eventid;

    @SerializedName("contractuuid")
    public String contractuuid;

    @SerializedName("profileid")
    public String profileid;

    @SerializedName("contractid")
    public String contractid;

    @SerializedName("contractjson")
    @Expose
    public Contractjson ContractjsonObject;

    @SerializedName("status")
    @Expose
    public String status;

    protected Portfolio(Parcel in) {
        eventid = in.readString();
        contractuuid = in.readString();
        profileid = in.readString();
        contractid = in.readString();
        status = in.readString();
    }

    public static final Creator<Portfolio> CREATOR = new Creator<Portfolio>() {
        @Override
        public Portfolio createFromParcel(Parcel in) {
            return new Portfolio(in);
        }

        @Override
        public Portfolio[] newArray(int size) {
            return new Portfolio[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventid);
        dest.writeString(contractuuid);
        dest.writeString(profileid);
        dest.writeString(contractid);
        dest.writeString(status);
    }


    // Getter Methods

   /* public Profilejson getProfilejson() {
        return ProfilejsonObject;
    }

    public String getEventid() {
        return eventid;
    }

    public String getContractuuid() {
        return contractuuid;
    }

    public String getProfileid() {
        return profileid;
    }

    public String getContractid() {
        return contractid;
    }

    public Contractjson getContractjson() {
        return ContractjsonObject;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods

    public void setProfilejson(Profilejson profilejsonObject) {
        this.ProfilejsonObject = profilejsonObject;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public void setContractuuid(String contractuuid) {
        this.contractuuid = contractuuid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public void setContractid(String contractid) {
        this.contractid = contractid;
    }

    public void setContractjson(Contractjson contractjsonObject) {
        this.ContractjsonObject = contractjsonObject;
    }

    public void setStatus(String status) {
        this.status = status;
    }*/
}
/*class Contractjson {
    ArrayList< Object > Terms = new ArrayList < Object > ();


    // Getter Methods

    public ArrayList<Object> getTerms() {
        return Terms;
    }

    public void setTerms(ArrayList<Object> terms) {
        Terms = terms;
    }


}*/
