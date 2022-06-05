package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataTerms implements Parcelable {
    @SerializedName("portfolio")
    @Expose
    public Portfolio portfolioNew;

    protected DataTerms(Parcel in) {
    }

    public static final Creator<DataTerms> CREATOR = new Creator<DataTerms>() {
        @Override
        public DataTerms createFromParcel(Parcel in) {
            return new DataTerms(in);
        }

        @Override
        public DataTerms[] newArray(int size) {
            return new DataTerms[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
