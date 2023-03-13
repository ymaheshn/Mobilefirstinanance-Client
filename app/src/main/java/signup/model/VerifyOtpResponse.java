package signup.model;

import java.util.Date;

public class VerifyOtpResponse {

    public String sid;
    //  @JsonProperty("to")
    public String to;
    public String channel;
    public String status;
    public boolean valid;
    public Object amount;
    public Object payee;
    public String serviceSid;
    public String accountSid;
    public Date dateCreated;
    public Date dateUpdated;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
