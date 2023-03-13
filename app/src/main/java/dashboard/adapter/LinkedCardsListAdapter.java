package dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;
import com.odedtech.mff.client.databinding.ApplyNewCardItemBinding;

import java.util.ArrayList;
import java.util.List;

import dashboard.ApplyNewCardActivity;
import dashboard.models.ApplyNewCardResponse;
import dashboard.models.CardDetails;
import dashboard.models.CardDetailsList;
import onboard.LinkCardInterface;

public class LinkedCardsListAdapter extends RecyclerView.Adapter<LinkedCardsListAdapter.MyViewHolder> {
    public ApplyNewCardResponse applyNewCardResponse;
    public Context context;
    public static LinkCardInterface linkCardInterface;
    public ApplyNewCardActivity applyNewCardActivity;
    private String linkedProfileId;
    public int index = 0;
    public CardDetailsList cardDetailsList = new CardDetailsList();
    public List<CardDetails> list = new ArrayList<>();
    public CardDetails cardDetail = new CardDetails();

    public LinkedCardsListAdapter(Context applicationContext, ApplyNewCardResponse applyNewCardResponse, String linkedProfileId, ApplyNewCardActivity applyNewCardActivity, LinkCardInterface linkCardInterface) {
        this.applyNewCardResponse = applyNewCardResponse;
        this.context = applicationContext;
        this.applyNewCardActivity = applyNewCardActivity;
        LinkedCardsListAdapter.linkCardInterface = linkCardInterface;
        this.linkedProfileId = linkedProfileId;
    }

    @NonNull
    @Override
    public LinkedCardsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apply_new_card_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinkedCardsListAdapter.MyViewHolder holder, int position) {
        if (applyNewCardResponse.getData() != null) {
            holder.binding.contractName.setText(applyNewCardResponse.getData().getPortfolio().get(position).getProductName().trim());
            holder.binding.contractId.setText(applyNewCardResponse.getData().getPortfolio().get(position).getContractID());

        }
        holder.binding.checkBoxApply.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                holder.binding.checkBoxApply.setChecked(true);
                cardDetail.setContractUUID(applyNewCardResponse.getData().getPortfolio().get(position).getContractUUID());
                cardDetail.setStatus("apply");
                cardDetail.setLinkedProfileId(linkedProfileId);
                cardDetail.setProductName(applyNewCardResponse.getData().getPortfolio().get(position).getProductName());
                cardDetail.setContractID(applyNewCardResponse.getData().getPortfolio().get(position).getContractID());
                list.add(cardDetail);
            } else {
                holder.binding.checkBoxApply.setChecked(false);
                list.remove(cardDetail);
            }
            cardDetailsList.setCardDetails(list);
            linkCardInterface.getCardLink(cardDetailsList, position);
        });


    }

    @Override
    public int getItemCount() {
        return applyNewCardResponse.getData().getPortfolio().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ApplyNewCardItemBinding binding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ApplyNewCardItemBinding.bind(itemView);
        }
    }
}
