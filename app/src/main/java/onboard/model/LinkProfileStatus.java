package onboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkProfileStatus {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public LinkProfileStatus data;
}
