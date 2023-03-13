package onboard.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.odedtech.mff.client.R;

import java.util.ArrayList;

import Utilities.PreferenceConnector;
import networking.GlideApp;
import onboard.LinkClientInterface;
import onboard.OnBoardClientAdapterItemClickListener;
import onboard.model.ClientDashboardModel;
import onboard.model.Workflow;

public class OnBoardClientAdapter extends RecyclerView.Adapter<OnBoardClientAdapter.MyViewHolder> {

    public ClientDashboardModel clientDashboardModel;
    private final ArrayList<Workflow> data;
    private final Context mContext;
    // private ArrayList<String> workFlowName = new ArrayList<>();
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private final LinkClientInterface linkClientInterface;
    private final OnBoardClientAdapterItemClickListener onBoardClientAdapterItemClickListener;

    public OnBoardClientAdapter(Context mContext, ClientDashboardModel clientDashboardModel, LinkClientInterface linkClientInterface, OnBoardClientAdapterItemClickListener onBoardClientAdapterItemClickListener) {
        this.mContext = mContext;
        this.clientDashboardModel = clientDashboardModel;
        this.data = clientDashboardModel.data.workflow;
        this.linkClientInterface = linkClientInterface;
        this.onBoardClientAdapterItemClickListener = onBoardClientAdapterItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.client_on_board_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        viewBinderHelper.setOpenOnlyOne(true);
        holder.textView.setText(data.get(position).workFlowName);
        holder.textDescription.setText(data.get(position).description);

        String accessToken = PreferenceConnector.readString(mContext, mContext.getString(R.string.accessToken), "");
        String urlImage=data.get(position).picture+"?&access_token="+accessToken;
        GlideApp.with(mContext).load(urlImage).into(holder.imageClient);
        viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(data.get(position).workFlowID));
        viewBinderHelper.closeLayout(String.valueOf(data.get(position).workFlowID));

        holder.applyTextView.setOnClickListener(v -> linkClientInterface.linkClient(position, data));

        holder.linearLayout.setOnClickListener(view -> {
            PreferenceConnector.writeInteger(mContext, "ListPosition", position);
            onBoardClientAdapterItemClickListener.onItemClick(position, data);
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatTextView textView, textDescription;
        public AppCompatImageView imageClient;
        public AppCompatTextView applyTextView;
        public SwipeRevealLayout swipeRevealLayout;
        public LinearLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name_on_board);
            textDescription = itemView.findViewById(R.id.name_on_board_description);
            imageClient = itemView.findViewById(R.id.imageClient);
            swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);
            applyTextView = itemView.findViewById(R.id.apply_text_view);
            linearLayout = itemView.findViewById(R.id.item_on_board);

        }
    }

    public void applyClientLink(int position) {
        /*data.remove(position);
        notifyItemRemoved(position);*/
    }

    public ArrayList<Workflow> getData() {
        return data;
    }


}
