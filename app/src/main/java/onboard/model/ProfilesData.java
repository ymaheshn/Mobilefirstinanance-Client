package onboard.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfilesData {

    @SerializedName("profiles")
    List<Profiles> profiles;

    public void setProfiles(List<Profiles> profiles) {
        this.profiles = profiles;
    }
    public List<Profiles> getProfiles() {
        return profiles;
    }

}
