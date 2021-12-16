package onboard;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.odedtech.mff.mffapp.R;
import dashboard.DashboardActivity;
import loans.model.SearchData;

import java.util.ArrayList;
import java.util.List;

public class SearchProfilesFragment extends DialogFragment implements SearchProfilesFragmentCallbacks, SearchProfilesAdapter.OnItemClickListener {
    private ProgressDialog progressDialog = null;
    private SearchProfilesPresenter searchProfilesPresenter;
    private RecyclerView rv_list, rv_list_2;
    private FrameLayout progressBar;
    private EditText EditBox;
    private TextView noClientsTV;
    private SearchProfilesAdapter searchProfilesAdapter;
    private AlertDialog alertDialogBuilder;
    private Activity mContext;
    private String branch_name = "";
    private String branch_id = "";

    public static SearchProfilesFragment newInstance(String title) {
        SearchProfilesFragment frag = new SearchProfilesFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        searchProfilesPresenter = new SearchProfilesPresenter(getActivity(), this);

        String title = getArguments().getString("title");
        alertDialogBuilder = new AlertDialog.Builder(getActivity()).create();
        final View customLayout = getActivity().getLayoutInflater().inflate(R.layout.search_profiles_list, null);
        alertDialogBuilder.setView(customLayout);
        TextView text_title = customLayout.findViewById(R.id.text_title);
        EditBox = customLayout.findViewById(R.id.EditBox);
        ImageView search_img = customLayout.findViewById(R.id.search_img);
        rv_list = customLayout.findViewById(R.id.List);
        rv_list_2 = customLayout.findViewById(R.id.rv_list_2);
        noClientsTV = customLayout.findViewById(R.id.noClientsTV);
        noClientsTV.setText("No Data Found");
        progressBar = customLayout.findViewById(R.id.progressBar);
        LinearLayout mainLayout = customLayout.findViewById(R.id.LinearLayout01);
        search_img.setOnClickListener(v -> {
            if (EditBox.getText().length() >= 4) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                searchProfiles(EditBox.getText().toString());
            } else {
                Toast.makeText(getActivity(), "you have to enter at least 4 digit!", Toast.LENGTH_SHORT).show();
            }
        });
        text_title.setText(title);
        return alertDialogBuilder;
    }

    private void searchProfiles(String search_text) {
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            searchProfilesPresenter.searchProfiles(search_text);
        } else {
            showMessage("Please check Internet connection");
        }
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLogoutAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(mContext);
    }

    @Override
    public void loadRecyclerView(List<SearchData> clients) {
        if (clients != null && clients.size() != 0) {
            noClientsTV.setVisibility(View.GONE);
            rv_list.setVisibility(View.VISIBLE);
            searchProfilesAdapter = new SearchProfilesAdapter(mContext, clients, this);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_list.getContext(), DividerItemDecoration.VERTICAL);
            rv_list.addItemDecoration(dividerItemDecoration);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            rv_list.setLayoutManager(linearLayoutManager);
            rv_list.setAdapter(searchProfilesAdapter);
        } else {
            noClientsTV.setVisibility(View.VISIBLE);
            rv_list.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClicked(SearchData clientDataDTO) {
        this.branch_name = clientDataDTO.branch_name;
        this.branch_id = clientDataDTO.branchid;
        searchProfilesPresenter.getProfileData(clientDataDTO.branchid);
    }

    @Override
    public void loadProfilesRecyclerView(ArrayList<ClientDataDTO> clients) {
        if (clients != null && clients.size() > 0) {
            if (alertDialogBuilder != null) {
                alertDialogBuilder.dismiss();
            }
            ((DashboardActivity) mContext).openSearchProfilesLisFragment(clients,branch_name,branch_id);
        } else {
            showMessage("No data available");
        }
    }

}
