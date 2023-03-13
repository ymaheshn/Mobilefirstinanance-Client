package shufpti;

import com.google.gson.annotations.SerializedName;

public class Info {
    @SerializedName("agent")
    Agent agent;

    @SerializedName("geolocation")
    Geolocation geolocation;


    public void setAgent(Agent agent) {
        this.agent = agent;
    }
    public Agent getAgent() {
        return agent;
    }

    public void setGeolocation(Geolocation geolocation) {
        this.geolocation = geolocation;
    }
    public Geolocation getGeolocation() {
        return geolocation;
    }
}
