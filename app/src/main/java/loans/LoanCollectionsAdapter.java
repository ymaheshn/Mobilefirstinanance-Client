package loans;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;

import loans.model.Datum;
import loans.model.LinkedProfilesResponse;

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


