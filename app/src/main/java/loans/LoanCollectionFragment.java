package loans;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.odedtech.mff.mffapp.R;
import com.prowesspride.api.Setup;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import base.BaseFragment;
import base.LocalDatabase;
import bluetooth.BluetoothDeviceFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dashboard.DashboardActivity;
import interfaces.IOnFragmentChangeListener;
import loans.model.Datum;
import loans.model.Installments;
import loans.model.LoanBluetoothData;
import loans.model.ProfileCollection;
import networking.MyApplication;
import pub.devrel.easypermissions.EasyPermissions;

public class LoanCollectionFragment extends BaseFragment implements LoanCollectionsFragmentCallback {
    private static final int RC_LOCATION = 9;

    @BindView(R.id.edit_loan_amount)
    TextView editLoanAmount;

    @BindView(R.id.btn_collect)
    Button btnCollect;

//    @BindView(R.id.text_collection_principal)
//    TextView textPrincipal;
//
//    @BindView(R.id.text_collection_interest)
//    TextView textInterest;
//
//    @BindView(R.id.text_collection_total)
//    TextView textTotal;
//
//    @BindView(R.id.text_collection_balance)
//    TextView textBalance;

    private Unbinder unbinder;
    private LoanCollectionsPresenter loansPresenter;
    private ProgressDialog progressDialog = null;
    private Datum linkedProfileData;
    private int collectedAmount;
    private List<ProfileCollection> profileCollections;

    private RecyclerView recyclerView;

    private InstallmentsAdapter installmentsAdapter;

    private String contractUuid;
    private LoanBluetoothData bluetoothData;
    private View textNoRepayment;
    private View bottomLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loans_collections, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Initial the Bluetooth printer setup
        if (MyApplication.setup == null) {
            try {
                Setup setup = new Setup();
                setup.blActivateLibrary(getActivity(), R.raw.licence);
                MyApplication.setup = setup;
                LocalDatabase.getInstance().setBluetoothSetup(setup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationilze),
                    RC_LOCATION, perms);
        }
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        linkedProfileData = arguments.
                getParcelable(Constants.KeyExtras.LINKED_PROFILE);
        contractUuid = arguments.
                getString(Constants.KeyExtras.CONTRACT_ID);
        profileCollections = arguments.getParcelableArrayList(Constants.KeyExtras.LINKED_PROFILE_COLLECTION);
//        for (ProfileCollection pf2: profileCollections
//             ) {
//            Log.i("pf2","pf2 :"+pf2);
//        }
        textNoRepayment = view.findViewById(R.id.text_no_repayments);
        recyclerView = view.findViewById(R.id.rc_installments);
        bottomLayout = view.findViewById(R.id.bottom_layout);

        preProcessData(profileCollections);

