package loans;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import de.hdodenhof.circleimageview.CircleImageView;
import loans.model.ContractCodes;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import onboard.ClientDataDTO;

public class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.ViewModel> implements Filterable {

    private final IOnNoDataFiltered iOnNoDataFiltered;
    private List<Datum> contractCodes;
    private IOnItemClickListener iOnItemClickListener;

    public LoansAdapter(Context mContext, List<Datum> contractCodes, IOnItemClickListener iOnItemClickListener,
                        IOnNoDataFiltered iOnNoDataFiltered) {
        this.contractCodes = contractCodes;
        this.contactListFiltered = contractCodes;
        this.iOnItemClickListener = iOnItemClickListener;
        this.iOnNoDataFiltered = iOnNoDataFiltered;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboard_item, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        Datum datum = this.contactListFiltered.get(position);
        ContractCodes contractCodes = datum.contractCodes;
        holder.nameTV.setText(contractCodes.name);
        holder.textRole.setText(contractCodes.identifier);
        holder.textAmount.setVisibility(View.VISIBLE);
//        int balanceAmount = datum.contractCodes.nextTotalCollection;
//        int collectionAmount = contractCodes.collectionAmount;
//        if (contractCodes.nextTotalCollection < contractCodes.collectionAmount) {
//            balanceAmount = 0;
//            collectionAmount = 0;
//        }
//        balanceAmount = balanceAmount - collectionAmount;
//        holder.textAmount.setText("₹" + balanceAmount);
//        holder.imgLoanStatus.setVisibility(View.VISIBLE);
//        if (contractCodes.nextCollectionPrincipal + contractCodes.nextCollectionInterest == 0) {
//            holder.imgLoanStatus.setImageResource(R.drawable.ic_check_circle_black_24dp);
//        } else {
//            holder.imgLoanStatus.setImageResource(R.drawable.ic_cancel);
//        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    public void addItems(ArrayList<Datum> data) {
        if (data != null) {
            contractCodes.clear();
            contractCodes.addAll(data);
            contactListFiltered.clear();
            contactListFiltered.addAll(data);
            notifyDataSetChanged();
        }
    }

    List<Datum> contactListFiltered;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    contactListFiltered = contractCodes;
                } else {
                    List<Datum> filteredList = new ArrayList<>();
                    for (Datum row : contractCodes) {
                        ContractCodes contractCodes = row.contractCodes;
                        if (!TextUtils.isEmpty(contractCodes.name) && contractCodes.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Datum>) filterResults.values;
                if (contactListFiltered.isEmpty()) {
                    iOnNoDataFiltered.noDataFiltered(true);
                } else {
                    iOnNoDataFiltered.noDataFiltered(false);
                }
                notifyDataSetChanged();
            }
        };
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView nameTV;
        TextView textRole;
        TextView textAmount;
        ImageView imgLoanStatus;

        public ViewModel(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            textRole = itemView.findViewById(R.id.text_role);
            textAmount = itemView.findViewById(R.id.text_amount);
            textAmount = itemView.findViewById(R.id.text_amount);
            imgLoanStatus = itemView.findViewById(R.id.img_loan_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iOnItemClickListener.onItemClicked(contractCodes.get(getLayoutPosition()));
                }
            });
        }

    }

    public interface IOnItemClickListener {
        void onItemClicked(Datum datum);
    }

    public interface IOnNoDataFiltered {
        void noDataFiltered(boolean noData);
    }
}


