package vas.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BlockCardsPayload {

    @SerializedName("cardDetails")
    List<BlockedCardDetailsList> cardDetailsList = new ArrayList<>();

    public List<BlockedCardDetailsList> getCardDetailsList() {
        return cardDetailsList;
    }

    public void setCardDetailsList(List<BlockedCardDetailsList> cardDetailsList) {
        this.cardDetailsList = cardDetailsList;
    }
}
