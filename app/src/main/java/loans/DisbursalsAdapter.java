package loans;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import com.odedtech.mff.mffapp.databinding.ItemDisbursalsBinding;
import com.odedtech.mff.mffapp.databinding.ItemLoanCollectionPortfolioBinding;
import loans.model.CollectionPortfolio;
import loans.model.Datum;
import loans.model.LoansPortfolio;

import java.util.ArrayList;
import java.util.List;

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

    private List<LoansPortfolio> items = new ArrayList<>();

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
        viewHolder.bind(items.get(i));
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

        public void bind(LoansPortfolio item) {
            binding.textProductName.setText(item.product_name);
            binding.tvClientId.setText(item.loanContractCodes.getIdentifier());
            binding.tvClientName.setText(item.loanContractCodes.getName());
            binding.tvNatId.setText("" + item.loanContractCodes.getNationalID());
            binding.tvContractId.setText(item.loanContractCodes.getContractID());
            binding.tvBranchName.setText(item.loanContractCodes.getBranchName());
            binding.tvHierarchyName.setText(item.loanContractCodes.getGroupName());
            binding.tvStatus.setText(item.status);

            binding.textSign.setOnClickListener(view -> {
                SignatureDialog signatureDialog = new SignatureDialog(mContext);
                signatureDialog.show();
            });
            binding.tvView.setOnClickListener(v -> {
                if (itemViewClickListener != null) {
                    itemViewClickListener.onItemViewClick(item.loanContractCodes.getProfileID());
                }
            });
            binding.getRoot().setOnClickListener(view -> {
                iOnItemClickListener.onItemClicked(getItems(), getItems().get(getLayoutPosition()));
            });
        }
    }

    public interface IOnItemClickListener {
        void onItemClicked(List<LoansPortfolio> items, LoansPortfolio loansPortfolio);
    }

    public interface ItemViewClickListener {
        void onItemViewClick(String profileId);
    }
}



