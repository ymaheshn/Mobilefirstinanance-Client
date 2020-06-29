package onboard;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.mffapp.BuildConfig;
import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.Collections;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import addclient.AddClientFragment;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dashboard.DashboardActivity;
import interfaces.IOnFragmentChangeListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnBoardFragment extends BaseFragment implements IOnBoardFragmentCallback,
        MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {

    @BindView(R.id.addClientLL)
    LinearLayout addClientLL;
    @BindView(R.id.text_add_client)
    TextView textAddClient;
    @BindView(R.id.clientsRV)
    RecyclerView clientsRV;
    @BindView(R.id.noClientsTV)
    TextView noClientsTV;
    private Unbinder unbinder;
    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private OnBoardPresenter onBoardPresenter;
    private ProgressDialog progressDialog = null;
    private OnBoardAdapter onBoardAdapter;

    public OnBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_board, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.ONBOARD_FRAGMENT, getString(R.string.onboard));
        onBoardPresenter = new OnBoardPresenter(getActivity(), this);
        if(Constants.FLAVOR_CLIENT.equalsIgnoreCase(BuildConfig.FLAVOR)) {
            textAddClient.setText("Add Application");
        } else {
            textAddClient.setText("Add client");
        }
        ((DashboardActivity) getActivity()).setToolBarBackVisible(false);
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            onBoardPresenter.getAllClients();
        } else {
            Toast.makeText(getActivity(), "Please check your internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
    }

    @OnClick({R.id.addClientLL})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addClientLL:
                Constants.clientDataDTO = null;
                Constants.isFromAddClient = true;
                iOnFragmentChangeListener.onFragmentChanged(Constants.ADD_CLIENT_FRAGMENT, null);
                break;
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLogoutAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(getActivity());
    }


    @Override
    public void loadRecyclerView(ArrayList<ClientDataDTO> clients) {
        if (clients != null && clients.size() > 0) {
            if (!isVisible() || clientsRV == null) {
                return;
            }
            Collections.reverse(clients);
            clientsRV.setVisibility(View.VISIBLE);
            noClientsTV.setVisibility(View.GONE);
            onBoardAdapter = new OnBoardAdapter(getActivity(), clients, clientDataDTO -> {
                if (UtilityMethods.isNetworkAvailable(getActivity())) {
                    Constants.clientDataDTO = clientDataDTO;
                    Constants.isFromAddClient = false;
                    onBoardPresenter.getIsLinkedStatusAPI(clientDataDTO.profileId);
                } else {
                    showMessage("Please check your internet");
                }
            }, noData -> {
                noClientsTV.setVisibility(noData ? View.VISIBLE : View.GONE);
                clientsRV.setVisibility(noData ? View.GONE : View.VISIBLE);
            });
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(clientsRV.getContext(), DividerItemDecoration.VERTICAL);
            clientsRV.addItemDecoration(dividerItemDecoration);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            clientsRV.setLayoutManager(linearLayoutManager);
            clientsRV.setAdapter(onBoardAdapter);

            clientsRV.post(() -> {
                int previousPosition = PreferenceConnector.readInteger(getContext(), "ListPosition", -1);
                if (previousPosition >= 0) {
                    clientsRV.scrollToPosition(previousPosition);
                }
            });

        } else {
            noClientsTV.setVisibility(View.VISIBLE);
            clientsRV.setVisibility(View.GONE);
        }
    }


    @Override
    public void profileLinkStatusFromApi(boolean status, WorkFlowTemplateDto workFlowTemplateDto) {
        if (status) {
            if (workFlowTemplateDto != null) {
                Constants.workFlowTemplateDt = workFlowTemplateDto;
                Bundle bundle = new Bundle();
                bundle.putInt(AddClientFragment.KEY_FORM_ID, Constants.clientDataDTO.formId);
                iOnFragmentChangeListener.onFragmentChanged(Constants.ADD_CLIENT_FRAGMENT, bundle);
            }
        } else {
            Constants.isFromAddClient = true;
            Bundle bundle = new Bundle();
            bundle.putInt(AddClientFragment.KEY_FORM_ID, Constants.clientDataDTO.formId);
            iOnFragmentChangeListener.onFragmentChanged(Constants.ADD_CLIENT_FRAGMENT, bundle);
            // AlertDialogUtils.getAlertDialogUtils().showOkAlert(getActivity(), "Your account is not linked");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onBoardAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        onBoardAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {
    }
}
