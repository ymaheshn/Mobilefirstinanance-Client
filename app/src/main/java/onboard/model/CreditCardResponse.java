package onboard.model;

import com.google.gson.annotations.SerializedName;

public class CreditCardResponse {

    @SerializedName("status")
    int status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    CardDetailsData data;


    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(CardDetailsData data) {
        this.data = data;
    }
    public CardDetailsData getData() {
        return data;
    }

}
