package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProfileCollectionResponse implements Parcelable {
    @SerializedName("data")
    @Expose
    public List<ProfileCollection> data = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public Integer status;

    protected ProfileCollectionResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<ProfileCollection>();
            in.readList(data, ProfileCollection.class.getClassLoader());
        } else {
            data = null;
        }
        message = in.readString();
        status = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (data == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(data);
        }
        dest.writeString(message);
        if (status == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(status);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProfileCollectionResponse> CREATOR = new Parcelable.Creator<ProfileCollectionResponse>() {
        @Override
        public ProfileCollectionResponse createFromParcel(Parcel in) {
            return new ProfileCollectionResponse(in);
        }

        @Override
        public ProfileCollectionResponse[] newArray(int size) {
            return new ProfileCollectionResponse[size];
        }
    };
}