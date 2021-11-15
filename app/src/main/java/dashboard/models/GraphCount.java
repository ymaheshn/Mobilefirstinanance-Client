package dashboard.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GraphCount {
    @SerializedName("1-30")
    @Expose
    public int value30;

    @SerializedName("31-60")
    @Expose
    public int value60;

    @SerializedName("61-120")
    @Expose
    public int value120;

    @SerializedName("181-240")
    @Expose
    public int value240;

    @SerializedName(">360")
    @Expose
    public int valueGreater360;

    @SerializedName("241-360")
    @Expose
    public int value360;

    @SerializedName("121-180")
    @Expose
    public int value180;

}
