package signup.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;

import java.util.ArrayList;
import java.util.List;

import signup.listener.SelectedBranchInterface;
import signup.model.BranchName;
import signup.model.Branches;

public class BranchesAdapter extends RecyclerView.Adapter<BranchesAdapter.MyViewHolder> implements Filterable {

    public ArrayList<String> branchNameList1 = new ArrayList<>();
    private ArrayList<Integer> currentBranchIdList = new ArrayList<>();
    private ArrayList<String> branchLevelList = new ArrayList<>();
    private Context context;
    private BranchName branchNameNew = new BranchName();

    private Branches branches;
    private ArrayList<BranchName> mBranchesName = new ArrayList<>();

    private ArrayList<BranchName> branchNameArrayList;
    private SelectedBranchInterface selectedBranchInterface;
    ArrayList<Branches> branchesList=new ArrayList<>();


    public BranchesAdapter(Context applicationContext, Branches branches, ArrayList<BranchName> mBranchesName, SelectedBranchInterface selectedBranchInterface, ArrayList<Branches> branchesList) {
        this.context = applicationContext;
        this.branches = branches;
        this.mBranchesName = mBranchesName;
        this.selectedBranchInterface = selectedBranchInterface;
        this.branchesList=branchesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_item_layout, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Branches branches=branchesList.get(position);
        holder.branchName.setText(branches.getBranchName());

        holder.itemView.setOnClickListener(v -> selectedBranchInterface.selectedBranch(position, branches));
    }

    @Override
    public int getItemCount() {
        return branchesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView branchName;
        public ArrayList<BranchName> branchNameArrayList;
        public int currentBranchId;
        public View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            branchName = itemView.findViewById(R.id.title);
            view = itemView;

        }
    }

    public void filterList(ArrayList<BranchName> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        //  branchNameNew = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

  /*  public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        animalNamesList.clear();
        if (charText.length() == 0) {
            animalNamesList.addAll(arraylist);
        } else {
            for (AnimalNames wp : arraylist) {
                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    animalNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<BranchName> filteredList = new ArrayList<>();
                filteredList.addAll(mBranchesName);
                if (charString.isEmpty()) {
                    filteredList.addAll(filteredList);
                } else {
                    for (BranchName branchesName : mBranchesName) {
                        if (branchesName.getBranchName().contains(charString.toLowerCase())) {
                            filteredList.add(branchesName);
                        }

                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults
                    filterResults) {
                branches.getBranchNameList().clear();
                branches.getBranchNameList().addAll((List) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}
