package login.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import base.ResponseDTO;

public class LoginResponse extends ResponseDTO {
    @SerializedName("login")
    public Login login;

    public class Login {

        @SerializedName("message")
        @Expose
        public String message;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("token")
        @Expose
        public Token token;
    }

    public class Token {

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
        public Integer expiresIn;
        @SerializedName("jti")
        @Expose
        public String jti;
    }
}
