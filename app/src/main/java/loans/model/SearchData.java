package loans.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class SearchData implements Serializable  {

    @SerializedName("branchid")
    @Expose
    public double branchid;

    @SerializedName("branch_name")
    @Expose
    public String branch_name;






}