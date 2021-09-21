package loans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odedtech.mff.mffapp.databinding.ItemLoanCollectionPortfolioBinding;

import java.util.ArrayList;
import java.util.List;

import loans.model.CollectionPortfolio;
import loans.model.Datum;

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
            binding.getRoot().setOnClickListener(view -> {
                iOnItemClickListener.onItemClicked(getItems(), getItems().get(getLayoutPosition()));
            });
        }
    }

    public interface IOnItemClickListener {
        void onItemClicked(List<CollectionPortfolio> items, CollectionPortfolio collectionPortfolio);
    }
}
