package vas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.mffapp.R;

import loans.model.Datum;

public class AccountsBlockAdapter extends RecyclerView.Adapter<AccountsBlockAdapter.ViewModel> {

    private Context mContext;
    private IOnItemClickListener iOnItemClickListener;

    private int index = -1;

    public AccountsBlockAdapter(Context mContext, IOnItemClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accounts, parent, false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(ViewModel holder, int position) {
        holder.checkBlockAccount.setChecked(position == index);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    class ViewModel extends RecyclerView.ViewHolder {
        CheckBox checkBlockAccount;

        public ViewModel(View itemView) {
            super(itemView);
            checkBlockAccount = itemView.findViewById(R.id.checkbox_account);
            checkBlockAccount.setOnClickListener(view -> {
                index = getLayoutPosition();
                notifyDataSetChanged();
            });
        }

    }

    public interface IOnItemClickListener {
        void onItemClicked(Datum datum);
    }
}


