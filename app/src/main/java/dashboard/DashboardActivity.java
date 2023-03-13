package dashboard;

import static Utilities.Constants.ADD_CLIENT_FRAGMENT;
import static Utilities.Constants.CURRENT_FRAGMENT;
import static Utilities.Constants.DASHBOARD_FRAGMENT;
import static Utilities.Constants.KYC_FRAGMENT;
import static Utilities.Constants.LOANS_FRAGMENT;
import static Utilities.Constants.LOAN_COLLECTIONS_FRAGMENT;
import static Utilities.Constants.MAP_FRAGMENT;
import static Utilities.Constants.ONBOARD_FRAGMENT;
import static Utilities.Constants.PROFILE_DETAILS_FRAGMENT;
import static Utilities.Constants.SAVINGS_FRAGMENT;
import static Utilities.Constants.SCAN;
import static Utilities.Constants.SEARCH_PROFILE_LIST_FRAGMENT;
import static Utilities.Constants.SHUFPTI_FRAGMENT;
import static Utilities.Constants.VAS_FRAGMENT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.client.R;

import java.util.ArrayList;

import javax.inject.Inject;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import addclient.AddClientFragment;
import base.BaseActivity;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import collections.PaymentsFragment;
import database.db.DBOperations;
import domain.AddLoanCollectionUseCase;
import domain.GetLoanCollectionsUseCase;
import interfaces.IOnDataRetrievedFromAdharScannerListener;
import interfaces.IOnDataRetrievedFromGallery;
import interfaces.IOnFragmentChangeListener;
import interfaces.IOnHeaderItemsClickListener;
import kyc.KycFragment;
import loans.AccountsFragment;
import loans.LoanCollectionFragment;
import loans.LoansFragment;
import maps.MapViewFragment;
import networking.MyApplication;
import onboard.ClientDataDTO;
import onboard.OnBoardFragment;
import pub.devrel.easypermissions.EasyPermissions;
import search_profiles_list.SearchProfilesListFragment;
import services.LocationUpdatesBroadcastReceiver;
import shufpti.ShufptiVerificationServicesActivity;
import vas.VasFragment;

