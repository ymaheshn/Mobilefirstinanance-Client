package base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MFFErrorResponse {
    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("error_description")
    @Expose
    public String errorDescription;
}
