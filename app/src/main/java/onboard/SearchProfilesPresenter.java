package onboard;

import Utilities.PreferenceConnector;
import Utilities.UtilityMethods;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.odedtech.mff.mffapp.R;
import loans.model.SearchData;
import networking.WebService;
import networking.WebServiceURLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class SearchProfilesPresenter {

    private Context context;
    private SearchProfilesFragmentCallbacks searchProfilesFragmentCallbacks;

    public SearchProfilesPresenter(Context context, SearchProfilesFragmentCallbacks searchProfilesFragmentCallbacks) {
        this.context = context;
        this.searchProfilesFragmentCallbacks = searchProfilesFragmentCallbacks;
    }

    public void searchProfiles(String search_text) {
        searchProfilesFragmentCallbacks.showProgressBar();
        String url = WebServiceURLs.BASE_URL +
                WebServiceURLs.HIERARCHY_SEARCH +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "")
                + "&branchName=" + search_text;

        WebService.getInstance().apiGetRequestCall(url, new WebService.OnServiceResponseListener() {

            @Override
            public void onApiCallResponseSuccess(String url, String object) {
                if (!TextUtils.isEmpty(object)) {
                    searchProfilesFragmentCallbacks.hideProgressBar();
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        JSONArray searchArray = jsonObject.getJSONObject("data").getJSONArray("portfolio");
                        List<SearchData> listData = new ArrayList<>();
                        if (searchArray != null) {
                            for (int i = 0; i < searchArray.length(); i++) {
                                SearchData objData = new SearchData();
                                objData.branchid = searchArray.getJSONObject(i).getString("branchid");
                                objData.branch_name = searchArray.getJSONObject(i).getString("branch_name");
                                listData.add(objData);
                            }
                        }
                        searchProfilesFragmentCallbacks.loadRecyclerView(listData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onApiCallResponseFailure(String errorMessage) {
                searchProfilesFragmentCallbacks.showProgressBar();
                searchProfilesFragmentCallbacks.showMessage(errorMessage);
            }
        });

    }

    private ArrayList<ClientDataDTO> getParseSearchResults(String object) {
        ArrayList<ClientDataDTO> clientDataDTOS = new ArrayList<>();
        try {
            JSONObject mObject = new JSONObject(object);
            JSONObject obj = mObject.getJSONObject("data");
            JSONArray mainJson = obj.getJSONArray("profiles");
            if (mainJson != null) {
                for (int i = 0; i < mainJson.length(); i++) {
                    ClientDataDTO clientDataDTO = new ClientDataDTO();
                    JSONObject jsonObject = mainJson.getJSONObject(i);
                    if (jsonObject.has("profileID")) {
                        clientDataDTO.profileId = jsonObject.getString("profileID");
                    }
                    if (jsonObject.has("profileFormID")) {
                        clientDataDTO.formId = jsonObject.getInt("profileFormID");
                    }

                    if (jsonObject.has("status")) {
                        clientDataDTO.status = jsonObject.getString("status");
                    }

                    if (jsonObject.has("identifier")) {
                        clientDataDTO.identifier = jsonObject.getString("identifier");
                    }

                    if (jsonObject.has("profileDetails")) {
                        JSONObject jsonObj = jsonObject.getJSONObject("profileDetails");
                        if (jsonObj.has("Name")) {
                            clientDataDTO.name = jsonObj.getString("Name");
                        } else if (jsonObj.has("First Name") && jsonObj.has("Last Name")) {
                            clientDataDTO.name = jsonObj.getString("First Name") + " " + jsonObj.getString("Last Name");
                        } else if (jsonObj.has("First Name")) {
                            clientDataDTO.name = jsonObj.getString("First Name");
                        }
                        if (jsonObj.has("profilePicture")) {
                            clientDataDTO.profilePicture = jsonObj.getString("profilePicture");
                        }
                        if (jsonObj.has("formLabel")) {
                            clientDataDTO.formLabel = jsonObj.getString("formLabel");
                        }
                        clientDataDTO.kycData = jsonObj.toString();
                    }
                    clientDataDTOS.add(clientDataDTO);
                }

                Log.i("RESULT", "RESULT :" + clientDataDTOS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientDataDTOS;
    }

    public void getProfileData(String branch_id) {
        searchProfilesFragmentCallbacks.showProgressBar();
        String url = WebServiceURLs.BASE_URL +
                WebServiceURLs.PROFILES_SEARCH +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "")
                + "&pageNumber=0&numberOfRecords=10&Hierarchy=" + branch_id;
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        searchProfilesFragmentCallbacks.hideProgressBar();
                        ArrayList<ClientDataDTO> clients = getParseSearchResults(object);
                        searchProfilesFragmentCallbacks.loadProfilesRecyclerView(clients);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        searchProfilesFragmentCallbacks.hideProgressBar();
                        if (errorMessage.contains("AuthFailureError")) {
                            searchProfilesFragmentCallbacks.showLogoutAlert();
                        } else {
                            searchProfilesFragmentCallbacks.showMessage(errorMessage);
                        }
                    }
                });
    }
}
