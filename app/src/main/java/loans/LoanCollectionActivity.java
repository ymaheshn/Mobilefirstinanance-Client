package loans;

import Utilities.AlertDialogUtils;
import Utilities.Constants;
import Utilities.PreferenceConnector;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import base.BaseActivity;
import base.LocalDatabase;
import bluetooth.BluetoothDeviceFragment;
import butterknife.OnClick;
import com.odedtech.mff.mffapp.R;
import com.odedtech.mff.mffapp.databinding.ActivityLoanCollectionBinding;
import com.prowesspride.api.Setup;
import loans.model.*;
import network.MFFApiWrapper;
import networking.MyApplication;
import org.json.JSONArray;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class LoanCollectionActivity extends BaseActivity implements LoanCollectionsFragmentCallback, View.OnClickListener {

    private static final int RC_LOCATION = 9;
    private ActivityLoanCollectionBinding binding;
    private String type = "";
    private String eventType = "";
    private ProgressDialog progressDialog = null;
    private Context context;
    private InstallmentsAdapter installmentsAdapter;
    private LoansCollectionAdapter loansCollectionAdapter;
    private String contractUuid;
    private LoanBluetoothData bluetoothData;
    private int receiptId = -1;
    private int collectedAmount;
    private CollectionPortfolio linkedProfileData;
    private LoansPortfolio linkedProfileData1;
    boolean showPrint = false;
    private LoanCollectionsPresenter loansPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoanCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        loansPresenter = new LoanCollectionsPresenter(context, this);
        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("1")) {
            linkedProfileData = getIntent().
                    getParcelableExtra(Constants.KeyExtras.LINKED_PROFILE);
            binding.tvTitle.setText("Contacts");
        } else {
            linkedProfileData1 = getIntent().getParcelableExtra(Constants.KeyExtras.LINKED_PROFILE);
            binding.tvTitle.setText("Loans");
        }
        contractUuid = getIntent().getStringExtra(Constants.KeyExtras.CONTRACT_ID);

        // Initial the Bluetooth printer setup
        if (MyApplication.setup == null) {
            try {
                Setup setup = new Setup();
                setup.blActivateLibrary(this, R.raw.licence);
                MyApplication.setup = setup;
                LocalDatabase.getInstance().setBluetoothSetup(setup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationilze),
                    RC_LOCATION, perms);
        }

        if (type.equalsIgnoreCase("1")) {
            if (linkedProfileData != null && linkedProfileData.contractCodes != null) {
                binding.nameTV.setText(linkedProfileData.contractCodes.name);
                binding.textRole.setText(linkedProfileData.contractCodes.identifier);
            }
        } else {
            if (linkedProfileData1 != null && linkedProfileData1.loanContractCodes != null) {
                binding.nameTV.setText(linkedProfileData1.loanContractCodes.getName());
                binding.textRole.setText(linkedProfileData1.loanContractCodes.getIdentifier());
            }
        }
        getPortfolioDetails();
        binding.btnPrint.setOnClickListener(this);
        binding.btnCollect.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void getPortfolioDetails() {
        if (type.equalsIgnoreCase("2")) {
            eventType = "IED,FP";
        }
        String accessToken = PreferenceConnector.readString(context,
                getString(R.string.accessToken), "");
        MFFApiWrapper.getInstance().service.getPortfolioCollectionDetails(accessToken, contractUuid, eventType)
                .enqueue(new Callback<CollectionPortfolioDetailsResponse>() {
                    @Override
                    public void onResponse(Call<CollectionPortfolioDetailsResponse> call,
                                           Response<CollectionPortfolioDetailsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (type.equalsIgnoreCase("1")) {
                                preProcessData(response.body().data.portfolio);
                            } else {
                                preProcessLoansData(response.body().data.portfolio);
                            }
                        } else {
                            Toast.makeText(context,
                                    getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CollectionPortfolioDetailsResponse> call, Throwable t) {
                        Toast.makeText(context,
                                getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void preProcessLoansData(List<CollectionPortfolioDetails> profileCollections) {
        ArrayList<CollectionPortfolioDetails> installments = new ArrayList<>();
        for (CollectionPortfolioDetails profileCollection : profileCollections) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
            Date result;
            try {
                result = df.parse(profileCollection.event_time);
                profileCollection.event_time = result.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                profileCollection.event_time = sdf.format(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            installments.add(profileCollection);
        }
        ArrayList<Installments> adapterInstallments = new ArrayList<>();
        Collections.sort(installments, (o1, o2) -> o1.event_time.compareTo(o2.event_time));
        for (int i = 0; i < installments.size(); i++) {
            Installments inst = new Installments();
            CollectionPortfolioDetails collection = installments.get(i);
            inst.collectionPR = collection;
            adapterInstallments.add(inst);
        }
         loansCollectionAdapter = new LoansCollectionAdapter(context, adapterInstallments, new LoansCollectionAdapter.OnUpdateAmount() {
            @Override
            public void onUpdate(double value) {
                binding.editLoanAmount.setText(String.valueOf(value));
                binding.containerCollect.setVisibility(View.VISIBLE);
                binding.containerPayEdit.setVisibility(View.VISIBLE);
            }
        });
        binding.rcInstallments.setAdapter(loansCollectionAdapter);
        if (adapterInstallments == null || adapterInstallments.size() == 0) {
            noData();
        } else {
            dataAvl();
        }
    }

    private void preProcessData(List<CollectionPortfolioDetails> profileCollections) {
        ArrayList<CollectionPortfolioDetails> installments = new ArrayList<>();
        for (CollectionPortfolioDetails profileCollection : profileCollections) {
            if (!TextUtils.isEmpty(profileCollection.event_type) && (profileCollection.event_type.equalsIgnoreCase("PR")
                    || profileCollection.event_type.equalsIgnoreCase("IP"))) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
                Date result;
                try {
                    result = df.parse(profileCollection.event_time);
                    profileCollection.event_time = result.toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    profileCollection.event_time = sdf.format(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                installments.add(profileCollection);
            }
        }

        ArrayList<Installments> adapterInstallments = new ArrayList<>();
        Collections.sort(installments, (o1, o2) -> o1.event_time.compareTo(o2.event_time));

        for (int i = 0; i < installments.size(); i = i + 2) {
            Installments inst = new Installments();
            CollectionPortfolioDetails collection = installments.get(i);
            if (collection.event_type.equals("PR")) {
                inst.collectionPR = collection;
                inst.collectionIP = installments.get(i + 1);
            } else {
                inst.collectionIP = collection;
                inst.collectionPR = installments.get(i + 1);
            }
            adapterInstallments.add(inst);
        }

        installmentsAdapter = new InstallmentsAdapter(context, adapterInstallments, new InstallmentsAdapter.OnUpdateAmount() {
            @Override
            public void onUpdate(double value) {
                binding.editLoanAmount.setText(String.valueOf(value));
                binding.containerCollect.setVisibility(View.VISIBLE);
                binding.containerPayEdit.setVisibility(View.VISIBLE);
            }
        });
        binding.rcInstallments.setAdapter(installmentsAdapter);
        if (adapterInstallments == null || adapterInstallments.size() == 0) {
            noData();
        } else {
            dataAvl();
        }
    }

    private void noData() {
        binding.rcInstallments.setVisibility(View.GONE);
        //  bottomLayout.setVisibility(View.GONE);
        binding.containerPayEdit.setVisibility(View.GONE);
        binding.containerCollect.setVisibility(View.GONE);
        binding.textNoRepayments.setVisibility(View.VISIBLE);
        if (showPrint) {
            binding.btnPrint.setVisibility(View.VISIBLE);
        } else {
            binding.btnPrint.setVisibility(View.GONE);
        }
    }

    private void dataAvl() {
        binding.rcInstallments.setVisibility(View.VISIBLE);
        //   bottomLayout.setVisibility(View.VISIBLE);
        binding.containerPayEdit.setVisibility(View.GONE);
        binding.containerCollect.setVisibility(View.GONE);
        binding.textNoRepayments.setVisibility(View.GONE);
        binding.btnPrint.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressBar() {
        progressDialog = new ProgressDialog(context);
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLogoutAlert() {
        AlertDialogUtils.getAlertDialogUtils().showAlert(LoanCollectionActivity.this);
    }


    @Override
    public void onSaveContractData(String message, int recieptId, int i) {
        dismissLoading();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(context, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            return;
        }
        this.receiptId = recieptId;
        this.collectedAmount = collectedAmount;
        binding.editLoanAmount.setText("");
        Toast.makeText(context, message + ", Please print the receipt", Toast.LENGTH_SHORT).show();
        binding.containerCollect.setVisibility(View.GONE);
        binding.containerPayEdit.setVisibility(View.GONE);
        binding.editLoanAmount.setEnabled(false);
        binding.editLoanAmount.setFocusable(false);
        binding.editLoanAmount.setClickable(false);
        //loansPresenter.getLinkedProfileDetails(contractUuid);
        getPortfolioDetails();
    }

    @Override
    public void onGetLinkedProfile(Datum datum, List<ProfileCollection> profileCollections) {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_collect:
                onCollectClick();
                break;
            case R.id.btn_print:
                onPrintClick();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void onCollectClick() {
        String btnCollectText = binding.btnCollect.getText().toString();
        String message = null;
        if (btnCollectText.equals(getString(R.string.collection_cash))) {
            message = getString(R.string.do_you_want_pay);
            String loanAmount = binding.editLoanAmount.getText().toString();
            if (TextUtils.isEmpty(loanAmount)) {
                Toast.makeText(context, R.string.please_enter_some_amount, Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (btnCollectText.equals(getString(R.string.print_receipt))) {
            message = getString(R.string.print_the_reciept);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("Yes", (dialogInterface, i) -> {
            if (btnCollectText.equalsIgnoreCase(getString(R.string.collection_cash))) {
                JSONArray data;
                if(type.equalsIgnoreCase("1")){
                    data = installmentsAdapter.getSavedPortfolioData();
                    bluetoothData = installmentsAdapter.getBluetoothData(bluetoothData);
                }else {
                    data = loansCollectionAdapter.getSavedPortfolioData();
                    bluetoothData = loansCollectionAdapter.getBluetoothData(bluetoothData);
                }

                showLoading();
                showPrint = true;
//                        savePortfolioCollectedLoan(data);
                loansPresenter.savePayment(0, data);
//                        loansPresenter.saveContractData(((DashboardActivity) getActivity()).addLoanCollectionUseCase, editLoanAmount.getText().toString(),
//                                linkedProfileData.contractCodes.contractCode);
            } else if (btnCollectText.equalsIgnoreCase(getString(R.string.print_receipt))) {
                if (receiptId != -1 && bluetoothData != null) {
                    // Start the Bluetooth
//                            bluetoothData = installmentsAdapter.getBluetoothData();
                    if (type.equalsIgnoreCase("1")) {
                        BluetoothDeviceFragment deviceFragment = new BluetoothDeviceFragment(new BluetoothDeviceFragment.DeviceConnected() {
                            @Override
                            public void deviceConnected(boolean isConnected) {

                            }

                            @Override
                            public void printSuccessfully() {
                                onBackPressed();
                            }
                        }, linkedProfileData.contractCodes, collectedAmount, bluetoothData.interest,
                                bluetoothData.principal, bluetoothData.principal, receiptId, type);

                        deviceFragment.show(getSupportFragmentManager(),
                                BluetoothDeviceFragment.class.getSimpleName());
                    } else {
                        BluetoothDeviceFragment deviceFragment = new BluetoothDeviceFragment(new BluetoothDeviceFragment.DeviceConnected() {
                            @Override
                            public void deviceConnected(boolean isConnected) {

                            }

                            @Override
                            public void printSuccessfully() {
                                onBackPressed();
                            }
                        }, linkedProfileData1.loanContractCodes, collectedAmount, bluetoothData.interest,
                                bluetoothData.principal, bluetoothData.principal, receiptId, type);

                        deviceFragment.show(getSupportFragmentManager(),
                                BluetoothDeviceFragment.class.getSimpleName());
                    }
                } else {
                    Toast.makeText(context, "You didn't collect the loan", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", null).show();
    }

    public void onPrintClick() {
        if (receiptId != -1 && bluetoothData != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            String message = getString(R.string.print_the_reciept);
            builder.setMessage(message).setPositiveButton("Yes", (dialogInterface, i) -> {
                // Start the Bluetooth
//                bluetoothData = installmentsAdapter.getBluetoothData();
                if (type.equalsIgnoreCase("1")) {
                    BluetoothDeviceFragment deviceFragment = new BluetoothDeviceFragment(new BluetoothDeviceFragment.DeviceConnected() {
                        @Override
                        public void deviceConnected(boolean isConnected) {

                        }

                        @Override
                        public void printSuccessfully() {
                            onBackPressed();
                        }
                    }, linkedProfileData.contractCodes, collectedAmount, bluetoothData.interest,
                            bluetoothData.principal, bluetoothData.total, receiptId, type);
                    deviceFragment.show(getSupportFragmentManager(),
                            BluetoothDeviceFragment.class.getSimpleName());
                } else {
                    BluetoothDeviceFragment deviceFragment = new BluetoothDeviceFragment(new BluetoothDeviceFragment.DeviceConnected() {
                        @Override
                        public void deviceConnected(boolean isConnected) {

                        }

                        @Override
                        public void printSuccessfully() {
                            onBackPressed();
                        }
                    }, linkedProfileData1.loanContractCodes, collectedAmount, bluetoothData.interest,
                            bluetoothData.principal, bluetoothData.total, receiptId, type);
                    deviceFragment.show(getSupportFragmentManager(),
                            BluetoothDeviceFragment.class.getSimpleName());
                }


            }).setNegativeButton("Cancel", null).show();
        } else {
            Toast.makeText(context, "You didn't collect the loan", Toast.LENGTH_LONG).show();
        }
    }
}
