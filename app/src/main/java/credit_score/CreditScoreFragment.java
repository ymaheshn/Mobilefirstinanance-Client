package credit_score;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class CreditScoreFragment extends Fragment implements ICreditScoreCallback {
    private Unbinder unbinder;
    public WorkFlowTemplateDto workFlowTemplateDto = null;
    private ArrayList<TabFields> tabFieldsArrayList = null;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.scoreET)
    EditText scoreET;

    @BindView(R.id.cibilET)
    EditText cibilET;

    @BindView(R.id.saveBTN)
    Button saveBTN;
    private String templateDetailsId = "";

    public WorkFlowTemplateDto workFlowTemplateDt = null;
    private CreditScorePresenter creditScorePresenter;

    public CreditScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit_score, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        workFlowTemplateDto = Constants.workFlowTemplateDt;
        creditScorePresenter = new CreditScorePresenter(getActivity(), this);
        String creditScoreId = getCrediScoreId();
        creditScorePresenter.getCreditScoreData(creditScoreId, workFlowTemplateDto.workFlowProfileId);
    }

    private String getCrediScoreId() {
        String id = "";
        for (TabDto tabDto : workFlowTemplateDto.tabDtoArrayList) {
            if (tabDto.tabName.equalsIgnoreCase(Constants.CREDIT_SCORE)) {
                id = String.valueOf(tabDto.tabId);
            }
        }
        return id;
    }


    @OnClick({R.id.saveBTN})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveBTN:
                saveScoreData();
                break;
        }
    }

    private void saveScoreData() {
        if (TextUtils.isEmpty(scoreET.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter Credit Rating Score", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cibilET.getText().toString())) {
            Toast.makeText(getActivity(), "Please enter Credit Rating Status", Toast.LENGTH_SHORT).show();
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("workflowProfileID", workFlowTemplateDto.workFlowProfileId);
                jsonObject.put("Id", getCrediScoreId());
                jsonObject.put("Credit Rating Score",scoreET.getText().toString());
                jsonObject.put("Credit Rating Status", cibilET.getText().toString());
                if (!TextUtils.isEmpty(templateDetailsId)) {
                    creditScorePresenter.updateCreditScoreData(jsonObject.toString(), templateDetailsId);
                } else {
                    creditScorePresenter.saveCreditScoreData(jsonObject.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    public void addView(View view) {

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
    public void getCreditScoreData(boolean status, String string, String templateDetailsId) {
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

    private void mapSavedDataToViews(String jsonObjectString) {

        Map<String, String> valuesHM = new Gson().fromJson(
                jsonObjectString, new TypeToken<HashMap<String, String>>() {
                }.getType()
        );

        if (valuesHM.containsKey("Credit Rating Score")) {
            scoreET.setText(valuesHM.get("Credit Rating Score"));
        }

        if (valuesHM.containsKey("Credit Rating Status")) {
            cibilET.setText(valuesHM.get("Credit Rating Status"));
        }

    }
}
