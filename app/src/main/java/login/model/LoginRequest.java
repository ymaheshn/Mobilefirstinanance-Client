package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("rootUser")
    @Expose
    public String rootUser;

    @SerializedName("userName")
    @Expose
    public String userName;


    @SerializedName("password")
    @Expose
    public String password;

}
