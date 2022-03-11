package loans;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.odedtech.mff.mffapp.R;

import java.util.List;

import loans.model.Datum;
import loans.model.SearchData;
import loans.model.SearchResponse;
import vas.AccountsBlockAdapter;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewModel> {

    private Context mContext;
    private IOnSearchItemClickListener iOnItemClickListener;
    private List<SearchData> mList;

    private int index = -1;

    public SearchAdapter(Context mContext, IOnSearchItemClickListener iOnItemClickListener, List<SearchData> mList) {
        this.mContext = mContext;
        this.iOnItemClickListener = iOnItemClickListener;
        this.mList = mList;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item_layout, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        holder.mTitle.setText(mList.get(position).branch_name);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView mTitle;

        public ViewModel(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTitle.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                     SearchData mData = mList.get(getAdapterPosition());
                    iOnItemClickListener.onItemClicked(mData);
                }
            });
        }
    }

    public interface IOnSearchItemClickListener {
        void onItemClicked(SearchData mData);
    }


}


