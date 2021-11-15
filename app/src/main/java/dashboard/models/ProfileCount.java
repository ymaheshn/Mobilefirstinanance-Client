package dashboard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileCount {
    @SerializedName("value")
    @Expose
    public int value;
}
