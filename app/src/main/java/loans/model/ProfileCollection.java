package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class ProfileCollection implements Serializable ,Parcelable {

    @SerializedName("eventid")
    @Expose
    public String eventid;

    @SerializedName("event_type")
    @Expose
    public String event_type;

    @SerializedName("contractuuid")
    @Expose
    public String contractuuid;

    @SerializedName("profileid")
    @Expose
    public String profileid;

    @SerializedName("contractid")
    @Expose
    public String contractid;

    @SerializedName("eventjson")
    @Expose
    public Map<String, Object> eventjson;

    @SerializedName("event_value")
    @Expose
    public String event_value;

    @SerializedName("event_time")
    @Expose
    public String event_time;


    protected ProfileCollection(Parcel in) {
        eventid = in.readString();
        event_type = in.readString();
        contractuuid = in.readString();
        profileid = in.readString();
        contractid = in.readString();
        event_value = in.readString();
        event_time = in.readString();
    }

    public static final Creator<ProfileCollection> CREATOR = new Creator<ProfileCollection>() {
        @Override
        public ProfileCollection createFromParcel(Parcel in) {
            return new ProfileCollection(in);
        }

        @Override
        public ProfileCollection[] newArray(int size) {
            return new ProfileCollection[size];
        }
    };

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    @Override
    public String toString() {
        return "ProfileCollection2{" +
                "eventid='" + eventid + '\'' +
                ", event_type='" + event_type + '\'' +
                ", contractuuid='" + contractuuid + '\'' +
                ", profileid='" + profileid + '\'' +
                ", contractid='" + contractid + '\'' +
                ", eventjson=" + eventjson +
                ", event_value='" + event_value + '\'' +
                ", event_time='" + event_time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventid);
        dest.writeString(event_type);
        dest.writeString(contractuuid);
        dest.writeString(profileid);
        dest.writeString(contractid);
        dest.writeString(event_value);
        dest.writeString(event_time);
    }


//    @SerializedName("contractUUID")
//    @Expose
//    public String contractUUID;
//    @SerializedName("units")
//    @Expose
//    public String units;
//    @SerializedName("eventTime")
//    @Expose
//    public String eventTime;
//    @SerializedName("eventValue")
//    @Expose
//    public Integer eventValue;
//    @SerializedName("eventType")
//    @Expose
//    public String eventType;
//    @SerializedName("eventID")
//    @Expose
//    public String eventID;
//    @SerializedName("profileId")
//    @Expose
//    public String profileId;
//    @SerializedName("rePayment")
//    @Expose
//    public String rePayment;
//    @SerializedName("remainingAmount")
//    @Expose
//    public String remainingAmount;
//    @SerializedName("paymentMethod")
//    @Expose
//    public String paymentMethod;
//    @SerializedName("debitPaymentChannelID")
//    @Expose
//    public String debitPaymentChannelID;
//    @SerializedName("creditPaymentChannelID")
//    @Expose
//    public String creditPaymentChannelID;
//
//    public Date eventTimeInDate;
//    public String eventTimeFormated;
//
////    protected ProfileCollection2(Parcel in) {
////        contractUUID = in.readString();
////        units = in.readString();
////        eventTime = in.readString();
////        eventValue = in.readByte() == 0x00 ? null : in.readInt();
////        eventType = in.readString();
////        eventID = in.readString();
////        profileId = in.readString();
////        rePayment = in.readString();
////        remainingAmount = in.readString();
////        paymentMethod = in.readString();
////        debitPaymentChannelID = in.readString();
////        creditPaymentChannelID = in.readString();
////    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(contractUUID);
//        dest.writeString(units);
//        dest.writeString(eventTime);
//        if (eventValue == null) {
//            dest.writeByte((byte) (0x00));
//        } else {
//            dest.writeByte((byte) (0x01));
//            dest.writeInt(eventValue);
//        }
//        dest.writeString(eventType);
//        dest.writeString(eventID);
//        dest.writeString(profileId);
//        dest.writeString(rePayment);
//        dest.writeString(remainingAmount);
//        dest.writeString(paymentMethod);
//        dest.writeString(debitPaymentChannelID);
//        dest.writeString(creditPaymentChannelID);
//    }
//
//    @SuppressWarnings("unused")
//    public static final Creator<ProfileCollection2> CREATOR = new Creator<ProfileCollection2>() {
//        @Override
//        public ProfileCollection2 createFromParcel(Parcel in) {
//            return new ProfileCollection2(in);
//        }
//
//        @Override
//        public ProfileCollection2[] newArray(int size) {
//            return new ProfileCollection2[size];
//        }
//    };


}