package otp;

import com.google.gson.annotations.SerializedName;

public class TokenResponseDTO {

    @SerializedName("userID")
    public String userID;
    @SerializedName("ownerID")
    public String ownerID;
    @SerializedName("rootUserID")
    public String rootUserID;
    @SerializedName("flag")
    public String flag;
    @SerializedName("portNumber")
    public String portNumber;
    @SerializedName("url")
    public String url;
}
