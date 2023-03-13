package loans;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;

import loans.model.Datum;

public class LoanCollectionsAdapter extends RecyclerView.Adapter<LoanCollectionsAdapter.ViewModel> {


    private Context mContext;
    private IOnItemClickListener iOnItemClickListener;

    public LoanCollectionsAdapter(Context mContext, IOnItemClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_collection_item, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView nameTV;

        public ViewModel(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

    }

    public interface IOnItemClickListener {
        void onItemClicked(Datum datum);
    }
}


