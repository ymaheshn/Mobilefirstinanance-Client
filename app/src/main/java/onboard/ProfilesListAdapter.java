package onboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.odedtech.mff.client.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Utilities.PreferenceConnector;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilesListAdapter extends RecyclerView.Adapter<ProfilesListAdapter.ViewModel> {

    private Context mContext;
    private ArrayList<ClientDataDTO> clients;
    private IOnItemClickListener iOnItemClickListener;

    public ProfilesListAdapter(Context mContext, ArrayList<ClientDataDTO> clients,
                               IOnItemClickListener iOnItemClickListener) {
        this.mContext = mContext;
        this.clients = clients;
        filteredList = clients;
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
    public void onBindViewHolder(@NonNull @NotNull ViewModel holder, @SuppressLint("RecyclerView") int position) {
        ClientDataDTO clientDataDTO = filteredList.get(position);
        holder.nameTV.setText(clientDataDTO.name);
        holder.textRole.setText(clientDataDTO.identifier);
        holder.textFormLabel.setText(clientDataDTO.formLabel);
        if (!TextUtils.isEmpty(clientDataDTO.profilePicture)) {
            String imageUrl = clientDataDTO.profilePicture + "?access_token=" +
                    PreferenceConnector.readString(mContext, mContext.getString(R.string.accessToken), "");
            Glide.with(mContext).load(imageUrl).placeholder(R.color.colorGray).into(holder.profile_image);
        } else {
            Glide.with(mContext).load(R.drawable.default_profile).into(holder.profile_image);
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

    class ViewModel extends RecyclerView.ViewHolder {
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

    public interface IOnItemClickListener {
        void onItemClicked(ClientDataDTO clientDataDTO);
    }
}
