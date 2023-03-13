package credit_bureau;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odedtech.mff.client.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import onboard.TabDto;
import onboard.TabFields;
import onboard.WorkFlowTemplateDto;

/**
 * Created by gufran khan on 20-10-2018.
 */

public class CreditBureauFragment extends Fragment implements ICreditBureauCallback {
    @BindView(R.id.mainCreditBureauLL)
    LinearLayout mainCreditBureauLL;

    private Unbinder unbinder;
    public WorkFlowTemplateDto workFlowTemplateDto = null;
    private ArrayList<TabFields> tabFieldsArrayList = null;
    private ArrayList<TabFields> tabBodyFieldsArrayList = null;
    CreditBureauPresenter creditBureauPresenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

   /* @BindView(R.id.scoreET)
    EditText scoreET;

    @BindView(R.id.cibilET)
    EditText cibilET;*/

    @BindView(R.id.saveBTN)
    Button saveBTN;
    private String templateDetailsId = "";

    public CreditBureauFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_bureau, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void addDynamicViews() {
        for (TabFields tabFields : tabBodyFieldsArrayList) {
            addFieldsToLayout(tabFields);
        }
    }

    private void addFieldsToLayout(TabFields tabFields) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.credit_bureau_view, null);
        TextView questionTV = view.findViewById(R.id.questionTV);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        if (tabFields.valuesList.size() > 0) {
            for (int i = 0; i < tabFields.valuesList.size(); i++) {
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setText(tabFields.valuesList.get(i));
                radioButton.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
                radioGroup.addView(radioButton);
            }
        } else if (!TextUtils.isEmpty(tabFields.value)) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(tabFields.value);
            radioButton.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
            radioButton.setChecked(true);
            radioGroup.addView(radioButton);
        }

        questionTV.setTag(R.id.questionTV, tabFields.name);
        questionTV.setText(tabFields.name);

        mainCreditBureauLL.addView(view);

    }

    private ArrayList<TabFields> getCreditBureuFields() {
        ArrayList<TabFields> fields = null;
        for (TabDto tabDto : workFlowTemplateDto.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CREDIT_BUREAU)) {
                fields = tabDto.tabFieldsBodyArrayList;
                break;
            }
        }
        return fields;
    }

    private String getCreditBureauId() {
        String id = "";
        for (TabDto tabDto : workFlowTemplateDto.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CREDIT_BUREAU)) {
                id = String.valueOf(tabDto.tabId);
            }
        }
        return id;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        creditBureauPresenter = new CreditBureauPresenter(getActivity(), this);
        workFlowTemplateDto = Constants.workFlowTemplateDt;
        tabFieldsArrayList = getCreditBureauFields();
        tabBodyFieldsArrayList = getCreditBureauBodyFields();
        if (tabBodyFieldsArrayList != null && tabBodyFieldsArrayList.size() > 0)
            addDynamicViews();

        String creditBureauId = getCreditBureauId();
        creditBureauPresenter.getCreditBureauData(creditBureauId, workFlowTemplateDto.workFlowProfileId);
    }

    private ArrayList<TabFields> getCreditBureauBodyFields() {
        ArrayList<TabFields> fields = null;
        for (TabDto tabDto : workFlowTemplateDto.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CREDIT_BUREAU)) {
                fields = tabDto.tabFieldsBodyArrayList;
                break;
            }
        }
        return fields;
    }

    private ArrayList<TabFields> getCreditBureauFields() {
        ArrayList<TabFields> fields = null;
        for (TabDto tabDto : workFlowTemplateDto.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CREDIT_BUREAU)) {
                fields = tabDto.tabFieldsArrayList;
                break;
            }
        }
        return fields;
    }

    private void saveBureauData() {
        try {
            boolean allFieldsValidated = true;
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("workflowProfileID", workFlowTemplateDto.workFlowProfileId);
                jsonObject.put("Id", getCreditBureauId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < mainCreditBureauLL.getChildCount(); i++) {
                View view = mainCreditBureauLL.getChildAt(i);
                RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
                TextView questionTV = view.findViewById(R.id.questionTV);

                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    allFieldsValidated = false;
                    Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioBTN = view.findViewById(selectedId);
                    try {
                        jsonObject.put(questionTV.getTag(R.id.questionTV).toString(), selectedRadioBTN.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (allFieldsValidated) {
                if (!TextUtils.isEmpty(templateDetailsId)) {
                    creditBureauPresenter.updateCreditBureauData(jsonObject.toString(), templateDetailsId);
                } else {
                    creditBureauPresenter.saveCreditBureauData(jsonObject.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLogoutAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
    }

    @Override
    public void showAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
    }

    @Override
    public void getCreditBureauData(boolean status, String string, String templateDetailsId) {
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

    @OnClick({R.id.saveBTN})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBTN:
                saveBureauData();
                break;
        }
    }

    private void mapSavedDataToViews(String jsonObjectString) {

        Map<String, String> valuesHM = new Gson().fromJson(
                jsonObjectString, new TypeToken<HashMap<String, String>>() {
                }.getType()
        );

        if (mainCreditBureauLL.getChildCount() > 0) {
            for (int i = 0; i < mainCreditBureauLL.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) mainCreditBureauLL.getChildAt(i);
                if (linearLayout.getChildCount() >= 2) {
                    RadioGroup radioGroup = (RadioGroup) linearLayout.getChildAt(1);
                    if (radioGroup.getChildCount() > 0) {
                        for (int j = 0; j < radioGroup.getChildCount(); j++) {
                            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(j);
                            if (radioButton != null) {
                             /*   if (valuesHM.containsKey(radioButton.getText().toString())) {
                                    radioButton.setChecked(true);
                                }*/
                                for (String question : valuesHM.keySet()) {
                                    if (valuesHM.get(question).equalsIgnoreCase(radioButton.getText().toString())) {
                                        radioButton.setChecked(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
