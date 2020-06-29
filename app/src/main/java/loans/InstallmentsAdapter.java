package loans;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Utilities.AlertDialogUtils;
import loans.model.Installments;
import loans.model.LoanBluetoothData;
import loans.model.ProfileCollection;

public class InstallmentsAdapter extends RecyclerView.Adapter<InstallmentsAdapter.ViewHolder> {

    private final ArrayList<Installments> collections;
    private final OnUpdateAmount onUpdateAmount;
    private final Context context;
    private int totalAmount;


    public InstallmentsAdapter(Context context, ArrayList<Installments> collections, OnUpdateAmount onUpdateAmount) {
        this.context = context;
        this.collections = collections;
        this.onUpdateAmount = onUpdateAmount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_installment, viewGroup, false);
        return new InstallmentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Installments installments = collections.get(i);
        ProfileCollection profileCollection = installments.collectionPR;
        ProfileCollection profileCollectionNext = installments.collectionIP;
        viewHolder.textDate.setText(profileCollection.eventTimeFormated);
        if (profileCollection.eventType.equalsIgnoreCase("PR")) {
            viewHolder.textPrincipal.setText(String.valueOf(profileCollection.eventValue));
            viewHolder.textInterest.setText(String.valueOf(profileCollectionNext.eventValue));
        } else {
            viewHolder.textPrincipal.setText(String.valueOf(profileCollectionNext.eventValue));
            viewHolder.textInterest.setText(String.valueOf(profileCollection.eventValue));
        }
        viewHolder.textTotal.setText(String.valueOf(profileCollection.eventValue + profileCollectionNext.eventValue));
        viewHolder.checkInstallment.setOnCheckedChangeListener(null);
        viewHolder.checkInstallment.setChecked(installments.isChecked);
        viewHolder.checkInstallment.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textDate;
        private final CheckBox checkInstallment;
        private final TextView textInterest;
        private final TextView textPrincipal;
        private final TextView textTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.text_date);
            textInterest = itemView.findViewById(R.id.text_interest);
            textPrincipal = itemView.findViewById(R.id.text_prinicpal);
            textTotal = itemView.findViewById(R.id.text_total);
            checkInstallment = itemView.findViewById(R.id.check_installment);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Installments installments = collections.get(getAdapterPosition());
                    boolean previousSelection = checkPreviousSelection(getAdapterPosition());
                    if (previousSelection) {
                        int totalAmountInstallment = Integer.parseInt(textTotal.getText().toString());
                        if (!installments.isChecked) {
                            totalAmount = totalAmount + totalAmountInstallment;
                        } else {
                            deselectThePreviousSelections(getAdapterPosition());
                            totalAmount = totalAmount - totalAmountInstallment;
                        }
                        onUpdateAmount.onUpdate(totalAmount);
                        installments.isChecked = !installments.isChecked;
                        notifyDataSetChanged();
                    } else {
                        AlertDialogUtils.getAlertDialogUtils().showOkAlert((Activity) context, "Please select the previous installments");
                    }
                }
            });
        }

        private void deselectThePreviousSelections(int adapterPosition) {
            for (int i = adapterPosition + 1; i < collections.size(); i++) {
                Installments installment = collections.get(i);
                if (installment.isChecked) {
                    installment.isChecked = false;
                    totalAmount = totalAmount - (installment.collectionPR.eventValue + installment.collectionIP.eventValue);
                }
            }
        }

        private boolean checkPreviousSelection(int adapterPosition) {
            for (int i = adapterPosition - 1; i >= 0; i--) {
                if (!collections.get(i).isChecked) {
                    return false;
                }
            }
            return true;
        }
    }

    public JSONArray getSavedPortfolioData() {
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        for (Installments collection : collections) {
            if (collection.isChecked) {
                try {
                    jsonArray.put(new JSONObject(gson.toJson(collection.collectionPR, ProfileCollection.class)));
                    jsonArray.put(new JSONObject(gson.toJson(collection.collectionIP, ProfileCollection.class)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }

    public LoanBluetoothData getBluetoothData() {
        int interest = 0, principal = 0, total = 0;
        LoanBluetoothData bluetoothData = new LoanBluetoothData();
        for (Installments collection : collections) {
            if (collection.isChecked) {
                principal = principal + collection.collectionPR.eventValue;
                interest = interest + collection.collectionIP.eventValue;
                total = total + collection.collectionPR.eventValue + collection.collectionIP.eventValue;
            }
        }
        bluetoothData.interest = interest;
        bluetoothData.principal = interest;
        bluetoothData.total = interest;
        return bluetoothData;
    }

    public interface OnUpdateAmount {
        void onUpdate(double value);
    }
}
