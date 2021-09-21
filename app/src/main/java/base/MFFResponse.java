package base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MFFResponse<T> {
    @SerializedName("statusCode")
    @Expose
    public String statusCode;

    @SerializedName("statusCodeValue")
    @Expose
    public int statusCodeValue;

    @SerializedName("body")
    @Expose
    public T body;

}
