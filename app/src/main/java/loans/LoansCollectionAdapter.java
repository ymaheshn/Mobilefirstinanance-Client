package loans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import loans.model.CollectionPortfolioDetails;
import loans.model.Installments;
import loans.model.LoanBluetoothData;
/*This is Accounts Adapter*/
public class LoansCollectionAdapter extends RecyclerView.Adapter<LoansCollectionAdapter.ViewHolder> {

    private final ArrayList<Installments> collections;
    private final OnUpdateAmount onUpdateAmount;
    private final Context context;
    private int totalAmount;
    private int selectedPosition = -1;
    private boolean[] checkedArr;
    int x = 0;


    public LoansCollectionAdapter(Context context, ArrayList<Installments> collections,
                                  OnUpdateAmount onUpdateAmount) {
        this.context = context;
        this.collections = collections;
        this.onUpdateAmount = onUpdateAmount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_installment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Installments installments = collections.get(i);
        CollectionPortfolioDetails profileCollection = installments.collectionPR;
        viewHolder.textDate.setText(profileCollection.event_time);
        viewHolder.checkInstallment.setChecked(installments.isChecked);
        if (selectedPosition == i) {
            viewHolder.checkInstallment.setChecked(installments.isChecked);
        } else {
            installments.isChecked = false;
            viewHolder.checkInstallment.setChecked(false);
        }
        viewHolder.ll_interest.setVisibility(View.GONE);
        viewHolder.tv_principal.setText("Event Type");
        viewHolder.textPrincipal.setText(profileCollection.event_type);
        String totalText = profileCollection.event_value;
        if (profileCollection.event_value.contains("-")) {
            viewHolder.textTotal.setText(totalText.substring(1));
        } else {
            viewHolder.textTotal.setText(totalText);
        }

        viewHolder.checkInstallment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                x += Integer.parseInt(viewHolder.textTotal.getText().toString());
                collections.get(i).isChecked = true;
                // sumAllCheckedAndNotify();
                // int totalAmountInstallment = Integer.parseInt(viewHolder.textTotal.getText().toString());
                // onUpdateAmount.onUpdate(x);
            } else {
                //sumAllCheckedAndNotifyNew();
                collections.get(i).isChecked = false;
                x -= Integer.parseInt(viewHolder.textTotal.getText().toString());
                //  int totalAmountInstallment = Integer.parseInt(viewHolder.textTotal.getText().toString());
            }
            onUpdateAmount.onUpdate(x);
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
        private final TextView tv_principal;
        private final LinearLayout ll_interest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.text_date);
            textInterest = itemView.findViewById(R.id.text_interest);
            textPrincipal = itemView.findViewById(R.id.text_prinicpal);
            textTotal = itemView.findViewById(R.id.text_total);
            checkInstallment = itemView.findViewById(R.id.check_installment);
            tv_principal = itemView.findViewById(R.id.tv_principal);
            ll_interest = itemView.findViewById(R.id.ll_interest);
          /*  itemView.setOnClickListener(v -> {
                Installments installments = collections.get(getAdapterPosition());

                selectedPosition = getAdapterPosition();
            //    if(selectedPosition == 0){
                    if(installments.isChecked){
                        onUpdateAmount.onUpdate(0.0);
                        installments.isChecked = !installments.isChecked;
                        notifyDataSetChanged();
                        return;
                    }else{
                        int totalAmountInstallment = Integer.parseInt(textTotal.getText().toString());
                        // if (!installments.isChecked) {
                        onUpdateAmount.onUpdate(totalAmountInstallment);
                        installments.isChecked = !installments.isChecked;
                        notifyDataSetChanged();
                    }
                    boolean previousSelection = checkPreviousSelection(getAdapterPosition());
                    if (previousSelection) {
                  *//* deselectThePreviousSelections(getAdapterPosition());
                   int totalAmountInstallment = Integer.parseInt(textTotal.getText().toString());
                  // if (!installments.isChecked) {
                       onUpdateAmount.onUpdate(totalAmountInstallment);
                       // installments.isChecked = !installments.isChecked;
                       notifyDataSetChanged();*//*
                        // }
                    }
               // }
               *//* else {
                    if(selectedPosition > 0){
                        AlertDialogUtils.getAlertDialogUtils().showOkAlert((Activity) context, "Please select only one installment at a time");

                    }
                }*//*
                //    } else {
                //      AlertDialogUtils.getAlertDialogUtils().showOkAlert((Activity) context, "Please select the previous installments");
                //}
            });*/
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
        String eventType = "";
        LoanBluetoothData bluetoothData;
        if (existingData != null) {
            bluetoothData = existingData;
        } else {
            bluetoothData = new LoanBluetoothData();
        }
        for (Installments collection : collections) {
            if (collection.isChecked) {
                principal = principal + Integer.parseInt(collection.collectionPR.event_value);
                interest = interest + Integer.parseInt(collection.collectionPR.event_value);
                total = total + Integer.parseInt(collection.collectionPR.event_value);

                eventType = collection.collectionPR.event_type;
            }
        }
        bluetoothData.interest = bluetoothData.interest + interest;
        bluetoothData.principal = bluetoothData.principal + principal;
        bluetoothData.total = bluetoothData.total + total;
        bluetoothData.eventType = bluetoothData.eventType + eventType;
        return bluetoothData;
    }

    public interface OnUpdateAmount {
        void onUpdate(double value);
    }
}
