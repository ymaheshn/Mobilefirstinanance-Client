package login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityProfileDetails {
    @SerializedName("Entity Name")
    @Expose
    public String entityName;

    @SerializedName("Entity Country")
    @Expose
    public String entityCountry;

    @SerializedName("Entity Address")
    public String entityAddress;

    @SerializedName("Mobile Number")
    public long mobileNumber;

    @SerializedName("Root Email")
    public String rootEmail;

    @SerializedName("Currency")
    public String currency;

    @SerializedName("Tax ID")
    public String taxID;

    @SerializedName("Theme color")
    public String themecolor;

    @SerializedName("url")
    public String url;

    @SerializedName("Identifier")
    public String identifier;

    @SerializedName("Form Label")
    public String formLabel;

    @SerializedName("port")
    public int port;

    @SerializedName("Entity Logo")
    public String entityLogo;

    @SerializedName("Root Password")
    public String rootPassword;

    @SerializedName("Entity Legal Form")
    public String entityLegalForm;

    @SerializedName("User")
    public String user;
}
