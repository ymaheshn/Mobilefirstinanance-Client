package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MobileNumberLoginRequest {

    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;

    @SerializedName("otp")
    @Expose
    public String otp;

}
