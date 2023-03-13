package onboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.odedtech.mff.client.R;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import de.hdodenhof.circleimageview.CircleImageView;

public class OnBoardAdapter extends RecyclerView.Adapter<OnBoardAdapter.ViewModel> implements Filterable {
    private final IOnNoDataFiltered iOnNoDataFiltered;
    private Context mContext;
    private ArrayList<ClientDataDTO> clients;
    private IOnItemClickListener iOnItemClickListener;

    public OnBoardAdapter(Context mContext, ArrayList<ClientDataDTO> clients, IOnItemClickListener iOnItemClickListener,
                          IOnNoDataFiltered iOnNoDataFiltered) {
        this.mContext = mContext;
        this.clients = clients;
        filteredList = clients;
        this.iOnNoDataFiltered = iOnNoDataFiltered;
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public ViewModel onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboard_item, parent, false);
        return new ViewModel(view);
    }

    public void setData(ArrayList<ClientDataDTO> clients, boolean clearAll) {
        if (clients != null && clients.size() > 0) {
            if (clearAll) {
                this.clients.clear();
            }
            this.clients.addAll(clients);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewModel holder, @SuppressLint("RecyclerView") int position) {
        ClientDataDTO clientDataDTO = filteredList.get(position);
        holder.nameTV.setText(clientDataDTO.name);
        holder.textRole.setText(clientDataDTO.identifier);
        holder.textFormLabel.setText(clientDataDTO.formLabel);
        if (!TextUtils.isEmpty(clientDataDTO.profilePicture)) {
            String imageUrl = clientDataDTO.profilePicture + "?access_token=" +
                    PreferenceConnector.readString(mContext, mContext.getString(R.string.accessToken), "");
            Glide.with(mContext).load(imageUrl).placeholder(R.color.colorGray).into(holder.profile_image);
        } else {
            Glide.with(mContext).load(R.drawable.user_profile).into(holder.profile_image);
        }
        holder.itemView.setOnClickListener(view -> {
            PreferenceConnector.writeInteger(mContext, "ListPosition", position);
            iOnItemClickListener.onItemClicked(clientDataDTO);
        });

    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    static class ViewModel extends RecyclerView.ViewHolder {
        TextView textRole;
        CircleImageView profile_image;
        TextView nameTV;
        TextView textFormLabel;

        public ViewModel(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            textRole = itemView.findViewById(R.id.text_role);
            textFormLabel = itemView.findViewById(R.id.text_form_label);
            profile_image = itemView.findViewById(R.id.profile_image);
        }


    }

    private List<ClientDataDTO> filteredList;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    filteredList = clients;
                } else {
                    List<ClientDataDTO> filteredList = new ArrayList<>();
                    for (ClientDataDTO row : clients) {
                        if (!TextUtils.isEmpty(row.name) && row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    OnBoardAdapter.this.filteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<ClientDataDTO>) filterResults.values;
                if (filteredList != null && filteredList.isEmpty()) {
                    iOnNoDataFiltered.noDataFiltered(true);
                } else {
                    iOnNoDataFiltered.noDataFiltered(false);
                }
                notifyDataSetChanged();
            }
        };
    }

    public interface IOnItemClickListener {
        void onItemClicked(ClientDataDTO clientDataDTO);
    }

    public interface IOnNoDataFiltered {
        void noDataFiltered(boolean noData);
    }
}


