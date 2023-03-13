package signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.odedtech.mff.client.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import Utilities.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import networking.WebService;
import signup.adapter.BranchesAdapter;
import signup.listener.SelectedBranchInterface;
import signup.model.BranchName;
import signup.model.Branches;

public class BranchSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SelectedBranchInterface, View.OnClickListener {

    @BindView(R.id.recycler_view_branch_list)
    RecyclerView recyclerView;

    @BindView(R.id.branch_search_view)
    SearchView searchView;

    @BindView(R.id.back_icon_button)
    AppCompatImageButton backButton;

    public ArrayList<String> branchNameList = new ArrayList<>();
    public ArrayList<Integer> currentBranchIdList = new ArrayList<>();
    public ArrayList<String> branchLevelList = new ArrayList<>();
    private BranchesAdapter branchesAdapter;
    public Branches branches = new Branches();
    public ArrayList<BranchName> mBranchesName = new ArrayList<>();
    public int position;
    public int branchId;
    public String branchName;
    ArrayList<Branches> branchesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search);
        ButterKnife.bind(BranchSearchActivity.this);

        getBranchesApi();

        searchView.setOnQueryTextListener(this);
        backButton.setOnClickListener(this);
    }

    private void getBranchesApi() {
        ProgressBar.showProgressDialog(this);
        String url = "https://dev.mobilefirstfinance.com:7190/mff/api/getBranchByBranchLevel/L1";
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        ProgressBar.dismissDialog();
                        if (!TextUtils.isEmpty(object)) {
                            try {
                                JSONArray jsonArray = new JSONArray(object);
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        Branches branches1 = new Branches(
                                                jsonObject.getString("branch_name"),
                                                jsonObject.getInt("current_branch_id"),
                                                jsonObject.getString("branch_level")
                                        );
                                        branchesList.add(branches1);

                                        branchNameList.add(jsonObject.getString("branch_name"));
                                        currentBranchIdList.add(jsonObject.getInt("current_branch_id"));
                                        branchLevelList.add(jsonObject.getString("branch_level"));
                                    }
                                    branches.setBranchNameList(branchNameList);
                                    branches.setCurrentBranchIdList(currentBranchIdList);
                                    branches.setBranchLevelList(branchLevelList);
                                    mBranchesName.addAll(branches.getBranchNameArrayList());
                                    setAdapterData();
                                }
                            } catch (JSONException e) {
                                ProgressBar.dismissDialog();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        } else {
                            ProgressBar.dismissDialog();
                        }
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        ProgressBar.dismissDialog();
                        Toast.makeText(BranchSearchActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setAdapterData() {
        branchesAdapter = new BranchesAdapter(getApplicationContext(), branches, mBranchesName, this, branchesList);
        recyclerView.setAdapter(branchesAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() > 1) {
            filter(newText);
        }
        return false;
    }

    private void filter(String text) {
        ArrayList<Branches> filteredlist = new ArrayList<>();
        for (Branches item : branchesList) {
            if (item.getBranchName().toLowerCase().equals(text)) {
                filteredlist.addAll(Collections.singleton(item));
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
            branchesAdapter = new BranchesAdapter(getApplicationContext(), branches, mBranchesName, this, branchesList);
        } else {
            branchesAdapter = new BranchesAdapter(getApplicationContext(), branches, mBranchesName, this, filteredlist);
        }
        recyclerView.setAdapter(branchesAdapter);
    }


    @Override
    public void selectedBranch(Integer position, Branches branches) {
        this.position = position;
        branchId = branches.getCurrentBranchId();
        this.branchName = branches.getBranchName();
        Toast.makeText(getApplicationContext(), branchId + branchName, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), SignUpFormActivity.class);
        intent.putExtra("branchName", branchName);
        intent.putExtra("branchId", branchId);
        // onBranchInfoListener.onBranchInfoListener(branchName);
        setResult(11, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(11);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_icon_button) {
            onBackPressed();
        }
    }
}
