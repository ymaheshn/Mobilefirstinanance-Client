package loans;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Utilities.AlertDialogUtils;
import loans.model.CollectionPortfolioDetails;
import loans.model.Installments;
import loans.model.LoanBluetoothData;

public class InstallmentsAdapter extends RecyclerView.Adapter<InstallmentsAdapter.ViewHolder> {

    private final ArrayList<Installments> collections;
    private final OnUpdateAmountInterface onUpdateAmountInterface;
    private final Context context;
    private int totalAmount;
    private int selectedPosition = -1;
    int x = 0;


    public InstallmentsAdapter(Context context, ArrayList<Installments> collections,
                               OnUpdateAmountInterface onUpdateAmountInterface) {
        this.context = context;
        this.collections = collections;
        this.onUpdateAmountInterface = onUpdateAmountInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_installment, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Installments installments = collections.get(position);
        CollectionPortfolioDetails profileCollection = installments.collectionPR;
        CollectionPortfolioDetails profileCollectionNext = installments.collectionIP;
        viewHolder.textDate.setText(profileCollection.event_time);
        if (selectedPosition == position) {
            viewHolder.checkInstallment.setChecked(installments.isChecked);
        } else {
            installments.isChecked = false;
            viewHolder.checkInstallment.setChecked(false);
        }
       /* if(i == 0 && !installments.isChecked){
            viewHolder.checkInstallment.setChecked(false);
        }*/
        if (profileCollection.event_type.equalsIgnoreCase("PR")) {
            viewHolder.textPrincipal.setText(String.valueOf(profileCollection.event_value));
            viewHolder.textInterest.setText(String.valueOf(profileCollectionNext.event_value));
        } else {
            viewHolder.textPrincipal.setText(String.valueOf(profileCollectionNext.event_value));
            viewHolder.textInterest.setText(String.valueOf(profileCollection.event_value));
        }
        viewHolder.textTotal.setText(String.valueOf(Integer.parseInt(profileCollection.event_value) + Integer.parseInt(profileCollectionNext.event_value)));
        //   viewHolder.checkInstallment.setOnCheckedChangeListener(null);
        // viewHolder.checkInstallment.setChecked(installments.isChecked);
        viewHolder.checkInstallment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                collections.get(position).isChecked = true;
                x += Integer.parseInt(viewHolder.textTotal.getText().toString());
              //  installments.isChecked = !installments.isChecked;
            } else {
                collections.get(position).isChecked = false;
                x -= Integer.parseInt(viewHolder.textTotal.getText().toString());
               // installments.isChecked = !installments.isChecked;
            }
            onUpdateAmountInterface.onUpdate(x);
            //  boolean previousSelection = checkPreviousSelection(viewHolder.getAdapterPosition());
              /*  if (previousSelection) {
                    deselectThePreviousSelections(viewHolder.getAdapterPosition());
                    int totalAmountInstallment = Integer.parseInt(viewHolder.textTotal.getText().toString());
                    // if (!installments.isChecked) {
                    onUpdateAmountInterface.onUpdate(totalAmountInstallment);
                    // installments.isChecked = !installments.isChecked;
                    notifyDataSetChanged();
                    if (!installments.isChecked) {
                        totalAmount = totalAmount + totalAmountInstallment;
                    } else {
                        deselectThePreviousSelections(viewHolder.getAdapterPosition());
                        totalAmount = totalAmount - totalAmountInstallment;
                    }

                    onUpdateAmountInterface.onUpdate(totalAmountInstallment);
                    installments.isChecked = !installments.isChecked;
                    notifyDataSetChanged();
                }*/
                /* else{
                    if (installments.get 0) {
                        AlertDialogUtils.getAlertDialogUtils().showOkAlert((Activity) context, "Please select only one installment at a time");

                    } else {
                        AlertDialogUtils.getAlertDialogUtils().showOkAlert((Activity) context, "Please select the previous installments");
                    }
                }*/
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


        }


    }

    private void deselectThePreviousSelections(int adapterPosition) {
        for (int i = adapterPosition + 1; i < collections.size(); i++) {
            Installments installment = collections.get(i);
            if (installment.isChecked) {
                installment.isChecked = false;
                totalAmount = totalAmount - (Integer.parseInt(installment.collectionPR.event_value) + Integer.parseInt(installment.collectionIP.event_value));
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

    public JSONArray getSavedPortfolioData() {
        JSONArray jsonArray = new JSONArray();
        Gson gson = new Gson();
        for (Installments collection : collections) {
            if (collection.isChecked) {
                try {

                    JSONObject jsonObjectFromPR = new JSONObject(gson.toJson(collection.collectionPR, CollectionPortfolioDetails.class));
                    JSONObject eventJSONPR = (JSONObject) jsonObjectFromPR.get("eventjson");
                    JSONObject eventJSONTransPR = (JSONObject) eventJSONPR.get("Transaction");

                    JSONObject jsonObjectPR = new JSONObject();

                    jsonObjectPR.put("ContractUUID", jsonObjectFromPR.get("contractuuid"));
                    jsonObjectPR.put("EventID", jsonObjectFromPR.get("eventid"));
                    jsonObjectPR.put("EventTime", jsonObjectFromPR.get("event_time").toString().concat("T00:00"));
                    jsonObjectPR.put("EventType", jsonObjectFromPR.get("event_type"));
                    jsonObjectPR.put("Units", eventJSONTransPR.get("Units").toString());
                    jsonObjectPR.put("Value", Double.parseDouble(eventJSONTransPR.get("Value").toString()));
                    jsonObjectPR.put("DebitPaymentChannelID", eventJSONTransPR.get("DebitPaymentChannelID").toString());
                    jsonObjectPR.put("CreditPaymentChannelID", eventJSONTransPR.get("CreditPaymentChannelID").toString());
                    jsonObjectPR.put("PaymentMethod", eventJSONTransPR.get("PaymentMethod").toString());
                    jsonObjectPR.put("RemainingAmount", Double.parseDouble(eventJSONTransPR.get("RemainingAmount").toString()));
                    jsonObjectPR.put("ProcessTime", jsonObjectFromPR.get("event_time").toString().concat("T00:00"));
                    jsonArray.put(jsonObjectPR);

                    JSONObject jsonObjectIP = new JSONObject();

                    JSONObject jsonObjectFromIP = new JSONObject(gson.toJson(collection.collectionIP, CollectionPortfolioDetails.class));
                    JSONObject eventJSONIP = (JSONObject) jsonObjectFromIP.get("eventjson");
                    JSONObject eventJSONTransIP = (JSONObject) eventJSONIP.get("Transaction");


                    jsonObjectIP.put("ContractUUID", jsonObjectFromIP.get("contractuuid"));
                    jsonObjectIP.put("EventID", jsonObjectFromIP.get("eventid"));
                    jsonObjectIP.put("EventTime", jsonObjectFromIP.get("event_time").toString().concat("T00:00"));
                    jsonObjectIP.put("EventType", jsonObjectFromIP.get("event_type"));
                    jsonObjectIP.put("Units", eventJSONTransIP.get("Units").toString());
                    jsonObjectIP.put("Value", Double.parseDouble(eventJSONTransIP.get("Value").toString()));
                    jsonObjectIP.put("DebitPaymentChannelID", eventJSONTransIP.get("DebitPaymentChannelID").toString());
                    jsonObjectIP.put("CreditPaymentChannelID", eventJSONTransIP.get("CreditPaymentChannelID").toString());
                    jsonObjectIP.put("PaymentMethod", eventJSONTransIP.get("PaymentMethod").toString());
                    jsonObjectIP.put("RemainingAmount", Double.parseDouble(eventJSONTransIP.get("RemainingAmount").toString()));
                    jsonObjectIP.put("ProcessTime", jsonObjectFromIP.get("event_time").toString().concat("T00:00"));
                    jsonArray.put(jsonObjectIP);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }


    public LoanBluetoothData getBluetoothData(LoanBluetoothData existingData) {
        int interest = 0, principal = 0, total = 0;
        LoanBluetoothData bluetoothData;
        if (existingData != null) {
            bluetoothData = existingData;
        } else {
            bluetoothData = new LoanBluetoothData();
        }
        for (Installments collection : collections) {
            if (collection.isChecked) {
                principal = principal + Integer.parseInt(collection.collectionPR.event_value);
                interest = interest + Integer.parseInt(collection.collectionIP.event_value);
                total = total + Integer.parseInt(collection.collectionPR.event_value) + Integer.parseInt(collection.collectionIP.event_value);
            }
        }
        bluetoothData.interest = bluetoothData.interest + interest;
        bluetoothData.principal = bluetoothData.principal + principal;
        bluetoothData.total = bluetoothData.total + total;
        return bluetoothData;
    }

    public interface OnUpdateAmountInterface {
        void onUpdate(double value);
    }
}
