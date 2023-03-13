package vas;

import java.util.List;

import vas.models.BlockedCardDetailsList;

public interface IOnCheckBoxBlockCardClickListener {

    void onCheckBoxItemClicked(int position, List<BlockedCardDetailsList> cardDetailsList);
}
