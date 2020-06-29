package maps;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.odedtech.mff.mffapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Utilities.PreferenceConnector;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import networking.WebService;
import networking.WebServiceURLs;
import onboard.ClientDataDTO;


public class MapViewFragment extends BaseFragment implements OnMapReadyCallback {

    @BindView(R.id.kycMapIV)
    ImageView kycMapIV;
    @BindView(R.id.disbursalMapIV)
    ImageView disbursalMapIV;
    @BindView(R.id.collectionMapIV)
    ImageView collectionMapIV;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, view);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
            mapFragment.getMapAsync(this);
        } else {
            mapFragment.getMapAsync(this);
        }
        getAllClients();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        LatLng hyderabad = new LatLng(17.387140, 78.491684);
//        mMap.addMarker(new MarkerOptions().position(hyderabad).title("Hyderabad"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(hyderabad));
    }

    @OnClick({R.id.kycMapIV, R.id.disbursalMapIV, R.id.collectionMapIV})
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.kycMapIV:
                kycMapIV.setImageDrawable(getResources().getDrawable(R.drawable.kyc_selected));
                disbursalMapIV.setImageDrawable(getResources().getDrawable(R.drawable.disbursal_map));
                collectionMapIV.setImageDrawable(getResources().getDrawable(R.drawable.collection_map));
                break;
            case R.id.disbursalMapIV:
                disbursalMapIV.setImageDrawable(getResources().getDrawable(R.drawable.disbursal_selected));
                kycMapIV.setImageDrawable(getResources().getDrawable(R.drawable.kyc_map));
                collectionMapIV.setImageDrawable(getResources().getDrawable(R.drawable.collection_map));
                break;
            case R.id.collectionMapIV:
                collectionMapIV.setImageDrawable(getResources().getDrawable(R.drawable.collection_selected));
                kycMapIV.setImageDrawable(getResources().getDrawable(R.drawable.kyc_map));
                disbursalMapIV.setImageDrawable(getResources().getDrawable(R.drawable.disbursal_map));
                break;
        }
    }

    public void getAllClients() {
        String url = PreferenceConnector.readString(getActivity(), "BASE_URL", "") +
                WebServiceURLs.ALL_PROFILES_URL +
                PreferenceConnector.readString(getActivity(), getActivity().getString(R.string.accessToken), "");
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        ArrayList<ClientDataDTO> clients = getAndParseAllClinets(object);
                        for (int index = 0; index < clients.size(); index++) {
                            if (mMap != null) {
                                ClientDataDTO dataDTO = clients.get(index);
                                LatLng latLng = new LatLng(dataDTO.latitude, dataDTO.longitude);
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                                marker.setTag(index);
                            }
                        }

                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                    }
                });
    }


    private ArrayList<ClientDataDTO> getAndParseAllClinets(String object) {
        ArrayList<ClientDataDTO> clientDataDTOS = new ArrayList<>();
        try {
            JSONObject mainJson = new JSONObject(object);
            if (mainJson != null) {
                JSONArray jsonArray = mainJson.getJSONObject("data").getJSONArray("profiles");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientDataDTO clientDataDTO = new ClientDataDTO();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("ProfileID")) {
                        clientDataDTO.profileId = jsonObject.getString("ProfileID");
                    }
                    if (jsonObject.has("profileFormID")) {
                        clientDataDTO.formId = jsonObject.getInt("profileFormID");
                    }
                    if (jsonObject.has("profileDetails")) {
                        JSONObject jsonObj = jsonObject.getJSONObject("profileDetails");
                        if (jsonObj.has("Name")) {
                            clientDataDTO.name = jsonObj.getString("Name");
                        } else if (jsonObj.has("First Name") && jsonObj.has("Last Name")) {
                            clientDataDTO.name = jsonObj.getString("First Name") + " " + jsonObj.getString("Last Name");
                        } else if (jsonObj.has("First Name")) {
                            clientDataDTO.name = jsonObj.getString("First Name");
                        } else if (jsonObj.has("profilePicture")) {
                            clientDataDTO.profilePicture = jsonObj.getString("profilePicture");
                        } else if (jsonObj.has("Latitude")) {
                            clientDataDTO.latitude = jsonObj.getDouble("Latitude");
                        } else if (jsonObj.has("Longitude")) {
                            clientDataDTO.longitude = jsonObj.getDouble("Longitude");
                        }
                        clientDataDTO.kycData = jsonObj.toString();
                    }
                    clientDataDTOS.add(clientDataDTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientDataDTOS;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
