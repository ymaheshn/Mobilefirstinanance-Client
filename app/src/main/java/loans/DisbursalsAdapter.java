package loans;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import loans.model.Datum;

public class DisbursalsAdapter extends RecyclerView.Adapter<DisbursalsAdapter.ViewModel> {
    private Context mContext;
    private IOnItemClickListener iOnItemClickListener;

    public DisbursalsAdapter(Context mContext, IOnItemClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disbursals, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        if (position % 3 == 0) {
            holder.textStatus.setText("Delay");
        } else if (position % 2 == 0) {
            holder.textStatus.setText("Disbursed");
        } else {
            holder.textStatus.setText("Executed");
        }

        if (position % 2 == 0) {
            holder.textProduct.setText("Personal Loan");
        } else {
            holder.textProduct.setText("Mortgage Loan");
        }
        holder.textSign.setOnClickListener(view -> {
            SignatureDialog signatureDialog = new SignatureDialog(mContext);
            signatureDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewModel extends RecyclerView.ViewHolder {

        private final TextView textStatus;
        private final TextView textProduct;
        private final TextView textSign;

        public ViewModel(View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.text_status);
            textProduct = itemView.findViewById(R.id.text_product_name);
            textSign = itemView.findViewById(R.id.text_sign);
        }

    }

    public interface IOnItemClickListener {
        void onItemClicked(Datum datum);
    }
}


