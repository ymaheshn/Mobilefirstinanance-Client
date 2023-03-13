package onboard.model;

import com.google.gson.annotations.SerializedName;

public class CardDetailsResponse {

    @SerializedName("contractUUID")
    String contractUUID;

    @SerializedName("message")
    String message;
}
