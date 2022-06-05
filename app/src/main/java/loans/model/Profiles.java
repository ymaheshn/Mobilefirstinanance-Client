package loans.model;

import java.util.Date;
import java.util.logging.Handler;


import onboard.ProfileDetailsDTO;

public class Profiles {

    public String status;
    public String rootUserID;
    public String recordCreatorID;
    public int profileFormID;
    public ProfileDetailsDTO profileDetails;
    public Date profileCreationTime;
    public Handler handler;
    public String profileID;
    public String linkedProfileID;
    public String linkedRootUserProfileID;
    // public HibernateLazyInitializer hibernateLazyInitializer;
    public String identifier;


}