public class DashboardActivity extends BaseActivity implements IOnFragmentChangeListener,
        MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {
    private static final int RC_LOCATION = 9;

    private static final int LOCATION_SETTINGS_REQUEST = 8;
    public static final int IMAGE_PICK_GALLERY = 10;

    @Inject
    public AddLoanCollectionUseCase addLoanCollectionUseCase;

    @Inject
    public GetLoanCollectionsUseCase getLoanCollectionsUseCase;

    @BindView(R.id.clientClickRV)
    RelativeLayout clientClickRV;
    @BindView(R.id.loansClickRV)
    RelativeLayout loansClickRV;
    @BindView(R.id.savingsClickRV)
    RelativeLayout savingsClickRV;
    @BindView(R.id.reportsClickRV)
    RelativeLayout reportsClickRV;
    @BindView(R.id.profileClickRV)
    RelativeLayout profileClickRV;
    @BindView(R.id.adharScannerIV)
    ImageView adharScannerIV;
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.headerTitleTV)
    TextView headerTitleTV;
    @BindView(R.id.toolbar_back)
    ImageView toolBarBack;
    @BindView(R.id.headerView)
    FrameLayout headerView;
    @BindView(R.id.contentFL)
    FrameLayout contentFL;


    @BindView(R.id.search_view)
    MaterialSearchView materialSearchView;

    @BindView(R.id.clientTV)
    TextView clientTV;

    @BindView(R.id.profileTV)
    TextView profileTV;

    @BindView(R.id.loansTV)
    TextView loansTV;

    @BindView(R.id.reportsTV)
    TextView reportsTV;

    @BindView(R.id.saavingsTV)
    TextView saavingsTV;

    @BindView(R.id.clientIV)
    AppCompatImageView clientIV;

    @BindView(R.id.profileIV)
    AppCompatImageView profileIV;

    @BindView(R.id.loansIV)
    AppCompatImageView loansIV;

    @BindView(R.id.reportsIV)
    AppCompatImageView reportsIV;

    @BindView(R.id.savingsIV)
    AppCompatImageView savingsIV;

    boolean doubleBackToExitPressedOnce = false;
    private IOnHeaderItemsClickListener iOnHeaderItemsClickListener;
    private IOnDataRetrievedFromAdharScannerListener iOnDataRetrievedFromAdharScannerListener;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private IOnDataRetrievedFromGallery onDataRetrievedFromGallery;
    private BaseFragment currentFragment;
    private String query;
    private boolean isSubmitClick;
    public String PREF_COLOR = String.valueOf(R.color.colorAccent);

    int selectedBackgroundColorId;

    private String colorTheme;
    private int colorCode;

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);
        DBOperations.getInstance(this);
        CURRENT_FRAGMENT = -1;
        addFragment(DASHBOARD_FRAGMENT, "");

        createLocationRequest();
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            checkLocationSetting();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationilze),
                    RC_LOCATION, perms);
        }

        colorTheme = PreferenceConnector.getThemeColor(getApplicationContext());
        colorCode = Color.parseColor(colorTheme);

        headerTitleTV.setTextColor(colorCode);

        clientTV.setTextColor(colorCode);
        clientIV.setColorFilter(colorCode);
        loansIV.setColorFilter(getResources().getColor(R.color.colorBlack, getResources().newTheme()));
        reportsIV.setColorFilter(getResources().getColor(R.color.colorBlack, getResources().newTheme()));
        profileIV.setColorFilter(getResources().getColor(R.color.colorBlack, getResources().newTheme()));
        savingsIV.setColorFilter(getResources().getColor(R.color.colorBlack, getResources().newTheme()));

        materialSearchView.setOnQueryTextListener(this);
        materialSearchView.setOnSearchViewListener(this);
        materialSearchView.setSubmitOnClick(false);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2 * 60 * 1000);
        mLocationRequest.setFastestInterval(2 * 60 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void checkLocationSetting() {
        if (UtilityMethods.isGpsEnabled(this)) {
            requestLocationUpdates();
        } else {
            enableLocation(mLocationRequest);
        }
    }

    public void enableLocation(LocationRequest locationRequest) {
        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(locationSettingsRequest);
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
            } catch (ApiException ex) {
                ex.printStackTrace();
                switch (ex.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException =
                                    (ResolvableApiException) ex;
                            resolvableApiException.startResolutionForResult(this,
                                    LOCATION_SETTINGS_REQUEST);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    public void removeLocationUpdates() {
        if (mFusedLocationProviderClient != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                mFusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
    }

    public void requestLocationUpdates() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        }
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        MyApplication.getInstance().setCurrentLocation(location);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @OnClick({R.id.clientClickRV, R.id.loansClickRV, R.id.savingsClickRV, R.id.reportsClickRV, R.id.adharScannerIV,
            R.id.img_search, R.id.img_more, R.id.toolbar_back, R.id.profileClickRV})
    public void onButtonClick(View view) {
        if (view.getId() == R.id.clientClickRV) {
            clientTV.setTextColor(colorCode);
            clientIV.setColorFilter(colorCode);
            loansIV.setColorFilter(getColor(R.color.colorBlack));
            reportsIV.setColorFilter(getColor(R.color.colorBlack));
            profileIV.setColorFilter(getColor(R.color.colorBlack));
            savingsIV.setColorFilter(getColor(R.color.colorBlack));


            loansTV.setTextColor(getColor(R.color.text_color_black));
            saavingsTV.setTextColor(getColor(R.color.text_color_black));
            reportsTV.setTextColor(getColor(R.color.text_color_black));
            profileTV.setTextColor(getColor(R.color.text_color_black));

            replaceFragment(DASHBOARD_FRAGMENT, null);
        } else if (view.getId() == R.id.loansClickRV) {
            loansTV.setTextColor(colorCode);

            loansIV.setColorFilter(colorCode);
            savingsIV.setColorFilter(getColor(R.color.colorBlack));
            reportsIV.setColorFilter(getColor(R.color.colorBlack));
            profileIV.setColorFilter(getColor(R.color.colorBlack));
            clientIV.setColorFilter(getColor(R.color.colorBlack));

            saavingsTV.setTextColor(getColor(R.color.text_color_black));
            reportsTV.setTextColor(getColor(R.color.text_color_black));
            profileTV.setTextColor(getColor(R.color.text_color_black));
            clientTV.setTextColor(getColor(R.color.text_color_black));
            replaceFragment(LOANS_FRAGMENT, null);
        } else if (view.getId() == R.id.savingsClickRV) {
            saavingsTV.setTextColor(colorCode);

            savingsIV.setColorFilter(colorCode);
            loansIV.setColorFilter(getColor(R.color.colorBlack));
            reportsIV.setColorFilter(getColor(R.color.colorBlack));
            profileIV.setColorFilter(getColor(R.color.colorBlack));
            clientIV.setColorFilter(getColor(R.color.colorBlack));

            loansTV.setTextColor(getColor(R.color.text_color_black));
            reportsTV.setTextColor(getColor(R.color.text_color_black));
            profileTV.setTextColor(getColor(R.color.text_color_black));
            clientTV.setTextColor(getColor(R.color.text_color_black));
            replaceFragment(VAS_FRAGMENT, null);
        } else if (view.getId() == R.id.reportsClickRV) {
            reportsTV.setTextColor(colorCode);
            loansTV.setTextColor(getColor(R.color.text_color_black));

            reportsIV.setColorFilter(colorCode);
            loansIV.setColorFilter(getColor(R.color.colorBlack));
            profileIV.setColorFilter(getColor(R.color.colorBlack));
            clientIV.setColorFilter(getColor(R.color.colorBlack));
            savingsIV.setColorFilter(getColor(R.color.colorBlack));

            profileTV.setTextColor(getColor(R.color.text_color_black));
            saavingsTV.setTextColor(getColor(R.color.text_color_black));
            clientTV.setTextColor(getColor(R.color.text_color_black));
            replaceFragment(SAVINGS_FRAGMENT, null);
        } else if (view.getId() == R.id.profileClickRV) {
            profileTV.setTextColor(colorCode);
            loansTV.setTextColor(getColor(R.color.text_color_black));

            profileIV.setColorFilter(colorCode);
            loansIV.setColorFilter(getColor(R.color.colorBlack));
            reportsIV.setColorFilter(getColor(R.color.colorBlack));
            clientIV.setColorFilter(getColor(R.color.colorBlack));
            savingsIV.setColorFilter(getColor(R.color.colorBlack));

            reportsTV.setTextColor(getColor(R.color.text_color_black));
            saavingsTV.setTextColor(getColor(R.color.text_color_black));
            clientTV.setTextColor(getColor(R.color.text_color_black));
            replaceFragment(ONBOARD_FRAGMENT, null);
        } else if (view.getId() == R.id.adharScannerIV) {
            iOnHeaderItemsClickListener.onHeaderItemClick(SCAN);
        } else if (view.getId() == R.id.img_search) {
            materialSearchView.showSearch(true);
        } else if (view.getId() == R.id.img_more) {
            showPopup(view);
        } else if (view.getId() == R.id.toolbar_back) {
            onBackPressed();
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_options, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_bluetooth:
//                    // Initial the Bluetooth printer setup
//                    if (MyApplication.setup == null) {
//                        try {
//                            Setup setup = new Setup();
//                            setup.blActivateLibrary(DashboardActivity.this, R.raw.licence);
//                            MyApplication.setup = setup;
//                            LocalDatabase.getInstance().setBluetoothSetup(setup);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    private void addFragment(int fragmentName, Object intentExtra) {
        if (CURRENT_FRAGMENT != fragmentName) {
            CURRENT_FRAGMENT = fragmentName;
            switch (fragmentName) {
                case ONBOARD_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    headerTitleTV.setText(R.string.profiles);
                    currentFragment = new OnBoardFragment();
                    addFragmentToContent(currentFragment, "client");
                    break;
                case DASHBOARD_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    headerTitleTV.setText(R.string.dashboard);
                    adharScannerIV.setVisibility(View.GONE);
                    currentFragment = new DashboardFragment();
                    addFragmentToContent(currentFragment, "dashboard");
                    break;
                case LOANS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    currentFragment = new LoansFragment();
                    addFragmentToContent(currentFragment, "Contracts");
                    break;
                case MAP_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    currentFragment = new MapViewFragment();
                    addFragmentToContent(currentFragment, getString(R.string.onboard));
                    break;
                case VAS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    headerTitleTV.setText(getString(R.string.vas));
                    currentFragment = new VasFragment();
                    replaceFragmentToContent(currentFragment, getString(R.string.vas));
                    break;
                case SAVINGS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    headerTitleTV.setText(getString(R.string.collections));
                    currentFragment = new PaymentsFragment();
                    addFragmentToContent(currentFragment, "Reports");
                    break;
                case SHUFPTI_FRAGMENT:

                    Intent intent = new Intent(this, ShufptiVerificationServicesActivity.class);
                    startActivity(intent);

                 /*   currentFragment = new ShufptiVerificationServicesFragment();
                    addFragmentToContent(currentFragment, "Shufpti Pro");*/
                    break;
            }
        }
    }

    private void addFragmentToContent(Fragment fragment, String tagName) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
//                .setCustomAnimations(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right,
//                        R.anim.slide_in_right,
//                        R.anim.slide_out_left)
                .add(R.id.contentFL, fragment, tagName)
                .commit();
    }


    public void clearSearch() {
        query = null;
        if (currentFragment instanceof OnBoardFragment) {
            ((OnBoardFragment) currentFragment).getAllClients();
        } else if (currentFragment instanceof AccountsFragment) {
            ((AccountsFragment) currentFragment).getLoans();
        }
    }

    public void replaceFragment(int fragmentName, Bundle intentExtra) {
        materialSearchView.closeSearch();
        query = null;
        if (CURRENT_FRAGMENT != fragmentName) {
            imgSearch.setVisibility(View.GONE);
            CURRENT_FRAGMENT = fragmentName;
            switch (fragmentName) {
                case ONBOARD_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.VISIBLE);
                    currentFragment = new OnBoardFragment();
                    headerTitleTV.setText(getString(R.string.profiles));
                    replaceFragmentToContent(currentFragment, getString(R.string.profiles));
                    break;
                case DASHBOARD_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.VISIBLE);
                    currentFragment = new DashboardFragment();
                    headerTitleTV.setText(getString(R.string.dashboard));
                    replaceFragmentToContent(currentFragment, "dashboard");
                    break;
                case LOANS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    headerTitleTV.setText(getString(R.string.contracts));
                    imgSearch.setVisibility(View.VISIBLE);
                    currentFragment = new AccountsFragment();
                    replaceFragmentToContent(currentFragment, getString(R.string.contracts));
                    break;
                case LOAN_COLLECTIONS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    currentFragment = new LoanCollectionFragment();
                    currentFragment.setArguments(intentExtra);
                    headerTitleTV.setText(getString(R.string.contracts));
                    replaceFragmentToContent(currentFragment, getString(R.string.contracts));
                    break;
                case MAP_FRAGMENT:
                    headerView.setVisibility(View.GONE);
                    currentFragment = new MapViewFragment();
                    addFragmentToContent(currentFragment, getString(R.string.onboard));
                    break;
                case VAS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    headerTitleTV.setText(getString(R.string.vas));
                    currentFragment = new VasFragment();
                    replaceFragmentToContent(currentFragment, getString(R.string.vas));
                    break;
                case SAVINGS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    imgSearch.setVisibility(View.VISIBLE);
                    headerTitleTV.setText(getString(R.string.payments));
                    currentFragment = new PaymentsFragment();
                    replaceFragmentToContent(currentFragment, "Payments");
                    break;
                case ADD_CLIENT_FRAGMENT:
                    currentFragment = new AddClientFragment();
                    adharScannerIV.setVisibility(View.VISIBLE);
                    currentFragment.setArguments(intentExtra);
                    replaceFragmentToContent(currentFragment, "client");
                    break;
                case SHUFPTI_FRAGMENT:
                    headerView.setVisibility(View.GONE);
                    adharScannerIV.setVisibility(View.GONE);
                    //    currentFragment = new ShufptiVerificationServicesFragment();
                    replaceFragmentToContent(currentFragment, "Shufti Pro");
                    break;
                case SEARCH_PROFILE_LIST_FRAGMENT:
                    headerView.setVisibility(View.GONE);
                    adharScannerIV.setVisibility(View.GONE);
                    currentFragment = new SearchProfilesListFragment();
                    imgSearch.setVisibility(View.VISIBLE);
                    headerTitleTV.setText(getString(R.string.profiles));
                    replaceSearchListFragment(currentFragment, intentExtra);
                    break;
            }
        }
    }

    private void replaceSearchListFragment(Fragment fragment, Bundle data) {
        fragment.setArguments(data);
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
                .replace(R.id.contentFL, fragment, "tagName")
                .commit();
    }

    private void replaceFragmentToContent(Fragment fragment, String tagName) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(fragment.getClass().getSimpleName())
//                .setCustomAnimations(android.R.anim.slide_in_left,
//                        android.R.anim.slide_out_right,
//                        R.anim.slide_in_right,
//                        R.anim.slide_out_left)
                .replace(R.id.contentFL, fragment, tagName)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (materialSearchView.isSearchOpen()) {
            materialSearchView.closeSearch();
            return;
        }
        if (!TextUtils.isEmpty(query)) {
            query = null;
            if (currentFragment instanceof MaterialSearchView.OnQueryTextListener) {
                ((MaterialSearchView.OnQueryTextListener) currentFragment).onQueryTextSubmit("");
            }
        }
        if (CURRENT_FRAGMENT != ONBOARD_FRAGMENT) {
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
            }

            switch (CURRENT_FRAGMENT) {
                case LOAN_COLLECTIONS_FRAGMENT:
                    replaceFragment(LOANS_FRAGMENT, null);
                    break;
                default:
                    replaceFragment(ONBOARD_FRAGMENT, null);
                    break;
            }
        } else {
            /*user needs to click twice to exit the app*/
            if (doubleBackToExitPressedOnce) {
                finish();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.click_back), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @Override
    public void onHeaderUpdate(int fragmentName, String title) {
        headerTitleTV.setText(title);
        switch (fragmentName) {
            case KYC_FRAGMENT:
                adharScannerIV.setVisibility(View.VISIBLE);
                headerView.setVisibility(View.VISIBLE);
                break;
            case PROFILE_DETAILS_FRAGMENT:
                adharScannerIV.setVisibility(View.GONE);
                headerView.setVisibility(View.VISIBLE);
                break;
            case MAP_FRAGMENT:
                adharScannerIV.setVisibility(View.GONE);
                headerView.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onFragmentChanged(int fragmentName, Bundle intentExtra) {
        replaceFragment(fragmentName, intentExtra);
    }

    @Override
    public void setHeaderItemClickListener(IOnHeaderItemsClickListener iOnHeaderItemsClickListener) {
        this.iOnHeaderItemsClickListener = iOnHeaderItemsClickListener;
        if (iOnHeaderItemsClickListener instanceof KycFragment) {
            iOnDataRetrievedFromAdharScannerListener = (IOnDataRetrievedFromAdharScannerListener) iOnHeaderItemsClickListener;
        }
    }

    public void setOnDataRetrievedFromGallery(IOnDataRetrievedFromGallery onDataRetrievedFromGallery) {
        this.onDataRetrievedFromGallery = onDataRetrievedFromGallery;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST && resultCode == Activity.RESULT_OK) {
            requestLocationUpdates();
        } else if (requestCode == IMAGE_PICK_GALLERY && resultCode == Activity.RESULT_OK) {
            onDataRetrievedFromGallery.onDataRetrieved(requestCode, resultCode, data);
        } else if (iOnDataRetrievedFromAdharScannerListener != null && iOnDataRetrievedFromAdharScannerListener instanceof KycFragment) {
            iOnDataRetrievedFromAdharScannerListener.onAdharDataRetrieved(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        removeLocationUpdates();
        PreferenceConnector.writeInteger(DashboardActivity.this, "ListPosition", -1);
        super.onDestroy();
    }

    public void setToolBarBackVisible(boolean toolBarBackVisible) {
        toolBarBack.setVisibility(toolBarBackVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.query = query;
        isSubmitClick = true;
        if (currentFragment instanceof MaterialSearchView.OnQueryTextListener) {
            ((MaterialSearchView.OnQueryTextListener) currentFragment).onQueryTextSubmit(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (currentFragment instanceof OnBoardFragment) {
            materialSearchView.setEnabled(false);
            materialSearchView.setFocusableInTouchMode(false);
            return false;
        } else {
            if (isSubmitClick) {
                isSubmitClick = false;
                return false;
            }
            this.query = newText;
            if (currentFragment instanceof MaterialSearchView.OnQueryTextListener) {
                ((MaterialSearchView.OnQueryTextListener) currentFragment).onQueryTextChange(newText);
            }
        }
        return false;
    }

    @Override
    public void onSearchViewShown() {
        if (!TextUtils.isEmpty(query)) {
            materialSearchView.setQuery(query, false);
        }
        if (currentFragment instanceof MaterialSearchView.SearchViewListener) {
            ((MaterialSearchView.SearchViewListener) currentFragment).onSearchViewShown();
        }
    }

    @Override
    public void onSearchViewClosed() {
        if (currentFragment instanceof MaterialSearchView.SearchViewListener) {
            ((MaterialSearchView.SearchViewListener) currentFragment).onSearchViewClosed();
        }
    }

    public void openSearchProfilesLisFragment(ArrayList<ClientDataDTO> clients, String branch_name, String branch_id) {
        Bundle bundle = new Bundle();
        bundle.putString("branch_name", branch_name);
        bundle.putString("branch_id", branch_id);
        replaceFragment(SEARCH_PROFILE_LIST_FRAGMENT, bundle);
    }
}
