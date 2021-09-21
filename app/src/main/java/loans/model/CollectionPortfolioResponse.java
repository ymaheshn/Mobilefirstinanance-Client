package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import base.MFFResponse;

public class CollectionPortfolioResponse extends MFFResponse {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("data")
    @Expose
    public Data data;
}
