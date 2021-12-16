package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchData implements Serializable  {

    @SerializedName("branchid")
    @Expose
    public String  branchid;

    @SerializedName("branch_name")
    @Expose
    public String branch_name;






}