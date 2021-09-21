package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityResponse {
    @SerializedName("users")
    @Expose
    public List<EntityUsers> users;
}
