package loans;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.databinding.ItemLoanCollectionPortfolioBinding;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import loans.model.CollectionPortfolio;

public class LoanCollectionPortfolioAdapter extends
        RecyclerView.Adapter<LoanCollectionPortfolioAdapter.ViewHolder> {

    private final IOnItemClickListener iOnItemClickListener;

    public LoanCollectionPortfolioAdapter(IOnItemClickListener iOnItemClickListener) {
        this.iOnItemClickListener = iOnItemClickListener;
    }

    private List<CollectionPortfolio> items = new ArrayList<>();

    public void setData(List<CollectionPortfolio> data, boolean clearAll) {
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
        ItemLoanCollectionPortfolioBinding binding = ItemLoanCollectionPortfolioBinding
                .inflate(inflater, viewGroup, false);
        return new ViewHolder(binding);
    }

    public List<CollectionPortfolio> getItems() {
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
        private final ItemLoanCollectionPortfolioBinding binding;

        public ViewHolder(@NonNull ItemLoanCollectionPortfolioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CollectionPortfolio item) {
            binding.textPortfolioName.setText(item.contractCodes.name);
            binding.textIdentifier.setText(item.contractCodes.identifier);
            binding.textGroupName.setText(item.contractCodes.groupName);
            binding.textBranchName.setText(item.contractCodes.branchName);
            binding.textContractId.setVisibility(View.GONE);
            if (item.contractCodes.contractID != null) {
                binding.textContractId.setVisibility(View.VISIBLE);
                binding.textContractId.setText(item.contractCodes.contractID);
            }
            String colorTheme = PreferenceConnector.getThemeColor(itemView.getContext());
            int colorCode = Color.parseColor(colorTheme);
            binding.textIdentifier.setTextColor(colorCode);
            binding.textContractId.setTextColor(colorCode);
            binding.nextArrow.setColorFilter(colorCode);

            binding.getRoot().setOnClickListener(view -> {
                iOnItemClickListener.onItemClicked(getItems(), getItems().get(getLayoutPosition()));
            });
        }
    }

    public interface IOnItemClickListener {
        void onItemClicked(List<CollectionPortfolio> items, CollectionPortfolio collectionPortfolio);
    }
}
