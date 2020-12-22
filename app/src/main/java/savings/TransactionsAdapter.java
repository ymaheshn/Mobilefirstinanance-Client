package savings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import loans.SignatureDialog;
import loans.model.Datum;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewModel> {


    private Context mContext;
    private IOnItemClickListener iOnItemClickListener;

    public TransactionsAdapter(Context mContext, IOnItemClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transactions, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        if (position % 2 == 0) {
            holder.textStatus.setText("Performant");
        } else {
            holder.textStatus.setText("Closed");
        }

        if (position % 3 == 0) {
            holder.textProduct.setText("Personal Savings Account");
        } else if (position % 2 == 0) {
            holder.textProduct.setText("Recurring Deposit");
        } else {
            holder.textProduct.setText("Fixed Deposit");
        }
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView textStatus;
        TextView textProduct;
        TextView textSign;

        public ViewModel(View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.text_status);
            textProduct = itemView.findViewById(R.id.text_product);
            textSign = itemView.findViewById(R.id.text_sign);

            textSign.setOnClickListener(view -> {
                SignatureDialog signatureDialog = new SignatureDialog(mContext);
                signatureDialog.show();
            });

            itemView.setOnClickListener(view -> {
            });
        }

    }

    public interface IOnItemClickListener {
        void onItemClicked(Datum datum);
    }
}


