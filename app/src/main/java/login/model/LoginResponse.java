package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("access_token")
    @Expose
    public String accessToken;

    @SerializedName("refresh_token")
    @Expose
    public String refreshToken;

    @SerializedName("scope")
    @Expose
    public String scope;

    @SerializedName("token_type")
    @Expose
    public String tokenType;

    @SerializedName("expires_in")
    @Expose
    public long expiresIn;

    @SerializedName("jti")
    @Expose
    public String jti;
}
