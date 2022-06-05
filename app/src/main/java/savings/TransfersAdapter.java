package savings;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.mffapp.R;

import loans.model.Datum;

public class TransfersAdapter extends RecyclerView.Adapter<TransfersAdapter.ViewModel> {


    private Context mContext;
    private IOnItemClickListener iOnItemClickListener;

    public TransfersAdapter(Context mContext, IOnItemClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transfers, parent, false);
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


