package login.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

public class MobileOTPResponse implements Parcelable {
    public String sid;
    public String to;
    public String channel;
    public String status;
    public boolean valid;
    public Lookup lookup;
    public Object amount;
    public Object payee;
    public String url;
    public String serviceSid;
    public String accountSid;
    public ArrayList<SendCodeAttempt> sendCodeAttempts;
    public Date dateCreated;
    public Date dateUpdated;

    public MobileOTPResponse(Parcel in) {
        sid = in.readString();
        to = in.readString();
        channel = in.readString();
        status = in.readString();
        valid = in.readByte() != 0;
        url = in.readString();
        serviceSid = in.readString();
        accountSid = in.readString();
    }

    public static final Creator<MobileOTPResponse> CREATOR = new Creator<MobileOTPResponse>() {
        @Override
        public MobileOTPResponse createFromParcel(Parcel in) {
            return new MobileOTPResponse(in);
        }

        @Override
        public MobileOTPResponse[] newArray(int size) {
            return new MobileOTPResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(sid);
        dest.writeString(to);
        dest.writeString(channel);
        dest.writeString(status);
        dest.writeByte((byte) (valid ? 1 : 0));
        dest.writeString(url);
        dest.writeString(serviceSid);
        dest.writeString(accountSid);
    }
}

class Lookup {
    public MobileOTPResponse mobileOTPResponse;
}

class Root {
    public String mobile_country_code;
    public String type;
    public Object error_code;
    public String mobile_network_code;
    public String name;
}

class SendCodeAttempt {
    public String attempt_sid;
    public String channel;
    public Date time;
}
