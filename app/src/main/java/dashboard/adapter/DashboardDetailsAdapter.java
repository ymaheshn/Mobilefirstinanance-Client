package dashboard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.odedtech.mff.client.R;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import dashboard.models.DashboardWorkflowCount;
import dashboard.models.LinkProfilePayload;
import interfaces.ApplyCardClickListener;
import onboard.model.CardDetailsData;

public class DashboardDetailsAdapter extends RecyclerView.Adapter<DashboardDetailsAdapter.ViewHolder> {

    private final ArrayList<DashboardWorkflowCount> workflowCounts;
    public final Context context;
    public CardDetailsData cardDetailsList;
    private static final int ADD_MORE_VIEW = 2;
    public static ApplyCardClickListener applyCardClickListener;
    public static LinkProfilePayload linkProfilePayload = new LinkProfilePayload();
    private int position;
    private static int colorCode;

    public DashboardDetailsAdapter(Context context, CardDetailsData creditCardResponse, ApplyCardClickListener applyCardClickListener) {
        this.context = context;
        this.workflowCounts = new ArrayList<>();
        this.cardDetailsList = creditCardResponse;
        DashboardDetailsAdapter.applyCardClickListener = applyCardClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<DashboardWorkflowCount> workflowCounts) {
        if (workflowCounts != null) {
            this.workflowCounts.addAll(workflowCounts);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == ADD_MORE_VIEW) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.more_list_item, viewGroup, false);
            return new ViewHolder(view);
        }
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // DashboardWorkflowCount dashboardWorkflowCount = workflowCounts.get(i);
        if (i != cardDetailsList.getCardDetails().size()) {

            String colorTheme = PreferenceConnector.getThemeColor(context);

            colorCode = Color.parseColor(colorTheme);

            viewHolder.cardView.setCardBackgroundColor(colorCode);

            viewHolder.cardNumber.setText(String.valueOf(cardDetailsList.getCardDetails().get(i).getCardDetails().getCardNumber()));
            viewHolder.cardExpiryDate.setText(cardDetailsList.getCardDetails().get(i).getCardDetails().getExpiryDate());
            viewHolder.cardCVV.setText(String.valueOf(cardDetailsList.getCardDetails().get(i).getCardDetails().getCvv()));
            viewHolder.nameOnCard.setText(String.valueOf(cardDetailsList.getCardDetails().get(i).getCardDetails().getName()));

            switch (cardDetailsList.getCardDetails().get(i).getCardDetails().getCardNetwork()) {
                case "Visa":
                    viewHolder.cardTypeImage.setBackgroundResource(R.drawable.visa_image);
                    break;
                case "Mastercard":
                    viewHolder.cardTypeImage.setBackgroundResource(R.drawable.mastercard);
                    break;
                case "Platinum":
                    viewHolder.cardTypeImage.setBackgroundResource(R.drawable.platinum);
                    break;
                case "Stripe":
                    viewHolder.cardTypeImage.setBackgroundResource(R.drawable.stripe);
                    break;
                case "Rupay":
                    viewHolder.cardTypeImage.setBackgroundResource(R.drawable.rupay);
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return cardDetailsList.getCardDetails().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == cardDetailsList.getCardDetails().size()) {
            return ADD_MORE_VIEW;
        }
        return super.getItemViewType(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView cardNumber;
        public AppCompatTextView cardCVV;
        public AppCompatTextView cardExpiryDate;
        public AppCompatTextView nameOnCard;
        public AppCompatImageView cardTypeImage;
        public MaterialCardView applyCardLayout, cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardCVV = itemView.findViewById(R.id.cvv_);
            cardExpiryDate = itemView.findViewById(R.id.expire_date);
            nameOnCard = itemView.findViewById(R.id.name_on_card);
            cardTypeImage = itemView.findViewById(R.id.card_type);
            applyCardLayout = itemView.findViewById(R.id.apply_card_cardView);
            cardView = itemView.findViewById(R.id.front_card_container);


            if (applyCardLayout != null) {
                applyCardLayout.setCardBackgroundColor(colorCode);
                applyCardLayout.setOnClickListener(v -> {
                    applyCardClickListener.applyCardClicked();
                });
            }
        }
    }

}
