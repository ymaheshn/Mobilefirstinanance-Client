package dashboard.models;

import java.util.ArrayList;
import java.util.List;

public class LinkProfilePayload {

    private List<CardDetails> cardDetails = new ArrayList<CardDetails>();

    public List<CardDetails> getCardDetails() {
        return cardDetails;
    }

    public void setCardDetails(List<CardDetails> cardDetails) {
        this.cardDetails = cardDetails;
    }
}
