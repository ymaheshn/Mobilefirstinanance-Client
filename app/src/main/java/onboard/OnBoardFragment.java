package onboard;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.mffapp.BuildConfig;
import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;

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

    private static final int PERMISSION_REQUEST_CODE = 100;

    @BindView(R.id.addClientLL)
    RelativeLayout addClientLL;
    @BindView(R.id.text_add_client)
    TextView textAddClient;
    @BindView(R.id.clientsRV)
    RecyclerView clientsRV;
    @BindView(R.id.noClientsTV)
    TextView noClientsTV;
    @BindView(R.id.text_clears)
    TextView textClear;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Unbinder unbinder;
    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private OnBoardPresenter onBoardPresenter;
    private ProgressDialog progressDialog = null;
    private OnBoardAdapter onBoardAdapter;


    boolean isLoading = false;
    private int pageIndex = 0;

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

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.ONBOARD_FRAGMENT, getString(R.string.profiles));
        onBoardPresenter = new OnBoardPresenter(getActivity(), this);
        if (Constants.FLAVOR_CLIENT.equalsIgnoreCase(BuildConfig.FLAVOR)) {
            textAddClient.setText("Add Application");
        } else {
            textAddClient.setText("Add client");
        }
        ((DashboardActivity) getActivity()).setToolBarBackVisible(false);
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            onBoardPresenter.getAllClients(pageIndex, true);
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

    @OnClick({R.id.addClientLL, R.id.text_clears})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addClientLL:
                Constants.clientDataDTO = null;
                Constants.isFromAddClient = true;
                iOnFragmentChangeListener.onFragmentChanged(Constants.ADD_CLIENT_FRAGMENT, null);
                break;
            case R.id.text_clears:
                textClear.setVisibility(View.GONE);
                ((DashboardActivity) getActivity()).clearSearch();
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
    public void loadRecyclerView(ArrayList<ClientDataDTO> clients, boolean isFromSearch, boolean clearAll) {
        if (clients != null && clients.size() > 0) {
            if (!isVisible() || clientsRV == null) {
                return;
            }
            clientsRV.setVisibility(View.VISIBLE);
            noClientsTV.setVisibility(View.GONE);
            if (pageIndex == 0) {
                if (onBoardAdapter != null) {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    onBoardAdapter.setData(clients, clearAll);
                    textClear.setVisibility(clearAll ? View.VISIBLE : View.GONE);
                } else {
                    setAdapter(clients);
                }
            } else {
                textClear.setVisibility(clearAll ? View.VISIBLE : View.GONE);
                onBoardAdapter.setData(clients, clearAll);
                if (clearAll) {
                    isLoading = true;
                    progressBar.setVisibility(View.GONE);
                } else {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                }
            }
        } else {
            if (pageIndex == 0) {
                noClientsTV.setVisibility(View.VISIBLE);
                clientsRV.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    public void setAdapter(ArrayList<ClientDataDTO> clients) {
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

        /**
         * commented for now,but it should be fixed.
         */
        clientsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.i("isLoading", "isLoading :" + isLoading);
                if (!isLoading && onBoardAdapter.getItemCount() >= 10) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == onBoardAdapter.getItemCount() - 1) {
                        //bottom of list!
                        pageIndex++;
                        Log.i("pageIndex", "pageIndex :" + pageIndex);
                        onBoardPresenter.getAllClients(pageIndex, false);
                        isLoading = true;
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        clientsRV.post(() -> {
            int previousPosition = PreferenceConnector.readInteger(getContext(), "ListPosition", -1);
            if (previousPosition >= 0) {
                clientsRV.scrollToPosition(previousPosition);
            }
        });
    }


    public void getAllClients() {
        isLoading = false;
        onBoardPresenter.getAllClients(0, true);
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
        if (TextUtils.isEmpty(query)) {
            textClear.setVisibility(View.GONE);
            isLoading = false;
            pageIndex = 0;
            onBoardPresenter.getAllClients(0, true);
        } else {
            onBoardPresenter.getSearch(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        onBoardAdapter.getFilter().filter(newText);
        if (TextUtils.isEmpty(newText)) {
            textClear.setVisibility(View.GONE);
            isLoading = false;
            onBoardPresenter.getAllClients(0, true);
        }
        return false;
    }

    @Override
    public void onSearchViewShown() {

    }

    @Override
    public void onSearchViewClosed() {
    }
}
