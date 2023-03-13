package onboard.model;

import com.google.gson.annotations.SerializedName;

public class ProfileDetailsResponse {

    @SerializedName("status")
    int status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    ProfilesData data;


    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(ProfilesData data) {
        this.data = data;
    }
    public ProfilesData getData() {
        return data;
    }
}
