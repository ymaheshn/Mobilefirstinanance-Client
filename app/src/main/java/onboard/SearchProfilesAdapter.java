package onboard;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;

import loans.model.SearchData;

import java.util.List;

public class SearchProfilesAdapter extends RecyclerView.Adapter<SearchProfilesAdapter.ViewModel> {

    private Context mContext;
    private List<SearchData> clients;
    private OnItemClickListener onItemClickListener;

    public SearchProfilesAdapter(Context mContext, List<SearchData> clients, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.clients = clients;
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item_layout, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        holder.mTitle.setText(clients.get(position).branch_name);
        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener!=null){
                onItemClickListener.onItemClicked(clients.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView mTitle;

        public ViewModel(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
        }
    }
    public interface OnItemClickListener {
        void onItemClicked(SearchData clientDataDTO);
    }
}
