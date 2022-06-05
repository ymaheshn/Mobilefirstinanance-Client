package dashboard;

import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.List;

import dashboard.models.DashboardWorkflowCount;

public class DashboardDetailsAdapter extends RecyclerView.Adapter<DashboardDetailsAdapter.ViewHolder> {

    private final ArrayList<DashboardWorkflowCount> workflowCounts;
    private final Context context;


    public DashboardDetailsAdapter(Context context) {
        this.context = context;
        this.workflowCounts = new ArrayList<>();
    }

    public void setData(List<DashboardWorkflowCount> workflowCounts) {
        if (workflowCounts != null) {
            this.workflowCounts.addAll(workflowCounts);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dashboard_details, viewGroup, false);
        return new DashboardDetailsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DashboardWorkflowCount dashboardWorkflowCount = workflowCounts.get(i);
        viewHolder.textCount.setText(dashboardWorkflowCount.numberofRecords);
        viewHolder.textTitle.setText(dashboardWorkflowCount.workflowName);
    }

    @Override
    public int getItemCount() {
        return workflowCounts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textTitle;
        private final TextView textCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textCount = itemView.findViewById(R.id.text_count);
        }
    }

}
