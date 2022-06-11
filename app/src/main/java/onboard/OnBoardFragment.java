package onboard;


import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.mffapp.BuildConfig;
import com.odedtech.mff.mffapp.R;
import com.odedtech.mff.mffapp.databinding.FragmentOnBoardBinding;

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


public class OnBoardFragment extends BaseFragment implements IOnBoardFragmentCallback,
        View.OnClickListener, MaterialSearchView.OnQueryTextListener,
        MaterialSearchView.SearchViewListener {

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
    /*@BindView(R.id.iv_search)
    ImageView iv_search;*/
    @BindView(R.id.search_profile)
    SearchView searchView;
    @BindView(R.id.item1)
    TextView item1;
    @BindView(R.id.item2)
    TextView item2;
    @BindView(R.id.item3)
    TextView item3;
    @BindView(R.id.item4)
    TextView item4;
    @BindView(R.id.select)
    TextView select;
    @BindView(R.id.tabs_profile)
    FrameLayout tabs;


    private Unbinder unbinder;
    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private OnBoardPresenter onBoardPresenter;
    private ProgressDialog progressDialog = null;
    private OnBoardAdapter onBoardAdapter;
    private View view;
    ColorStateList def;
    boolean isLoading = false;
    private int pageIndex = 0;
    private String queryText;
    private int loansPageIndex;
    private int search_selection = 1;
    private int SearchClientPageIndex;
    private FragmentOnBoardBinding binding;
    private DataProfilesDTO dataProfilesDTO;

    public OnBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOnBoardBinding.inflate(inflater, container, false);
        unbinder = ButterKnife.bind(this, binding.getRoot());
        initViews();

        return binding.getRoot();
    }

    private void initViews() {
        /*item1 = view.findViewById(R.id.item1);
        item2 = view.findViewById(R.id.item2);
        item3 = view.findViewById(R.id.item3);
        item4 = view.findViewById(R.id.item4);
        tabs = view.findViewById(R.id.tabs_profile);*/

        def = binding.item2.getTextColors();
        binding.item1.setOnClickListener(this);
        binding.item2.setOnClickListener(this);
        binding.item3.setOnClickListener(this);
        binding.item4.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.ONBOARD_FRAGMENT, "Profile");
        ((DashboardActivity) getActivity()).setToolBarBackVisible(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
//        iOnFragmentChangeListener.onHeaderUpdate(Constants.ONBOARD_FRAGMENT, getString(R.string.profiles));
        onBoardPresenter = new OnBoardPresenter(getActivity(), this);
        if (Constants.FLAVOR_CLIENT.equalsIgnoreCase(BuildConfig.FLAVOR)) {
            textAddClient.setText("Add Application");
        } else {
            textAddClient.setText("Add client");
        }
        if (UtilityMethods.isNetworkAvailable(requireActivity())) {
            onBoardPresenter.getAllClients(pageIndex, true, false, 0, "");
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

    @OnClick({R.id.addClientLL, R.id.text_clears, R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.search_profile})
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
            /*case R.id.iv_search:
                searchDialog();
                break;*/
            case R.id.search_profile:
                //    searchView.showSearch(true);
                System.out.println("click is working");
                break;
        }
        if (view.getId() == R.id.item1) {
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            item3.setTextColor(def);
            item4.setTextColor(def);
            search_selection = 1;
        } else if (view.getId() == R.id.item2) {
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            item3.setTextColor(def);
            item4.setTextColor(def);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            search_selection = 2;
        } else if (view.getId() == R.id.item3) {
            item1.setTextColor(def);
            item2.setTextColor(def);
            item3.setTextColor(Color.WHITE);
            item4.setTextColor(def);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);
            search_selection = 3;
        } else if (view.getId() == R.id.item4) {
            item1.setTextColor(def);
            item2.setTextColor(def);
            item3.setTextColor(def);
            item4.setTextColor(Color.WHITE);
            int size = item2.getWidth() * 3;
            select.animate().x(size).setDuration(100);
            search_selection = 4;
        }
    }

    private void getSearchClient(String search) {
        if (SearchClientPageIndex == 0) {
            showLoading();
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (search_selection == 1) {
            onBoardPresenter.getAllClients(0, true, true, search_selection, search);
        } else if (search_selection == 2) {
            onBoardPresenter.getAllClients(0, true, true, search_selection, search);

        } else if (search_selection == 3) {
            onBoardPresenter.getAllClients(0, true, true, search_selection, search);

        } else if (search_selection == 4) {
            onBoardPresenter.getAllClients(0, true, true, search_selection, search);

        }

    }

    private void profileSearchDisbursals(ProfileDetailsDTO response) {

        isLoading = false;
        progressBar.setVisibility(View.GONE);

       /* if (response == null && response.size() == 0) {
            binding.textNoDataLoans.setVisibility(View.VISIBLE);
            binding.rvLoans.setVisibility(View.GONE);
            return;
        } else {
            binding.textNoDataLoans.setVisibility(View.GONE);
            binding.rvLoans.setVisibility(View.VISIBLE);
            if (response.data.portfolio.size() > 0) {
                disbursalsAdapter.setData(response, SearchClientPageIndex == 0);
            } else {
                Toast.makeText(getActivity(), "No search Data", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.queryText = query;
        getSearchClient(query);
        // onBoardPresenter.getSearch(query);
        if (TextUtils.isEmpty(query)) {
            isLoading = false;
            pageIndex = 0;
            SearchClientPageIndex = 0;
            loansPageIndex = 0;
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.queryText = newText;
        tabs.setVisibility(View.VISIBLE);
        //  getSearchClient(newText);
       /* if (TextUtils.isEmpty(newText)) {
            isLoading = false;
            pageIndex = 0;
            SearchClientPageIndex = 0;
            loansPageIndex = 0;
        }*/
        return false;
    }

    private void searchDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        SearchProfilesFragment alertDialog = SearchProfilesFragment.newInstance("Profiles");
        //   alertDialog.show(fm, "Profiles");
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
            if (UtilityMethods.isNetworkAvailable(requireActivity())) {
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
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
                        onBoardPresenter.getAllClients(pageIndex, false, false, 0, "");
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
        onBoardPresenter.getAllClients(0, true, false, 0, "");
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
    public void onSearchViewShown() {
        Log.e("msg", "search open");
        binding.tabsProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchViewClosed() {
        Log.e("msg", "search close");
        binding.tabsProfile.setVisibility(View.GONE);

    }


 /*   @Override
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
    }*/

}
