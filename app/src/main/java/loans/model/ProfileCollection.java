package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ProfileCollection implements Parcelable {
    @SerializedName("contractUUID")
    @Expose
    public String contractUUID;
    @SerializedName("units")
    @Expose
    public String units;
    @SerializedName("eventTime")
    @Expose
    public String eventTime;
    @SerializedName("eventValue")
    @Expose
    public Integer eventValue;
    @SerializedName("eventType")
    @Expose
    public String eventType;
    @SerializedName("eventID")
    @Expose
    public String eventID;
    @SerializedName("profileId")
    @Expose
    public String profileId;
    @SerializedName("rePayment")
    @Expose
    public String rePayment;
    @SerializedName("remainingAmount")
    @Expose
    public String remainingAmount;
    @SerializedName("paymentMethod")
    @Expose
    public String paymentMethod;
    @SerializedName("debitPaymentChannelID")
    @Expose
    public String debitPaymentChannelID;
    @SerializedName("creditPaymentChannelID")
    @Expose
    public String creditPaymentChannelID;

    public Date eventTimeInDate;
    public String eventTimeFormated;

    protected ProfileCollection(Parcel in) {
        contractUUID = in.readString();
        units = in.readString();
        eventTime = in.readString();
        eventValue = in.readByte() == 0x00 ? null : in.readInt();
        eventType = in.readString();
        eventID = in.readString();
        profileId = in.readString();
        rePayment = in.readString();
        remainingAmount = in.readString();
        paymentMethod = in.readString();
        debitPaymentChannelID = in.readString();
        creditPaymentChannelID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contractUUID);
        dest.writeString(units);
        dest.writeString(eventTime);
        if (eventValue == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(eventValue);
        }
        dest.writeString(eventType);
        dest.writeString(eventID);
        dest.writeString(profileId);
        dest.writeString(rePayment);
        dest.writeString(remainingAmount);
        dest.writeString(paymentMethod);
        dest.writeString(debitPaymentChannelID);
        dest.writeString(creditPaymentChannelID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProfileCollection> CREATOR = new Parcelable.Creator<ProfileCollection>() {
        @Override
        public ProfileCollection createFromParcel(Parcel in) {
            return new ProfileCollection(in);
        }

        @Override
        public ProfileCollection[] newArray(int size) {
            return new ProfileCollection[size];
        }
    };
}