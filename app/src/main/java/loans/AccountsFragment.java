package loans;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.client.R;
import com.odedtech.mff.client.databinding.FragmentLoansNewBinding;

import java.util.ArrayList;

import Utilities.Constants;
import Utilities.PreferenceConnector;
import Utilities.ProgressBar;
import base.BaseFragment;
import dashboard.DashboardActivity;
import interfaces.IOnFragmentChangeListener;
import loans.model.LoansPortfolioResponse;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountsFragment extends BaseFragment implements MaterialSearchView.OnQueryTextListener,
        MaterialSearchView.SearchViewListener, DisbursalsAdapter.ItemViewClickListener, View.OnClickListener {

    private FragmentLoansNewBinding binding;
    private IOnFragmentChangeListener iOnFragmentChangeListener;

    private boolean isLoading = false;
    private int pageIndex;
    private int searchPageIndex;
    private int loansPageIndex;
    private int loansSearchPageIndex;
    private DisbursalsAdapter disbursalsAdapter;
    private String queryText;
    private boolean isCollections = true;
    ColorStateList def;
    private int search_selection = 1;

    private int colorCode;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoansNewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        // getCollectionPortfolio();
        getLoans();
        ((DashboardActivity) requireActivity()).setToolBarBackVisible(false);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) requireActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.LOANS_FRAGMENT, "Accounts");



        setPagingListeners();
    }

    private void initViews() {
        /*binding.radioContracts.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_collections) {
                binding.containerDisbursals.setVisibility(View.GONE);
                binding.containerCollections.setVisibility(View.VISIBLE);
                isCollections = true;
            } else if (i == R.id.radio_disbursals) {
                binding.containerDisbursals.setVisibility(View.VISIBLE);
                binding.containerCollections.setVisibility(View.GONE);
                isCollections = false;
            }
        });*/
        def = binding.item2.getTextColors();
        binding.item1.setOnClickListener(this);
        binding.item2.setOnClickListener(this);
        binding.item3.setOnClickListener(this);
        binding.item4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1) {
            binding.select.animate().x(0).setDuration(100);
            binding.item1.setTextColor(Color.WHITE);
            binding.item2.setTextColor(def);
            binding.item3.setTextColor(def);
            binding.item4.setTextColor(def);
            search_selection = 1;
        } else if (view.getId() == R.id.item2) {
            binding.item1.setTextColor(def);
            binding.item2.setTextColor(Color.WHITE);
            binding.item3.setTextColor(def);
            binding.item4.setTextColor(def);
            int size = binding.item2.getWidth();
            binding.select.animate().x(size).setDuration(100);
            search_selection = 2;
        } else if (view.getId() == R.id.item3) {
            binding.item1.setTextColor(def);
            binding.item2.setTextColor(def);
            binding.item3.setTextColor(Color.WHITE);
            binding.item4.setTextColor(def);
            int size = binding.item2.getWidth() * 2;
            binding.select.animate().x(size).setDuration(100);
            search_selection = 3;
        } else if (view.getId() == R.id.item4) {
            binding.item1.setTextColor(def);
            binding.item2.setTextColor(def);
            binding.item3.setTextColor(def);
            binding.item4.setTextColor(Color.WHITE);
            int size = binding.item2.getWidth() * 3;
            binding.select.animate().x(size).setDuration(100);
            search_selection = 4;
        }
    }

    private void setPagingListeners() {
        /*binding.rvCollections.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.i("isLoading", "isLoading :" + isLoading);
                if (!isLoading && adapter.getItemCount() >= 10) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() ==
                                    adapter.getItemCount() - 1) {
                        //bottom of list!
                        Log.i("pageIndex", "pageIndex :" + pageIndex);
                        isLoading = true;
                        binding.progressBar.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(queryText)) {
                            pageIndex++;
                            getCollectionPortfolio();
                        } else {
                            searchPageIndex++;
                            getSearchCollection(queryText);
                        }

                    }
                }
            }
        });*/

        binding.rvLoans.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.i("isLoading", "isLoading :" + isLoading);
                if (!isLoading && disbursalsAdapter.getItemCount() >= 10) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastCompletelyVisibleItemPosition() ==
                                    disbursalsAdapter.getItemCount() - 1) {
                        //bottom of list!
                        Log.i("loansPageIndex", "loansPageIndex :" + loansPageIndex);
                        isLoading = true;
                        binding.progressBar.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(queryText)) {
                            loansPageIndex++;
                            getLoans();
                        } else {
                            loansSearchPageIndex++;
                            getSearchLoans(queryText);
                        }

                    }
                }
            }
        });
    }


   /* public void getCollectionPortfolio() {
        if (pageIndex == 0) {
            showLoading();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        String accessToken = PreferenceConnector.readString(getActivity(),
                getActivity().getString(R.String.accessToken), "");
        MFFApiWrapper.getInstance().service.getCollections(accessToken,
                pageIndex, 10).enqueue(new Callback<CollectionPortfolioResponse>() {
            @Override
            public void onResponse(@NonNull Call<CollectionPortfolioResponse> call,
                                   @NonNull Response<CollectionPortfolioResponse> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    loadCollectionPortfolio(response.body());
                } else {
                    checkResponseError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectionPortfolioResponse> call, @NonNull Throwable t) {
                dismissLoading();
                Toast.makeText(getActivity(),
                        getString(R.String.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    public void getLoans() {
        if (loansPageIndex == 0) {
            showLoading();
            ProgressBar.showProgressDialog(requireContext());
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        String accessToken = PreferenceConnector.readString(getActivity(),
                requireActivity().getString(R.string.accessToken), "");
        showLoading();
        ProgressBar.showProgressDialog(requireContext());
        MFFApiWrapper.getInstance().service.getLoans(accessToken,
                loansPageIndex, 10, "IED").enqueue(new Callback<LoansPortfolioResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoansPortfolioResponse> call,
                                   @NonNull Response<LoansPortfolioResponse> response) {
                dismissLoading();
                ProgressBar.dismissDialog();
                if (response.isSuccessful()) {
                    loadCollectionDisbursals(response.body());
                } else {
                    checkResponseError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoansPortfolioResponse> call, @NonNull Throwable t) {
                dismissLoading();
                ProgressBar.dismissDialog();
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSearchLoans(String search) {
        if (loansSearchPageIndex == 0) {
            showLoading();
            ProgressBar.showProgressDialog(requireContext());
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        String accessToken = PreferenceConnector.readString(getActivity(),
                requireActivity().getString(R.string.accessToken), "");
        if (search_selection == 1) {
            MFFApiWrapper.getInstance().service.searchLoansUsingName(accessToken,
                    search, loansSearchPageIndex, 10, "IED").enqueue(new Callback<LoansPortfolioResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoansPortfolioResponse> call,
                                       @NonNull Response<LoansPortfolioResponse> response) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    if (response.isSuccessful()) {
                        loadSearchDisbursals(response.body());
                    } else {
                        checkResponseError(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoansPortfolioResponse> call, @NonNull Throwable t) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (search_selection == 2) {
            MFFApiWrapper.getInstance().service.searchLoansUsingHierarchy(accessToken,
                    search, loansSearchPageIndex, 10, "IED").enqueue(new Callback<LoansPortfolioResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoansPortfolioResponse> call,
                                       @NonNull Response<LoansPortfolioResponse> response) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    if (response.isSuccessful()) {
                        loadSearchDisbursals(response.body());
                    } else {
                        checkResponseError(response);
                    }
                }

                @Override
                public void onFailure(Call<LoansPortfolioResponse> call, Throwable t) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (search_selection == 3) {
            MFFApiWrapper.getInstance().service.searchLoansUsingNationalId(accessToken,
                    search, loansSearchPageIndex, 10, "IED").enqueue(new Callback<LoansPortfolioResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoansPortfolioResponse> call,
                                       @NonNull Response<LoansPortfolioResponse> response) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    if (response.isSuccessful()) {
                        loadSearchDisbursals(response.body());
                    } else {
                        checkResponseError(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoansPortfolioResponse> call, @NonNull Throwable t) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (search_selection == 4) {
            MFFApiWrapper.getInstance().service.searchLoansUsingIdentifier(accessToken,
                    search, loansSearchPageIndex, 10, "IED").enqueue(new Callback<LoansPortfolioResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoansPortfolioResponse> call,
                                       @NonNull Response<LoansPortfolioResponse> response) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    if (response.isSuccessful()) {
                        loadSearchDisbursals(response.body());
                    } else {
                        checkResponseError(response);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoansPortfolioResponse> call, @NonNull Throwable t) {
                    dismissLoading();
                    ProgressBar.dismissDialog();
                    Toast.makeText(getActivity(),
                            getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

/*    private void loadSearchedCollectionPortfolio(CollectionPortfolioResponse response) {
        binding.containerCollections.setVisibility(View.VISIBLE);
        binding.containerDisbursals.setVisibility(View.GONE);
        isLoading = false;
        binding.progressBar.setVisibility(View.GONE);
        if (response.data.portfolio == null && response.data.portfolio.size() == 0) {
            binding.textNoDataCollections.setVisibility(View.VISIBLE);
            binding.rvCollections.setVisibility(View.GONE);
            return;
        } else {
            binding.textNoDataCollections.setVisibility(View.GONE);
            binding.rvCollections.setVisibility(View.VISIBLE);
        }

        adapter.setData(response.data.portfolio, searchPageIndex == 0);
    }*/

   /* private void loadCollectionPortfolio(CollectionPortfolioResponse response) {
        // binding.containerCollections.setVisibility(View.VISIBLE);
        //binding.containerDisbursals.setVisibility(View.GONE);
        if (pageIndex == 0) {
            if (response.data.portfolio == null && response.data.portfolio.size() == 0) {
                binding.textNoDataCollections.setVisibility(View.VISIBLE);
                binding.rvCollections.setVisibility(View.GONE);
                return;
            } else {
                binding.textNoDataCollections.setVisibility(View.GONE);
                binding.rvCollections.setVisibility(View.VISIBLE);
            }
            adapter = new LoanCollectionPortfolioAdapter((items, collectionPortfolio) -> {
                Intent intent = new Intent(getActivity(), LoanCollectionActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra(Constants.KeyExtras.CONTRACT_ID, collectionPortfolio.contractUUID);
                intent.putExtra(Constants.KeyExtras.LINKED_PROFILE, collectionPortfolio);
                startActivity(intent);
               *//* Bundle bundle = new Bundle();
                bundle.putString("type", "1");
                bundle.putString(Constants.KeyExtras.CONTRACT_ID, collectionPortfolio.contractUUID);
                bundle.putParcelable(Constants.KeyExtras.LINKED_PROFILE, collectionPortfolio);
                iOnFragmentChangeListener.onFragmentChanged(Constants.LOAN_COLLECTIONS_FRAGMENT, bundle);*//*
            });

            adapter.setData(response.data.portfolio, true);
            binding.rvCollections.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            binding.rvCollections.setAdapter(adapter);
        } else {
            isLoading = false;
            binding.progressBar.setVisibility(View.GONE);
            if (response.data.portfolio == null && response.data.portfolio.size() == 0) {
                binding.textNoDataCollections.setVisibility(View.VISIBLE);
                binding.rvCollections.setVisibility(View.GONE);
                return;
            } else {
                binding.textNoDataCollections.setVisibility(View.GONE);
                binding.rvCollections.setVisibility(View.VISIBLE);
            }
            adapter.setData(response.data.portfolio, false);
        }
    }*/

    private void loadCollectionDisbursals(LoansPortfolioResponse response) {
        //  binding.containerCollections.setVisibility(View.GONE);
        // binding.containerDisbursals.setVisibility(View.VISIBLE);
        if (loansPageIndex == 0) {
            if (response.data.portfolio == null && response.data.portfolio.size() == 0) {
                binding.textNoDataLoans.setVisibility(View.VISIBLE);
                binding.rvLoans.setVisibility(View.GONE);
                return;
            } else {
                binding.textNoDataLoans.setVisibility(View.GONE);
                binding.rvLoans.setVisibility(View.VISIBLE);
            }
            disbursalsAdapter = new DisbursalsAdapter(requireActivity(), this, (items,loansPortfolio,position) -> {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("loanData", (ArrayList<? extends Parcelable>) items);
                bundle.putInt("position", position);
                Intent intent = new Intent(requireActivity(), TransactionHistoryActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

              /*  Intent intent = new Intent(getActivity(), LoanCollectionActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra(Constants.KeyExtras.CONTRACT_ID, loansPortfolio.contractuuid);
                intent.putExtra(Constants.KeyExtras.LINKED_PROFILE, loansPortfolio);
                startActivity(intent);*/

                //                Bundle bundle = new Bundle();
//                bundle.putString("type", "2");
//                bundle.putString(Constants.KeyExtras.CONTRACT_ID, loansPortfolio.contractuuid);
//                bundle.putParcelable(Constants.KeyExtras.LINKED_PROFILE, loansPortfolio);
//                iOnFragmentChangeListener.onFragmentChanged(Constants.LOAN_COLLECTIONS_FRAGMENT, bundle);
            });
            disbursalsAdapter.setData(response.data.portfolio, true);
            binding.rvLoans.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            binding.rvLoans.setAdapter(disbursalsAdapter);
        } else {
            isLoading = false;
            binding.progressBar.setVisibility(View.GONE);
            if (response.data.portfolio == null) {
                binding.textNoDataLoans.setVisibility(View.VISIBLE);
                binding.rvLoans.setVisibility(View.GONE);
                return;
            } else {
                binding.textNoDataLoans.setVisibility(View.GONE);
                binding.rvLoans.setVisibility(View.VISIBLE);
            }
            disbursalsAdapter.setData(response.data.portfolio, false);
        }
    }

    private void loadSearchDisbursals(LoansPortfolioResponse response) {
        // binding.containerCollections.setVisibility(View.GONE);
        //binding.containerDisbursals.setVisibility(View.VISIBLE);
        isLoading = false;
        binding.progressBar.setVisibility(View.GONE);
        if (response.data.portfolio == null) {
            binding.textNoDataLoans.setVisibility(View.VISIBLE);
            binding.rvLoans.setVisibility(View.GONE);
            return;
        } else {
            binding.textNoDataLoans.setVisibility(View.GONE);
            binding.rvLoans.setVisibility(View.VISIBLE);
            if (response.data.portfolio.size() > 0) {
                disbursalsAdapter.setData(response.data.portfolio, searchPageIndex == 0);
            } else {
                Toast.makeText(getActivity(), "No search Data", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.queryText = query;
        if (TextUtils.isEmpty(query)) {
            isLoading = false;
            pageIndex = 0;
            loansPageIndex = 0;
            getLoans();
          /*  if (isCollections) {
                getCollectionPortfolio();
            } else {

            }*/

        } else {
            getSearchLoans(query);
          /*  if (isCollections) {
                getSearchCollection(query);
            } else {

            }*/

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.queryText = newText;
        if (TextUtils.isEmpty(newText)) {
            isLoading = false;
            pageIndex = 0;
            loansPageIndex = 0;
            getLoans();
         /*   if (isCollections) {
                getCollectionPortfolio();
            } else {

            }*/
        }
        return false;
    }

    @Override
    public void onSearchViewShown() {
        Log.e("msg", "search open");
        binding.tabs.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchViewClosed() {
        Log.e("msg", "search close");
        binding.tabs.setVisibility(View.GONE);

    }

    @Override
    public void onItemViewClick(String profileId) {
        Intent intent = new Intent(requireActivity(), BusinessDocumentsActivity.class);
        intent.putExtra("profileId", profileId);
        startActivity(intent);
    }

    @Override
    public void onItemViewTermsClick(String contractUUID) {
        Intent intent = new Intent(requireActivity(), TermsLoanActivity.class);
        intent.putExtra("contractUUID", contractUUID);
        startActivity(intent);
    }

}
