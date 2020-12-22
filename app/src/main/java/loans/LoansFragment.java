package loans;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;
import com.odedtech.mff.mffapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dashboard.DashboardActivity;
import interfaces.IOnFragmentChangeListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import loans.model.Datum;
import loans.model.LinkedProfilesResponse;
import loans.model.LoanCollection;
import loans.model.ProfileCollection;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoansFragment extends BaseFragment implements LoansFragmentCallback,
        MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rv_list)
    MultiLevelRecyclerView clientsRV;


    @BindView(R.id.clientsRV)
    RecyclerView disbursalsRecyclerView;

    @BindView(R.id.container_disbursals)
    LinearLayout containerDisbursals;

    @BindView(R.id.noClientsTV)
    TextView noClientsTV;

    @BindView(R.id.text_load_date)
    TextView textDate;

    @BindView(R.id.switch_pending)
    SwitchCompat pendingSwitch;

    @BindView(R.id.radio_contracts)
    RadioGroup radioGroupContracts;

    private Unbinder unbinder;
    private IOnFragmentChangeListener iOnFragmentChangeListener;
    private LoansPresenter loansPresenter;
    private ProgressDialog progressDialog = null;
    private ArrayList<Datum> data;
    //    private LoansAdapter loansAdapter;
    private List<LoanCollection> loanCollections;

    public LoansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loans, container, false);
        unbinder = ButterKnife.bind(this, view);
        data = new ArrayList<>();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.LOANS_FRAGMENT, "Loans");
        getAllLoanCollections();
        loansPresenter = new LoansPresenter(getActivity(), this);
        radioGroupContracts.setOnCheckedChangeListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        disbursalsRecyclerView.setLayoutManager(linearLayoutManager);
        disbursalsRecyclerView.setAdapter(new DisbursalsAdapter(getActivity(), datum -> {

        }));
    }


    @OnClick({R.id.text_load_date})
    public void onClick(View view) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view1, year, monthOfYear, dayOfMonth) -> {
                    String month = "" + (monthOfYear + 1);
                    if (monthOfYear < 10) {
                        month = "0" + (monthOfYear + 1);
                    }
                    String day = "" + (dayOfMonth);
                    if (dayOfMonth < 10) {
                        day = "0" + (dayOfMonth);
                    }
                    textDate.setText(UtilityMethods.getDateFormat(day + "" + month + "" + year));
                    refreshList();
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getAllLoanCollections() {
        Flowable<List<LoanCollection>> loanData = ((DashboardActivity) getActivity()).getLoanCollectionsUseCase.getComments();
        Disposable disposable = loanData
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loanCollections -> LoansFragment.this.loanCollections = loanCollections, throwable -> {
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textDate.setText(UtilityMethods.getDateFormat());
        pendingSwitch.setOnCheckedChangeListener((compoundButton, b) -> getPendingList(b));
    }

    private void refreshList() {
        ArrayList<Datum> contracts = new ArrayList<>();
        // TODO
//        for (Datum datum : this.data) {
//            String collectionDate = datum.contractCodes.nextCollectionDate;
//            Date dateCollection = UtilityMethods.getDateFromString(collectionDate, "yyyyMMdd");
//            UtilityMethods.getDateFromString(collectionDate, "yyyy-MM-dd");
//            Date selectedDate = UtilityMethods.getDateFromString(textDate.getText().toString(), "MM-dd-yyyy");
//            if (selectedDate.compareTo(dateCollection) >= 0) {
//                contracts.add(datum);
//            }
//        }
//        loansAdapter.addItems(contracts);
    }


    private void getPendingList(boolean isPending) {
        ArrayList<Datum> data = new ArrayList<>();
        for (Datum datum : this.data) {
            if (isPending && !datum.isPending) {
                data.add(datum);
            } else if (!isPending && datum.isPending) {
                data.add(datum);
            }
        }
//        loansAdapter.addItems(data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unbind the view to free some memory
        unbinder.unbind();
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
    public void loadRecyclerView(LinkedProfilesResponse profilesResponse) {
        if (profilesResponse != null && profilesResponse.data != null
                && profilesResponse.data.size() > 0) {
            if (!isVisible() || clientsRV == null) {
                return;
            }
            data.clear();
            data.addAll(profilesResponse.data);
            //clientsRV.setVisibility(View.VISIBLE);
            noClientsTV.setVisibility(View.GONE);
            if (loanCollections != null && loanCollections.size() > 0) {
                for (LoanCollection loanCollection :
                        loanCollections) {
                    for (Datum datum : profilesResponse.data) {
                        if (datum.contractCodes.identifier.equalsIgnoreCase(loanCollection.getContractCode())) {
                            int collectedAmount = Integer.parseInt(loanCollection.getLoanAmount());
                            // TODO
//                            datum.contractCodes.collectionAmount = collectedAmount;
                            datum.isPending = false;
                        }
                    }
                }
            }


            HashMap<String, HashMap> branchData = new HashMap<>();
            // This for loop is for grouping based on the branch
            for (Datum datum : profilesResponse.data) {
                HashMap<String, List<Datum>> groupData = new HashMap<>();
                for (Datum againDatum : profilesResponse.data) {
                    if (datum.contractCodes.branch.equals(againDatum.contractCodes.branch)) {
                        if (TextUtils.isEmpty(againDatum.contractCodes.group)) {
                            againDatum.contractCodes.group = "Group";
                        }
                        if (groupData.containsKey(againDatum.contractCodes.group)) {
                            List<Datum> data = groupData.get(againDatum.contractCodes.group);
                            data.add(againDatum);
                        } else {
                            ArrayList<Datum> data = new ArrayList<>();
                            data.add(againDatum);
                            groupData.put(againDatum.contractCodes.group, data);
                        }
                    }
                }
                branchData.put(TextUtils.isEmpty(datum.contractCodes.branch) ? "Branch" : datum.contractCodes.branch, groupData);
            }

            ArrayList<Item> items = new ArrayList<>();
            Set<Map.Entry<String, HashMap>> entries = branchData.entrySet();
            for (Map.Entry<String, HashMap> entry : entries) {
                Item item = new Item(0);
                item.setText(entry.getKey());
                Set<Map.Entry<String, List<Datum>>> groupEntries = entry.getValue().entrySet();
                List<RecyclerViewItem> groupItems = new ArrayList<>();
                for (Map.Entry<String, List<Datum>> groupEntry : groupEntries) {
                    if (groupEntry.getKey().equalsIgnoreCase("Group")) {
                        for (Datum datum : groupEntry.getValue()) {
                            Item datamItem = new Item(1);
                            datamItem.setText(datum.contractCodes.name);
                            datamItem.setSecondText(datum.contractCodes.identifier);
                            datamItem.datum = datum;
                            groupItems.add(datamItem);
                        }
                    } else {
                        Item groupItem = new Item(1);
                        groupItem.setText(groupEntry.getKey());
                        List<RecyclerViewItem> datumItems = new ArrayList<>();
                        for (Datum datum : groupEntry.getValue()) {
                            Item datamItem = new Item(2);
                            datamItem.setText(datum.contractCodes.name);
                            datamItem.setSecondText(datum.contractCodes.identifier);
                            datamItem.datum = datum;
                            datumItems.add(datamItem);
                        }
                        groupItem.addChildren(datumItems);
                        groupItems.add(groupItem);
                    }
                }
                item.addChildren(groupItems);
                items.add(item);
            }

            if (items.size() == 0) {
                noClientsTV.setVisibility(View.VISIBLE);
                clientsRV.setVisibility(View.GONE);
                return;
            }
            MyAdapter myAdapter = new MyAdapter(getActivity(), items, clientsRV, datum -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.KeyExtras.LINKED_PROFILE, datum);
//                iOnFragmentChangeListener.onFragmentChanged(Constants.LOAN_COLLECTIONS_FRAGMENT, bundle);
                loansPresenter.getLinkedProfileDetails(datum);
            }, noData -> {
                noClientsTV.setVisibility(noData ? View.VISIBLE : View.GONE);
                clientsRV.setVisibility(noData ? View.GONE : View.VISIBLE);
            });


            //If you want to already opened Multi-RecyclerView just call openTill where is parameter is
            // position to corresponding each level.
//            multiLevelRecyclerView.openTill(0, 1, 2, 3);

//            loansAdapter = new LoansAdapter(getActivity(), profilesResponse.data, datum -> {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(Constants.KeyExtras.LINKED_PROFILE, datum);
////                iOnFragmentChangeListener.onFragmentChanged(Constants.LOAN_COLLECTIONS_FRAGMENT, bundle);
//                loansPresenter.getLinkedProfileDetails(datum);
//            }, noData -> {
//                noClientsTV.setVisibility(noData ? View.VISIBLE : View.GONE);
//                clientsRV.setVisibility(noData ? View.GONE : View.VISIBLE);
//            });
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(clientsRV.getContext(), DividerItemDecoration.VERTICAL);
            clientsRV.addItemDecoration(dividerItemDecoration);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            clientsRV.setLayoutManager(linearLayoutManager);
            clientsRV.setAdapter(myAdapter);

            clientsRV.post(() -> {
                int previousPosition = PreferenceConnector.readInteger(getContext(), "ListPosition", -1);
                if (previousPosition >= 0) {
                    clientsRV.scrollToPosition(previousPosition);
                }
                getPendingList(false);
                // Here adapter.getItemCount()== child count
            });

        } else {
            noClientsTV.setVisibility(View.VISIBLE);
            clientsRV.setVisibility(View.GONE);
        }
    }

    @Override
    public void linkedProfileCollection(Datum datum, List<ProfileCollection> profileCollections) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KeyExtras.CONTRACT_ID, datum.contractUUID);
        bundle.putParcelable(Constants.KeyExtras.LINKED_PROFILE, datum);
        bundle.putParcelableArrayList(Constants.KeyExtras.LINKED_PROFILE_COLLECTION, (ArrayList<ProfileCollection>) profileCollections);
        iOnFragmentChangeListener.onFragmentChanged(Constants.LOAN_COLLECTIONS_FRAGMENT, bundle);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        loansAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        loansAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onSearchViewShown() {
    }

    @Override
    public void onSearchViewClosed() {
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radio_collections:
                noClientsTV.setVisibility(View.VISIBLE);
                noClientsTV.setText("No Collections Available");
                disbursalsRecyclerView.setVisibility(View.GONE);
                clientsRV.setVisibility(View.VISIBLE);
                if (clientsRV.getAdapter() != null) {
                    clientsRV.setVisibility(View.VISIBLE);
                } else {
                    clientsRV.setVisibility(View.GONE);
                }
                containerDisbursals.setVisibility(View.GONE);
                break;
            case R.id.radio_disbursals:
                noClientsTV.setVisibility(View.GONE);
                noClientsTV.setText("No Disbursals Available");
                clientsRV.setVisibility(View.GONE);
                disbursalsRecyclerView.setVisibility(View.VISIBLE);
                containerDisbursals.setVisibility(View.VISIBLE);
                break;
        }
    }

    private List<?> recursivePopulateFakeData(int levelNumber, int depth) {
        List<RecyclerViewItem> itemList = new ArrayList<>();

        String title;
        switch (levelNumber) {
            case 1:
                title = "PQRST %d";
                break;
            case 2:
                title = "XYZ %d";
                break;
            default:
                title = "ABCDE %d";
                break;
        }

        for (int i = 0; i < depth; i++) {
            Item item = new Item(levelNumber);
            item.setText(String.format(Locale.ENGLISH, title, i));
            item.setSecondText(String.format(Locale.ENGLISH, title.toLowerCase(), i));
            if (depth % 2 == 0) {
                item.addChildren((List<RecyclerViewItem>) recursivePopulateFakeData(levelNumber + 1, depth / 2));
            }
            itemList.add(item);
        }

        return itemList;
    }

    public class Item extends RecyclerViewItem {

        String text = "";

        String secondText = "";

        public Datum datum;

        public String getSecondText() {
            return secondText;
        }

        public void setSecondText(String secondText) {
            this.secondText = secondText;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        protected Item(int level) {
            super(level);
        }
    }
}
