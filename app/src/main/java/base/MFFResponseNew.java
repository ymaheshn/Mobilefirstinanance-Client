package base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MFFResponseNew<T> {
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("data")
    @Expose
    public T data;

}
