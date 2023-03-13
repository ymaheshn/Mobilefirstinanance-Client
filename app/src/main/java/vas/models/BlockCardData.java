package vas.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlockCardData {

    @SerializedName("cardDetails")
    List<BlockedCardDetails> cardDetails;


    public void setCardDetails(List<BlockedCardDetails> cardDetails) {
        this.cardDetails = cardDetails;
    }
    public List<BlockedCardDetails> getCardDetails() {
        return cardDetails;
    }
}
