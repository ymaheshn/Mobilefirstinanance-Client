package search_profiles_list;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import addclient.AddClientFragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.odedtech.mff.mffapp.R;
import dashboard.DashboardActivity;
import interfaces.IOnFragmentChangeListener;
import onboard.*;

import java.util.ArrayList;

public class SearchProfilesListFragment extends BaseFragment implements IOnBoardFragmentCallback {

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
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    private Unbinder unbinder;
    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private SearchProfilesListPresenter searchProfilesListPresenter;
    private ProgressDialog progressDialog = null;
    private SearchProfileListAdapter searchProfileListAdapter;

    private String branch_name = "";
    private String branch_id = "";

    boolean isLoading = false;
    private int pageIndex = 0;

    public SearchProfilesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            branch_name = bundle.getString("branch_name", "");
            branch_id = bundle.getString("branch_id", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profiles_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        tv_title.setText(branch_name);
        searchProfilesListPresenter = new SearchProfilesListPresenter(getActivity(), this);

        ((DashboardActivity) getActivity()).setToolBarBackVisible(false);
        if (UtilityMethods.isNetworkAvailable(getActivity())) {
            searchProfilesListPresenter.getAllClients(branch_id, pageIndex, true);
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

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
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
                if (searchProfileListAdapter != null) {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                    searchProfileListAdapter.setData(clients, clearAll);
                    textClear.setVisibility(clearAll ? View.VISIBLE : View.GONE);
                } else {
                    setAdapter(clients);
                }
            } else {
                textClear.setVisibility(clearAll ? View.VISIBLE : View.GONE);
                searchProfileListAdapter.setData(clients, clearAll);
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
        searchProfileListAdapter = new SearchProfileListAdapter(getActivity(), clients, clientDataDTO -> {
            if (UtilityMethods.isNetworkAvailable(getActivity())) {
                Constants.clientDataDTO = clientDataDTO;
                Constants.isFromAddClient = false;
                searchProfilesListPresenter.getIsLinkedStatusAPI(clientDataDTO.profileId);
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
        clientsRV.setAdapter(searchProfileListAdapter);

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
                if (!isLoading && searchProfileListAdapter.getItemCount() >= 10) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchProfileListAdapter.getItemCount() - 1) {
                        //bottom of list!
                        pageIndex++;
                        Log.i("pageIndex", "pageIndex :" + pageIndex);
                        searchProfilesListPresenter.getAllClients(branch_id, pageIndex, false);
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

}
