package onboard.model;

import com.google.gson.annotations.SerializedName;

public class ApplyCardResponse {

    @SerializedName("status")
    int status;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    CardDetailsResponse data;
}
