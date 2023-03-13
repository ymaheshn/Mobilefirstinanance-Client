package vas.models;

import com.google.gson.annotations.SerializedName;

public class CardBlockStatusResponse {

    @SerializedName("status")
    int status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    BlockCardData data;


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

    public void setData(BlockCardData data) {
        this.data = data;
    }
    public BlockCardData getData() {
        return data;
    }
}
