package onboard.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CardDetailsData {

    @SerializedName("cardDetails")
    List<CardDetailsList> cardDetails=new ArrayList<>();

    public void setCardDetails(List<CardDetailsList> cardDetails) {
        this.cardDetails = cardDetails;
    }
    public List<CardDetailsList> getCardDetails() {
        return cardDetails;
    }

    public boolean isChecked;
}
