package onboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odedtech.mff.mffapp.R;

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
        ClientDataDTO clientDataDTO = filteredList.get(position);
        holder.nameTV.setText(clientDataDTO.name);
        holder.textRole.setText(clientDataDTO.identifier);
        if (!TextUtils.isEmpty(clientDataDTO.profilePicture)) {
            String imageUrl = clientDataDTO.profilePicture + "?access_token=" +
                    PreferenceConnector.readString(mContext, mContext.getString(R.string.accessToken), "");
            Glide.with(mContext).load(imageUrl).placeholder(R.color.colorGray).into(holder.profile_image);
        } else {
            Glide.with(mContext).load(R.drawable.default_profile).into(holder.profile_image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceConnector.writeInteger(mContext, "ListPosition", position);
                iOnItemClickListener.onItemClicked(clientDataDTO);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    class ViewModel extends RecyclerView.ViewHolder {
        TextView textRole;
        CircleImageView profile_image;
        TextView nameTV;

        public ViewModel(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            textRole = itemView.findViewById(R.id.text_role);
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


