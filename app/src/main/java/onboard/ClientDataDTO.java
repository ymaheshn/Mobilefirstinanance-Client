package onboard;

import java.io.Serializable;


/**
 * Created by gufran khan on 28-07-2018.
 */

public class ClientDataDTO implements Serializable {
    public String profileId;
    public int formId;
    public String kycData;
    public String name;
    public String profilePicture;

    public double latitude;
    public double longitude;

    public String status;
    public String identifier;
    public String formLabel;
}
