package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import base.MFFResponse;

public class SearchResponse implements Serializable {
    @SerializedName("data")
    @Expose
    public List<SearchData> data = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public Integer status;

}