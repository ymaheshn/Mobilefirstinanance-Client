package dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odedtech.mff.mff_android.EmptyFragment;
import com.odedtech.mff.mffapp.R;

import javax.inject.Inject;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import addclient.AddClientFragment;
import base.BaseActivity;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import database.db.DBOperations;
import domain.AddLoanCollectionUseCase;
import domain.GetLoanCollectionsUseCase;
import interfaces.IOnDataRetrievedFromAdharScannerListener;
import interfaces.IOnDataRetrievedFromGallery;
import interfaces.IOnFragmentChangeListener;
import interfaces.IOnHeaderItemsClickListener;
import kyc.KycFragment;
import loans.LoanCollectionFragment;
import loans.LoansFragment;
import maps.MapViewFragment;
import networking.MyApplication;
import onboard.OnBoardFragment;
import pub.devrel.easypermissions.EasyPermissions;
import services.LocationUpdatesBroadcastReceiver;
import vas.VasFragment;

import static Utilities.Constants.ADD_CLIENT_FRAGMENT;
import static Utilities.Constants.CURRENT_FRAGMENT;
import static Utilities.Constants.KYC_FRAGMENT;
import static Utilities.Constants.LOANS_FRAGMENT;
import static Utilities.Constants.LOAN_COLLECTIONS_FRAGMENT;
import static Utilities.Constants.MAP_FRAGMENT;
import static Utilities.Constants.ONBOARD_FRAGMENT;
import static Utilities.Constants.PROFILE_DETAILS_FRAGMENT;
import static Utilities.Constants.SAVINGS_FRAGMENT;
import static Utilities.Constants.SCAN;
import static Utilities.Constants.VAS_FRAGMENT;

public class DashboardActivity extends BaseActivity implements IOnFragmentChangeListener, MaterialSearchView.OnQueryTextListener, MaterialSearchView.SearchViewListener {
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
    @BindView(R.id.googleMapView)
    ImageView googleMapView;
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


    boolean doubleBackToExitPressedOnce = false;
    private IOnHeaderItemsClickListener iOnHeaderItemsClickListener;
    private IOnDataRetrievedFromAdharScannerListener iOnDataRetrievedFromAdharScannerListener;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private IOnDataRetrievedFromGallery onDataRetrievedFromGallery;
    private BaseFragment currentFrament;
    private String query;
    private boolean isSubmitClick;

    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        // bind the view using butterknife
        ButterKnife.bind(this);
        DBOperations.getInstance(this);
        CURRENT_FRAGMENT = -1;
        addFragment(MAP_FRAGMENT, "");

