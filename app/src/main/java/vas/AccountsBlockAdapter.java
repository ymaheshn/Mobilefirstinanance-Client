package vas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;

import java.util.ArrayList;
import java.util.List;

import onboard.model.CreditCardResponse;
import vas.models.BlockedCardDetailsList;

public class AccountsBlockAdapter extends RecyclerView.Adapter<AccountsBlockAdapter.ViewModel> {

    public Context mContext;
    public IOnCheckBoxBlockCardClickListener iOnCheckBoxBlockCardClickListener;
    private final CreditCardResponse creditCardResponse;
    public BlockedCardDetailsList cardDetailsList = new BlockedCardDetailsList();
    List<BlockedCardDetailsList> blockedCardDetailsLists = new ArrayList<>();

    public AccountsBlockAdapter(Context mContext, CreditCardResponse creditCardResponse, IOnCheckBoxBlockCardClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.iOnCheckBoxBlockCardClickListener = iOnItemClickListener;
        this.creditCardResponse = creditCardResponse;
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accounts, parent, false);
        return new ViewModel(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewModel holder, int position) {
        if (creditCardResponse.getData().getCardDetails() != null) {
            holder.accountName.setText(creditCardResponse.getData().getCardDetails().get(position).getCardDetails().getName());
            holder.accountNumber.setText(creditCardResponse.getData().getCardDetails().get(position).getCardDetails().getCardNumber());

        }

        holder.checkBlockAccount.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.checkBlockAccount.setChecked(true);
                cardDetailsList.setContractUUID(creditCardResponse.getData().getCardDetails().get(position).getContractUUID());
                cardDetailsList.setStatus("block");
                blockedCardDetailsLists.add(cardDetailsList);
            } else {
                holder.checkBlockAccount.setChecked(false);
                blockedCardDetailsLists.remove(cardDetailsList);
            }
            iOnCheckBoxBlockCardClickListener.onCheckBoxItemClicked(position, blockedCardDetailsLists);
        });
    }

    @Override
    public int getItemCount() {
        return creditCardResponse.getData().getCardDetails().size();
    }

    static class ViewModel extends RecyclerView.ViewHolder {
        CheckBox checkBlockAccount;
        AppCompatTextView accountName, accountNumber;
        AppCompatButton blockButton;

        @SuppressLint("NotifyDataSetChanged")
        public ViewModel(View itemView) {
            super(itemView);
            checkBlockAccount = itemView.findViewById(R.id.checkbox_account);
            accountName = itemView.findViewById(R.id.text_account_name);
            accountNumber = itemView.findViewById(R.id.text_account_number);
            blockButton = itemView.findViewById(R.id.block_card_button);

        }

    }

}


