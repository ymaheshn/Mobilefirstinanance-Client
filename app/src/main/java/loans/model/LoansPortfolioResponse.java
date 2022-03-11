package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoansPortfolioResponse {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("data")
    @Expose
    public LoansPortfolioData data;
}
