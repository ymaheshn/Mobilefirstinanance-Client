package loans.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.odedtech.mff.mffapp.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Utilities.PreferenceConnector;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {
    private Context context;
    private List<DocumentData> documentsList;

    public DocumentsAdapter(Context context, List<DocumentData> documentsList) {
        this.context = context;
        this.documentsList = documentsList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_document, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int position) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        String imageUrl = documentsList.get(position).getDownloadUrl() + "?access_token=" + PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
       Glide.with(context).load(imageUrl).apply(options).into(viewHolder.iv_img);
    }

    @Override
    public int getItemCount() {
        return documentsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
        }
    }
}
