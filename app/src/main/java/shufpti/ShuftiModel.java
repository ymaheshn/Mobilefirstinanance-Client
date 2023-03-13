package shufpti;

import com.google.gson.annotations.SerializedName;

public class ShuftiModel {

    @SerializedName("reference")
    Object reference;

    @SerializedName("event")
    String event;

    @SerializedName("email")
    String email;

    @SerializedName("country")
    String country;

    @SerializedName("verification_data")
    VerificationData verificationData;

    @SerializedName("verification_result")
    VerificationResult verificationResult;

    @SerializedName("info")
    Info info;


    public void setReference(Object reference) {
        this.reference = reference;
    }
    public Object getReference() {
        return reference;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    public String getEvent() {
        return event;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }

    public void setVerificationData(VerificationData verificationData) {
        this.verificationData = verificationData;
    }
    public VerificationData getVerificationData() {
        return verificationData;
    }

    public void setVerificationResult(VerificationResult verificationResult) {
        this.verificationResult = verificationResult;
    }
    public VerificationResult getVerificationResult() {
        return verificationResult;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
    public Info getInfo() {
        return info;
    }
}
