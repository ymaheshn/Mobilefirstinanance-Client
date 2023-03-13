package signup.model;

import java.util.ArrayList;
import java.util.Date;

public class Carrier {
    public Object mobile_country_code;
    public Object type;
    public Object error_code;
    public Object mobile_network_code;
    public Object name;


    static class Lookup {
        public Carrier carrier;
    }

    static class Root {
        public String sid;
        public String myto;
        public String channel;
        public String status;
        public boolean valid;
        public Lookup lookup;
        public Object amount;
        public Object payee;
        public String url;
        public String serviceSid;
        public String accountSid;
        public ArrayList<SendCodeAttempt> sendCodeAttempts;
        public Date dateCreated;
        public Date dateUpdated;
    }

    static class SendCodeAttempt {
        public Object attempt_sid;
        public String channel_id;
        public String channel;
        public Date time;
    }
}