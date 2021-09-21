package loans;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.mffapp.R;
import com.odedtech.mff.mffapp.databinding.FragmentLoansNewBinding;

import Utilities.Constants;
import Utilities.PreferenceConnector;
import base.BaseFragment;
import interfaces.IOnFragmentChangeListener;
import loans.model.CollectionPortfolioResponse;
import network.MFFApiWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoansFragmentNew extends BaseFragment implements MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {

    private FragmentLoansNewBinding binding;
    private IOnFragmentChangeListener iOnFragmentChangeListener;

    private boolean isLoading = false;
    private int pageIndex;
    private int searchPageIndex;
    private LoanCollectionPortfolioAdapter adapter;
    private String queryText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoansNewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        getCollectionPortfolio();
        loadCollectionDisbursals();

        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.LOANS_FRAGMENT, "Loans");

        setPagingListeners();
    }

    private void initViews() {
        binding.radioContracts.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.radio_collections) {
                binding.containerDisbursals.setVisibility(View.GONE);
                binding.containerCollections.setVisibility(View.VISIBLE);
            } else if (i == R.id.radio_disbursals) {
                binding.containerDisbursals.setVisibility(View.VISIBLE);
                binding.containerCollections.setVisibility(View.GONE);
            }
        });
    }

    private void setPagingListeners() {
        binding.rvCollections.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        });
    }

    public void getCollectionPortfolio() {
        if (pageIndex == 0) {
            showLoading();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }

        String accessToken = PreferenceConnector.readString(getActivity(),
                getActivity().getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getCollections(accessToken,
                pageIndex, 10).enqueue(new Callback<CollectionPortfolioResponse>() {
            @Override
            public void onResponse(Call<CollectionPortfolioResponse> call,
                                   Response<CollectionPortfolioResponse> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    loadCollectionPortfolio(response.body());
                } else {
                    checkResponseError(response);
                }
            }

            @Override
            public void onFailure(Call<CollectionPortfolioResponse> call, Throwable t) {
                dismissLoading();
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSearchCollection(String search) {
        if (searchPageIndex == 0) {
            showLoading();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        String accessToken = PreferenceConnector.readString(getActivity(),
                getActivity().getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.searchCollectionUsingName(accessToken,
                search, searchPageIndex, 10).enqueue(new Callback<CollectionPortfolioResponse>() {
            @Override
            public void onResponse(Call<CollectionPortfolioResponse> call,
                                   Response<CollectionPortfolioResponse> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    loadSearchedCollectionPortfolio(response.body());
                } else {
                    checkResponseError(response);
                }
            }

            @Override
            public void onFailure(Call<CollectionPortfolioResponse> call, Throwable t) {
                dismissLoading();
                Toast.makeText(getActivity(),
                        getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSearchedCollectionPortfolio(CollectionPortfolioResponse response) {
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
    }

    private void loadCollectionPortfolio(CollectionPortfolioResponse response) {
        binding.containerCollections.setVisibility(View.VISIBLE);
        binding.containerDisbursals.setVisibility(View.GONE);
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
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KeyExtras.CONTRACT_ID, collectionPortfolio.contractUUID);
                bundle.putParcelable(Constants.KeyExtras.LINKED_PROFILE, collectionPortfolio);
                iOnFragmentChangeListener.onFragmentChanged(Constants.LOAN_COLLECTIONS_FRAGMENT, bundle);
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
    }

    private void loadCollectionDisbursals() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        binding.rvLoans.setLayoutManager(linearLayoutManager);
        binding.rvLoans.setAdapter(new DisbursalsAdapter(getActivity(), datum -> {

        }));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.queryText = query;
        if (TextUtils.isEmpty(query)) {
            isLoading = false;
            pageIndex = 0;
            getCollectionPortfolio();
        } else {
            getSearchCollection(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        this.queryText = newText;
        if (TextUtils.isEmpty(newText)) {
            isLoading = false;
            pageIndex = 0;
            getCollectionPortfolio();
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
