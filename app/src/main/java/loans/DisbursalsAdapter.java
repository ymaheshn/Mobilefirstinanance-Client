package loans;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.databinding.ItemDisbursalsBinding;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import loans.model.LoansPortfolio;

public class DisbursalsAdapter extends
        RecyclerView.Adapter<DisbursalsAdapter.ViewHolder> {
    private Context mContext;
    private final IOnItemClickListener iOnItemClickListener;
    private final ItemViewClickListener itemViewClickListener;

    public DisbursalsAdapter(Context context, ItemViewClickListener itemViewClickListener,
                             IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
        this.mContext = context;
        this.itemViewClickListener = itemViewClickListener;
    }

    private final List<LoansPortfolio> items = new ArrayList<>();

    public void setData(List<LoansPortfolio> data, boolean clearAll) {
        if (data != null && data.size() > 0) {
            if (clearAll) {
                items.clear();
            }
            items.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemDisbursalsBinding binding = ItemDisbursalsBinding
                .inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    public List<LoansPortfolio> getItems() {
        return items;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(items.get(i), i);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDisbursalsBinding binding;

        public ViewHolder(@NonNull ItemDisbursalsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LoansPortfolio item, int position) {
            binding.textProductName.setText(item.product_name);
            binding.tvContractId.setText(item.loanContractCodes.getContractID());
            binding.balanceTv.setText(item.eventJSON.getTransaction().getNominalValue());

            String colorTheme = PreferenceConnector.getThemeColor(mContext);
            int colorCode = Color.parseColor(colorTheme);

            binding.textProductName.setBackgroundColor(colorCode);
            binding.balanceTv.setTextColor(colorCode);
            binding.nextImage.setColorFilter(colorCode);

            binding.textSign.setOnClickListener(view -> {
                SignatureDialog signatureDialog = new SignatureDialog(mContext);
                signatureDialog.show();
            });
            binding.tvView.setOnClickListener(v -> {
                if (itemViewClickListener != null) {
                    itemViewClickListener.onItemViewClick(item.loanContractCodes.getProfileID());
                }
            });
            binding.tvTerms.setOnClickListener(v -> {
                if (itemViewClickListener != null) {
                    itemViewClickListener.onItemViewTermsClick(item.loanContractCodes.getContractUUID());
                }
            });
            binding.getRoot().setOnClickListener(view -> {
                iOnItemClickListener.onItemClicked(getItems(), getItems().get(getLayoutPosition()), position);
            });
        }
    }

    public interface IOnItemClickListener {
        void onItemClicked(List<LoansPortfolio> items, LoansPortfolio loansPortfolio, int position);
    }

    public interface ItemViewClickListener {
        void onItemViewClick(String profileId);

        void onItemViewTermsClick(String contractUUID);
    }
}



