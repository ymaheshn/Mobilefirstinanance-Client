package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayloadOTPResponse {

    @SerializedName("mobileNumber")
    @Expose
    public String mobileNumber;

    @SerializedName("otp")
    @Expose
    public String otp;

    public PayloadOTPResponse(String mobileNumber, String otp) {
        this.mobileNumber = mobileNumber;
        this.otp = otp;
    }
}
