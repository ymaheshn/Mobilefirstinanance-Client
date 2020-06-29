package cashflow;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.mffapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.UtilityMethods;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import onboard.TabDto;
import onboard.TabFields;
import onboard.WorkFlowTemplateDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashFLowFragment extends Fragment implements ICashFlowCallBacks {

    @BindView(R.id.mainCashFlowLL)
    LinearLayout cashFlowView;

    private Unbinder unbinder;
    private ProgressDialog progressDialog = null;
    private CashFlowPresenter cashFlowPresenter;
    private ArrayList<TabFields> tabFieldsArrayList = null;
    public WorkFlowTemplateDto workFlowTemplateDt = null;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.saveBTN)
    Button saveBTN;
    private String templateDetailsId = "";

    public CashFLowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_flow, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cashFlowPresenter = new CashFlowPresenter(getActivity(), this);
        workFlowTemplateDt = Constants.workFlowTemplateDt;
        tabFieldsArrayList = getCashFlowFields();
        if (tabFieldsArrayList != null && tabFieldsArrayList.size() > 0)
            addDynamicViews();

        String cashFlowId = getCashFlowId();
        cashFlowPresenter.getCashFlowData(cashFlowId, workFlowTemplateDt.workFlowProfileId);
    }

    private String getCashFlowId() {
        String id = "";
        for (TabDto tabDto : workFlowTemplateDt.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CASH_FLOW)) {
                id = String.valueOf(tabDto.tabId);
            }
        }
        return id;
    }

    private void addDynamicViews() {
        for (TabFields tabFields : tabFieldsArrayList) {
            addFieldsToLayout(tabFields);
        }
    }

    private void addFieldsToLayout(TabFields tabFields) {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_cash_flow, null);
        EditText editText = view.findViewById(R.id.editText);
        TextView titleTV = view.findViewById(R.id.titleTV);

        editText.setTag(R.id.editText, tabFields.name);


        editText.setHint("Enter " + tabFields.name);
        titleTV.setText(tabFields.name);

      /*  if (tabFields.type.equalsIgnoreCase("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }*/
        cashFlowView.addView(view);

    }

    private ArrayList<TabFields> getCashFlowFields() {
        ArrayList<TabFields> fields = null;
        for (TabDto tabDto : workFlowTemplateDt.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CASH_FLOW)) {
                fields = tabDto.tabFieldsArrayList;
                break;
            }
        }
        return fields;
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void addView(View view) {
        cashFlowView.addView(view);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
    }

    @Override
    public void getCashFlowData(boolean status, String string, String templateDetailsId) {
        saveBTN.setVisibility(View.VISIBLE);
        if (status) {
            this.templateDetailsId = templateDetailsId;
            mapSavedDataToViews(string);
            if (!TextUtils.isEmpty(templateDetailsId)) {
                saveBTN.setText("Update");
            } else {
                saveBTN.setText("Save");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.saveBTN})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBTN:
                validateCashFlowAndSave();
                break;
        }
    }

    private void validateCashFlowAndSave() {
        try {
            boolean allFieldsValidated = true;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("workflowProfileID", workFlowTemplateDt.workFlowProfileId);
                jsonObject.put("Id", getCashFlowId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < cashFlowView.getChildCount(); i++) {
                View view = cashFlowView.getChildAt(i);
                EditText editText = view.findViewById(R.id.editText);
                try {
                    jsonObject.put(editText.getTag(R.id.editText).toString(), editText.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (TextUtils.isEmpty(editText.getText().toString())) {
                    allFieldsValidated = false;
                    editText.requestFocus();
                    Toast.makeText(getActivity(), "Please enter " + editText.getTag(R.id.editText), Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (allFieldsValidated) {
                if (!TextUtils.isEmpty(templateDetailsId)) {
                    cashFlowPresenter.updateCashFlowData(jsonObject.toString(), templateDetailsId);
                } else {
                    cashFlowPresenter.saveCashFlowData(jsonObject.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mapSavedDataToViews(String jsonObjectString) {

        Map<String, String> valuesHM = new Gson().fromJson(
                jsonObjectString, new TypeToken<HashMap<String, String>>() {
                }.getType()
        );

        if (cashFlowView.getChildCount() > 0) {
            for (int i = 0; i < cashFlowView.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) cashFlowView.getChildAt(i);
                if (linearLayout.getChildCount() >= 2) {
                    View view = linearLayout.getChildAt(1);
                    if (view instanceof EditText) {
                        EditText editText = (EditText) view;
                        String fieldName = (String) editText.getTag(R.id.editText);
                        if (valuesHM.containsKey(fieldName)) {
                            editText.setText(valuesHM.get(fieldName));
                        }

                    }
                }
            }
        }
    }
}
