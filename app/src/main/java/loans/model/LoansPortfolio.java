package loans.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoansPortfolio implements Parcelable {

    @SerializedName("userid")
    @Expose
    public String userid;

    @SerializedName("units")
    @Expose
    public String units;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("product_name")
    @Expose
    public String product_name;

    @SerializedName("event_value")
    @Expose
    public String event_value;

    @SerializedName("event_type")
    @Expose
    public String event_type;

    @SerializedName("contractuuid")
    @Expose
    public String contractuuid;

    @SerializedName("contractid")
    @Expose
    public String contractid;

    @SerializedName("eventid")
    @Expose
    public String eventid;

    @SerializedName("profile_identifier")
    @Expose
    public String profile_identifier;

    @SerializedName("profileid")
    @Expose
    public String profileid;

    @SerializedName("re_payment")
    @Expose
    public String re_payment;

    @SerializedName("event_time")
    @Expose
    public String event_time;

    @SerializedName("sync_date")
    @Expose
    public String sync_date;

    @SerializedName("contract_codes")
    @Expose
    public LoanContractCodes loanContractCodes;

    protected LoansPortfolio(Parcel in) {
        userid = in.readString();
        units = in.readString();
        status = in.readString();
        product_name = in.readString();
        event_value = in.readString();
        event_type = in.readString();
        contractuuid = in.readString();
        contractid = in.readString();
        eventid = in.readString();
        profile_identifier = in.readString();
        profileid = in.readString();
        re_payment = in.readString();
        event_time = in.readString();
        sync_date = in.readString();
        loanContractCodes = in.readParcelable(LoanContractCodes.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userid);
        dest.writeString(units);
        dest.writeString(status);
        dest.writeString(product_name);
        dest.writeString(event_value);
        dest.writeString(event_type);
        dest.writeString(contractuuid);
        dest.writeString(contractid);
        dest.writeString(eventid);
        dest.writeString(profile_identifier);
        dest.writeString(profileid);
        dest.writeString(re_payment);
        dest.writeString(event_time);
        dest.writeString(sync_date);
        dest.writeParcelable(loanContractCodes, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LoansPortfolio> CREATOR = new Creator<LoansPortfolio>() {
        @Override
        public LoansPortfolio createFromParcel(Parcel in) {
            return new LoansPortfolio(in);
        }

        @Override
        public LoansPortfolio[] newArray(int size) {
            return new LoansPortfolio[size];
        }
    };
}
