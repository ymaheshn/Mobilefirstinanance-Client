package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityUsers {
    @SerializedName("profileID")
    @Expose
    public String profileID;

    @SerializedName("profileDetails")
    @Expose
    public EntityProfileDetails profileDetails;
}