        TextView textName = view.findViewById(R.id.nameTV);
        textName.setText(linkedProfileData.contractCodes.name);
        TextView textRole = view.findViewById(R.id.text_role);
        textRole.setText(linkedProfileData.contractCodes.identifier);
//        int balanceAmount = linkedProfileData.contractCodes.nextTotalCollection;
//        int collectionAmount = linkedProfileData.contractCodes.collectionAmount;
//        if (linkedProfileData.contractCodes.nextTotalCollection < linkedProfileData.contractCodes.collectionAmount) {
//            balanceAmount = 0;
//            collectionAmount = 0;
//        }
//        balanceAmount = balanceAmount - collectionAmount;
//        textTotal.setText(getString(R.string.next_total_collection) + ": ₹" + balanceAmount);
//        textInterest.setText(getString(R.string.next_collection_interest) + ": ₹" + linkedProfileData.contractCodes.nextCollectionInterest);
//        textPrincipal.setText(getString(R.string.next_collection_principal) + ": ₹" + (linkedProfileData.contractCodes.nextCollectionPrincipal - collectionAmount));
//        textBalance.setText(getString(R.string.balance) + ": ₹" + balanceAmount);
//
//        editLoanAmount.setFilters(new InputFilter[]{new InputFilterMinMax("1", linkedProfileData.contractCodes.nextTotalCollection + "")});
    }

    private void preProcessData(List<ProfileCollection> profileCollections) {
        ArrayList<ProfileCollection> installments = new ArrayList<>();
        for (ProfileCollection profileCollection2 : profileCollections) {
            if (!TextUtils.isEmpty(profileCollection2.event_type) && (profileCollection2.event_type.equalsIgnoreCase("PR")
                    || profileCollection2.event_type.equalsIgnoreCase("IP"))) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
                Date result;
                try {
                    result = df.parse(profileCollection2.event_time);
                    profileCollection2.event_time = result.toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    profileCollection2.event_time = sdf.format(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                installments.add(profileCollection2);
            }
        }

        ArrayList<Installments> adapterInstallments = new ArrayList<>();
        Collections.sort(installments, (o1, o2) -> o1.event_time.compareTo(o2.event_time));
        for (int i = 0; i < installments.size(); i = i + 2) {
            Installments inst = new Installments();
            ProfileCollection collection = installments.get(i);
            if (collection.event_type.equals("PR")) {
                inst.collectionPR = collection;
                inst.collectionIP = installments.get(i + 1);
            } else {
                inst.collectionIP = collection;
                inst.collectionPR = installments.get(i + 1);
            }
            adapterInstallments.add(inst);
        }

        installmentsAdapter = new InstallmentsAdapter(getActivity(), adapterInstallments, new InstallmentsAdapter.OnUpdateAmount() {
            @Override
            public void onUpdate(double value) {
                editLoanAmount.setText(String.valueOf(value));
            }
        });
        recyclerView.setAdapter(installmentsAdapter);
        if (adapterInstallments == null || adapterInstallments.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            textNoRepayment.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            textNoRepayment.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((DashboardActivity) getActivity()).setToolBarBackVisible(true);
        IOnFragmentChangeListener iOnFragmentChangeListener = (IOnFragmentChangeListener) getActivity();
        iOnFragmentChangeListener.onHeaderUpdate(Constants.LOAN_COLLECTIONS_FRAGMENT, "Contracts");
        loansPresenter = new LoanCollectionsPresenter(getActivity(), this);
//        TODO : check with new implementation
//        loansPresenter.getPortfolioData(((DashboardActivity) getActivity()).getLoanCollectionsUseCase,
//                linkedProfileData.contractCodes.contractCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
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

    @OnClick({R.id.btn_print})
    public void onPrintClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getString(R.string.print_the_reciept);
        builder.setMessage(message).setPositiveButton("Yes", (dialogInterface, i) -> {
            // Start the Bluetooth
            bluetoothData = installmentsAdapter.getBluetoothData();
            BluetoothDeviceFragment deviceFragment = new BluetoothDeviceFragment(new BluetoothDeviceFragment.DeviceConnected() {
                @Override
                public void deviceConnected(boolean isConnected) {

                }

                @Override
                public void printSuccessfully() {
                    getActivity().onBackPressed();
                }
            }, linkedProfileData.contractCodes, collectedAmount, bluetoothData.interest,
                    bluetoothData.principal, bluetoothData.total);

            deviceFragment.show(getActivity().getSupportFragmentManager(),
                    BluetoothDeviceFragment.class.getSimpleName());
        }).setNegativeButton("Cancel", null).show();
    }

    @OnClick({R.id.btn_collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                String btnCollectText = ((Button) view).getText().toString();
                String message = null;
                if (btnCollectText.equals(getString(R.string.collection))) {
                    message = getString(R.string.do_you_want_pay);
                    String loanAmount = editLoanAmount.getText().toString();
                    if (TextUtils.isEmpty(loanAmount)) {
                        Toast.makeText(getActivity(), R.string.please_enter_some_amount, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (btnCollectText.equals(getString(R.string.print_receipt))) {
                    message = getString(R.string.print_the_reciept);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(message).setPositiveButton("Yes", (dialogInterface, i) -> {
                    if (btnCollectText.equalsIgnoreCase(getString(R.string.collection))) {
                        JSONArray data = installmentsAdapter.getSavedPortfolioData();
                        bluetoothData = installmentsAdapter.getBluetoothData();
                        showLoading();
                        loansPresenter.savePayment(0, data);
//                        loansPresenter.saveContractData(((DashboardActivity) getActivity()).addLoanCollectionUseCase, editLoanAmount.getText().toString(),
//                                linkedProfileData.contractCodes.contractCode);
                    } else if (btnCollectText.equalsIgnoreCase(getString(R.string.print_receipt))) {
                        // Start the Bluetooth
                        bluetoothData = installmentsAdapter.getBluetoothData();
                        BluetoothDeviceFragment deviceFragment = new BluetoothDeviceFragment(new BluetoothDeviceFragment.DeviceConnected() {
                            @Override
                            public void deviceConnected(boolean isConnected) {

                            }

                            @Override
                            public void printSuccessfully() {
                                getActivity().onBackPressed();
                            }
                        }, linkedProfileData.contractCodes, collectedAmount, bluetoothData.interest,
                                bluetoothData.principal, bluetoothData.principal);

                        deviceFragment.show(getActivity().getSupportFragmentManager(),
                                BluetoothDeviceFragment.class.getSimpleName());
                    }
                }).setNegativeButton("Cancel", null).show();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveContractData(String message, int collectedAmount) {
        dismissLoading();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            return;
        }
        this.collectedAmount = collectedAmount;
        editLoanAmount.setText("");
        Toast.makeText(getActivity(), message + ", Please print the receipt", Toast.LENGTH_SHORT).show();
        btnCollect.setVisibility(View.GONE);
        editLoanAmount.setEnabled(false);
        editLoanAmount.setFocusable(false);
        editLoanAmount.setClickable(false);
        loansPresenter.getLinkedProfileDetails(contractUuid);
    }

    @Override
    public void onGetLinkedProfile(Datum datum, List<ProfileCollection> profileCollections)  {
        preProcessData(profileCollections);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((DashboardActivity) getActivity()).setToolBarBackVisible(false);
    }
}
