package dashboard.models;

import com.google.gson.annotations.SerializedName;

public class ApplyNewCardResponse {

    @SerializedName("status")
    int status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    ApplyNewCardData data;


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

    public void setData(ApplyNewCardData data) {
        this.data = data;
    }
    public ApplyNewCardData getData() {
        return data;
    }
}