        createLocationRequest();
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            checkLocationSetting();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationilze),
                    RC_LOCATION, perms);
        }

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
            mFusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void requestLocationUpdates() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            MyApplication.getInstance().setCurrentLocation(location);
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @OnClick({R.id.clientClickRV, R.id.loansClickRV, R.id.savingsClickRV, R.id.reportsClickRV,
            R.id.googleMapView, R.id.adharScannerIV, R.id.img_search, R.id.img_more, R.id.toolbar_back})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.clientClickRV:
                replaceFragment(ONBOARD_FRAGMENT, null);
                break;
            case R.id.loansClickRV:
                replaceFragment(LOANS_FRAGMENT, null);
                break;
            case R.id.savingsClickRV:
                replaceFragment(VAS_FRAGMENT, null);
                break;
            case R.id.reportsClickRV:
                replaceFragment(SAVINGS_FRAGMENT, null);
                break;
            case R.id.googleMapView:
                replaceFragment(MAP_FRAGMENT, null);
                break;
            case R.id.adharScannerIV:
                iOnHeaderItemsClickListener.onHeaderItemClick(SCAN);
                break;
            case R.id.img_search:
                materialSearchView.showSearch(true);
                break;
            case R.id.img_more:
                showPopup(view);
                break;
            case R.id.toolbar_back:
                onBackPressed();
                break;
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
                    currentFrament = new OnBoardFragment();
                    addFragmentToContent(currentFrament, "client");
                    break;
                case LOANS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    currentFrament = new LoansFragment();
                    addFragmentToContent(currentFrament, "Contracts");
                    break;
                case MAP_FRAGMENT:
                    headerView.setVisibility(View.GONE);
                    currentFrament = new MapViewFragment();
                    addFragmentToContent(currentFrament, getString(R.string.onboard));
                    break;
                case VAS_FRAGMENT:
                    currentFrament = new EmptyFragment();
                    addFragmentToContent(currentFrament, "Savings");
                    break;
                case SAVINGS_FRAGMENT:
                    currentFrament = new EmptyFragment();
                    addFragmentToContent(currentFrament, "Reports");
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
        if (currentFrament instanceof OnBoardFragment) {
            ((OnBoardFragment) currentFrament).getAllClients();
        }
    }

    private void replaceFragment(int fragmentName, Bundle intentExtra) {
        materialSearchView.closeSearch();
        query = null;
        if (CURRENT_FRAGMENT != fragmentName) {
            imgSearch.setVisibility(View.GONE);
            CURRENT_FRAGMENT = fragmentName;
            switch (fragmentName) {
                case ONBOARD_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    currentFrament = new OnBoardFragment();
                    imgSearch.setVisibility(View.VISIBLE);
                    replaceFragmentToContent(currentFrament, getString(R.string.onboard));
                    break;
                case LOANS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    headerTitleTV.setText(getString(R.string.contracts));
                    // imgSearch.setVisibility(View.VISIBLE);
                    currentFrament = new LoansFragment();
                    replaceFragmentToContent(currentFrament, getString(R.string.contracts));
                    break;
                case LOAN_COLLECTIONS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    currentFrament = new LoanCollectionFragment();
                    currentFrament.setArguments(intentExtra);
                    headerTitleTV.setText(getString(R.string.contracts));
                    replaceFragmentToContent(currentFrament, getString(R.string.contracts));
                    break;
                case MAP_FRAGMENT:
                    headerView.setVisibility(View.GONE);
                    currentFrament = new MapViewFragment();
                    addFragmentToContent(currentFrament, getString(R.string.onboard));
                    break;
                case VAS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    headerTitleTV.setText(getString(R.string.vas));
                    currentFrament = new VasFragment();
                    replaceFragmentToContent(currentFrament, getString(R.string.vas));
                    break;
                case SAVINGS_FRAGMENT:
                    headerView.setVisibility(View.VISIBLE);
                    adharScannerIV.setVisibility(View.GONE);
                    currentFrament = new EmptyFragment();
                    headerTitleTV.setText(getString(R.string.savings));
                    replaceFragmentToContent(currentFrament, getString(R.string.savings));
                    break;
                case ADD_CLIENT_FRAGMENT:
                    currentFrament = new AddClientFragment();
                    adharScannerIV.setVisibility(View.VISIBLE);
                    currentFrament.setArguments(intentExtra);
                    replaceFragmentToContent(currentFrament, "client");
                    break;
            }
        }
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
            if (currentFrament instanceof MaterialSearchView.OnQueryTextListener) {
                ((MaterialSearchView.OnQueryTextListener) currentFrament).onQueryTextSubmit("");
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
                case ADD_CLIENT_FRAGMENT:
                    replaceFragment(ONBOARD_FRAGMENT, null);
                    break;
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
        if (currentFrament instanceof MaterialSearchView.OnQueryTextListener) {
            ((MaterialSearchView.OnQueryTextListener) currentFrament).onQueryTextSubmit(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (isSubmitClick) {
            isSubmitClick = false;
            return false;
        }
        this.query = newText;
        if (currentFrament instanceof MaterialSearchView.OnQueryTextListener) {
            ((MaterialSearchView.OnQueryTextListener) currentFrament).onQueryTextChange(newText);
        }
        return false;
    }

    @Override
    public void onSearchViewShown() {
        if (!TextUtils.isEmpty(query)) {
            materialSearchView.setQuery(query, false);
        }
        if (currentFrament instanceof MaterialSearchView.SearchViewListener) {
            ((MaterialSearchView.SearchViewListener) currentFrament).onSearchViewShown();
        }
    }

    @Override
    public void onSearchViewClosed() {
        if (currentFrament instanceof MaterialSearchView.SearchViewListener) {
            ((MaterialSearchView.SearchViewListener) currentFrament).onSearchViewClosed();
        }
    }
}
