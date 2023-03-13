package signup.model;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;

public class EmailResponse {

    public Object mobile_country_code;
    public Object type;
    public Object error_code;
    public Object mobile_network_code;
    public Object name;

    public Object getMobile_country_code() {
        return mobile_country_code;
    }

    public void setMobile_country_code(Object mobile_country_code) {
        this.mobile_country_code = mobile_country_code;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getError_code() {
        return error_code;
    }

    public void setError_code(Object error_code) {
        this.error_code = error_code;
    }

    public Object getMobile_network_code() {
        return mobile_network_code;
    }

    public void setMobile_network_code(Object mobile_network_code) {
        this.mobile_network_code = mobile_network_code;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public static class SendCodeAttempt {
        public Object attempt_sid;
        public String channel_id;
        public String channel;
        public Date time;

        public Object getAttempt_sid() {
            return attempt_sid;
        }

        public void setAttempt_sid(Object attempt_sid) {
            this.attempt_sid = attempt_sid;
        }

        public String getChannel_id() {
            return channel_id;
        }

        public void setChannel_id(String channel_id) {
            this.channel_id = channel_id;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public static class Carrier {
            public String sid;
            public String myto;
            public String channel;
            public String status;
            public boolean valid;
            public MethodHandles.Lookup lookup;
            public Object amount;
            public Object payee;
            public String url;
            public String serviceSid;
            public String accountSid;
            public ArrayList<SendCodeAttempt> sendCodeAttempts;
            public Date dateCreated;
            public Date dateUpdated;

            public String getSid() {
                return sid;
            }

            public void setSid(String sid) {
                this.sid = sid;
            }

            public String getMyto() {
                return myto;
            }

            public void setMyto(String myto) {
                this.myto = myto;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public boolean isValid() {
                return valid;
            }

            public void setValid(boolean valid) {
                this.valid = valid;
            }

            public MethodHandles.Lookup getLookup() {
                return lookup;
            }

            public void setLookup(MethodHandles.Lookup lookup) {
                this.lookup = lookup;
            }

            public Object getAmount() {
                return amount;
            }

            public void setAmount(Object amount) {
                this.amount = amount;
            }

            public Object getPayee() {
                return payee;
            }

            public void setPayee(Object payee) {
                this.payee = payee;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getServiceSid() {
                return serviceSid;
            }

            public void setServiceSid(String serviceSid) {
                this.serviceSid = serviceSid;
            }

            public String getAccountSid() {
                return accountSid;
            }

            public void setAccountSid(String accountSid) {
                this.accountSid = accountSid;
            }

            public ArrayList<SendCodeAttempt> getSendCodeAttempts() {
                return sendCodeAttempts;
            }

            public void setSendCodeAttempts(ArrayList<SendCodeAttempt> sendCodeAttempts) {
                this.sendCodeAttempts = sendCodeAttempts;
            }

            public Date getDateCreated() {
                return dateCreated;
            }

            public void setDateCreated(Date dateCreated) {
                this.dateCreated = dateCreated;
            }

            public Date getDateUpdated() {
                return dateUpdated;
            }

            public void setDateUpdated(Date dateUpdated) {
                this.dateUpdated = dateUpdated;
            }
        }

    }
}

