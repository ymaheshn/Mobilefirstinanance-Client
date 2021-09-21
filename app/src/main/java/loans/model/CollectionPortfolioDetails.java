package loans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;


public class CollectionPortfolioDetails {
    @SerializedName("eventid")
    @Expose
    public String eventid;

    @SerializedName("event_type")
    @Expose
    public String event_type;

    @SerializedName("contractuuid")
    @Expose
    public String contractuuid;

    @SerializedName("profileid")
    @Expose
    public String profileid;

    @SerializedName("contractid")
    @Expose
    public String contractid;

    @SerializedName("eventjson")
    @Expose
    public Map<String, Object> eventjson;

    @SerializedName("event_value")
    @Expose
    public String event_value;

    @SerializedName("event_time")
    @Expose
    public String event_time;
}


