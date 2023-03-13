package dashboard.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardDetailsList {

    @SerializedName("cardDetails")
    private List<CardDetails> cardDetails;

    public List<CardDetails> getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(List<CardDetails> cardDetails) {
        this.cardDetails = cardDetails;
    }
}
