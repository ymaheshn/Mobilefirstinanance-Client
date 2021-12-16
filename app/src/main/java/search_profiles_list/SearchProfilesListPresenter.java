package search_profiles_list;

import Utilities.PreferenceConnector;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.odedtech.mff.mffapp.R;
import networking.WebService;
import networking.WebServiceURLs;
import onboard.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class SearchProfilesListPresenter {
    private Context context;
    private IOnBoardFragmentCallback iOnBoardFragmentCallback;

    public SearchProfilesListPresenter(Context context, IOnBoardFragmentCallback iOnBoardFragmentCallback) {
        this.context = context;
        this.iOnBoardFragmentCallback = iOnBoardFragmentCallback;
    }

    public void getAllClients(String branch_id, int pageNumber, boolean clearAll) {
        if (pageNumber == 0) {
            iOnBoardFragmentCallback.showProgressBar();
        }
        String url = WebServiceURLs.BASE_URL +
                WebServiceURLs.PROFILES_SEARCH +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "")
                + "&pageNumber="+pageNumber+"&numberOfRecords=10&Hierarchy=" + branch_id;
        WebService.getInstance().apiGetRequestCallJSON(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        iOnBoardFragmentCallback.hideProgressBar();
                        ArrayList<ClientDataDTO> clients = getAndParseAllClients(object);
                        iOnBoardFragmentCallback.loadRecyclerView(clients, false, clearAll);

                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        iOnBoardFragmentCallback.hideProgressBar();
                        if (errorMessage.contains("AuthFailureError")) {
                            iOnBoardFragmentCallback.showLogoutAlert();
                        } else {
                            iOnBoardFragmentCallback.showMessage(errorMessage);
                        }
                    }
                });
    }


    public void getSearch(String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            return;
        }
        iOnBoardFragmentCallback.showProgressBar();
        String url = WebServiceURLs.BASE_URL +
                WebServiceURLs.SEARCH_PROFILES_URL +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        url = url.replaceAll("NAME", "" + searchQuery);
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        iOnBoardFragmentCallback.hideProgressBar();
                        ArrayList<ClientDataDTO> clients = getParseSearchResults(object);
                        iOnBoardFragmentCallback.loadRecyclerView(clients, true, true);
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        iOnBoardFragmentCallback.hideProgressBar();
                        if (errorMessage.contains("AuthFailureError")) {
                            iOnBoardFragmentCallback.showLogoutAlert();
                        } else {
                            iOnBoardFragmentCallback.showMessage(errorMessage);
                        }
                    }
                });
    }


    private ArrayList<ClientDataDTO> getAndParseAllClients(String object) {
        ArrayList<ClientDataDTO> clientDataDTOS = new ArrayList<>();
        try {
            JSONObject mainJson = new JSONObject(object);
            JSONArray jsonArray = mainJson.getJSONObject("data").getJSONArray("profiles");
            if (jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientDataDTO clientDataDTO = new ClientDataDTO();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientDataDTOS;
    }


    private ArrayList<ClientDataDTO> getParseSearchResults(String object) {
        ArrayList<ClientDataDTO> clientDataDTOS = new ArrayList<>();
        try {
            JSONArray mainJson = new JSONArray(object);
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


    public void getIsLinkedStatusAPI(String profileId) {
        iOnBoardFragmentCallback.showProgressBar();
        String updatedStatusUrl = WebServiceURLs.PROFILE_LINK_STATUS_URL.replace("PROFILE_ID", profileId);
        String url = WebServiceURLs.BASE_URL +
                updatedStatusUrl +
                PreferenceConnector.readString(context, context.getString(R.string.accessToken), "");
        WebService.getInstance().apiGetRequestCall(url,
                new WebService.OnServiceResponseListener() {
                    @Override
                    public void onApiCallResponseSuccess(String url, String object) {
                        iOnBoardFragmentCallback.hideProgressBar();
                        try {
                            Object json = new JSONTokener(object).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            Object dataJSON = jsonObject.getJSONObject("data").get("workflow");
                            if (dataJSON instanceof JSONObject) {
                                JSONObject workflowJson = (JSONObject) dataJSON;
                                if (workflowJson.has("isLinked") && workflowJson.getString("isLinked").equals("false")) {
                                    iOnBoardFragmentCallback.profileLinkStatusFromApi(false, null);
                                } else {
                                    WorkFlowTemplateDto workFlowTemplateDto = getParseTabsData(dataJSON.toString());
                                    iOnBoardFragmentCallback.profileLinkStatusFromApi(true, workFlowTemplateDto);
                                }
                            } else if (dataJSON instanceof JSONArray) {
                                iOnBoardFragmentCallback.profileLinkStatusFromApi(false, null);
                            } else {
                                iOnBoardFragmentCallback.showMessage("Something went wrong, please try again after sometime.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onApiCallResponseFailure(String errorMessage) {
                        iOnBoardFragmentCallback.hideProgressBar();
                        if (errorMessage.contains("AuthFailureError")) {
                            iOnBoardFragmentCallback.showLogoutAlert();
                        } else {
                            iOnBoardFragmentCallback.showMessage(errorMessage);
                        }

                    }
                });
    }

    private WorkFlowTemplateDto getParseTabsData(String object) {
        WorkFlowTemplateDto workFlowTemplateDto = new WorkFlowTemplateDto();
        try {
            JSONObject jsonObject = new JSONObject(object);
            if (jsonObject.has("workFlowID")) {
                workFlowTemplateDto.workFlowId = jsonObject.getString("workFlowID");
            }
            if (jsonObject.has("workFlowName")) {
                workFlowTemplateDto.workFlowName = jsonObject.getString("workFlowName");
            }
            if (jsonObject.has("workflowProfileID")) {
                workFlowTemplateDto.workFlowProfileId = jsonObject.getString("workflowProfileID");
            }

            if (jsonObject.has("workFlowTemplateDetails")) {
                JSONArray jsonArray = jsonObject.getJSONArray("workFlowTemplateDetails");
                if (jsonArray != null && jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TabDto tabDto = new TabDto();
                        JSONObject jsonObjectTab = jsonArray.getJSONObject(i);
                        if (jsonObjectTab.has("Id")) {
                            tabDto.tabId = jsonObjectTab.getInt("Id");
                        }
                        if (jsonObjectTab.has("Label")) {
                            tabDto.tabName = jsonObjectTab.getString("Label");

                        }
                        if (jsonObjectTab.has("Form Header")) {
                            JSONObject jsonFileds = jsonObjectTab.getJSONObject("Form Header");
                            if (jsonFileds.has("Form Header")) {
                                JSONArray jsonFieldsArray = jsonFileds.getJSONArray("Form Header");
                                if (jsonFieldsArray != null && jsonFieldsArray.length() > 0) {
                                    for (int j = 0; j < jsonFieldsArray.length(); j++) {
                                        TabFields tabFields = new TabFields();
                                        JSONObject josnFiled = jsonFieldsArray.getJSONObject(j);
                                        if (josnFiled.has("Label")) {
                                            tabFields.name = josnFiled.getString("Label");
                                        }
                                        if (josnFiled.has("Type")) {
                                            tabFields.type = josnFiled.getString("Type");
                                        }
                                        if (josnFiled.has("Value")) {
                                            if (josnFiled.get("Value") instanceof String) {
                                                tabFields.value = josnFiled.getString("Value");
                                            } else {
                                                JSONArray jsonValues = josnFiled.getJSONArray("Value");
                                                if (jsonValues != null && jsonValues.length() > 0) {
                                                    for (int k = 0; k < jsonValues.length(); k++) {
                                                        tabFields.valuesList.add(jsonValues.getString(k));
                                                    }
                                                }
                                            }
                                        }
                                        tabDto.tabFieldsArrayList.add(tabFields);
                                    }
                                }
                            }
                            if (jsonFileds.has("Form Body")) {
                                JSONArray jsonFieldsArray = jsonFileds.getJSONArray("Form Body");
                                if (jsonFieldsArray != null && jsonFieldsArray.length() > 0) {
                                    for (int j = 0; j < jsonFieldsArray.length(); j++) {
                                        TabFields tabFields = new TabFields();
                                        JSONObject josnFiled = jsonFieldsArray.getJSONObject(j);
                                        if (josnFiled.has("Label")) {
                                            tabFields.name = josnFiled.getString("Label");
                                        }
                                        if (josnFiled.has("Type")) {
                                            tabFields.type = josnFiled.getString("Type");
                                        }
                                        if (josnFiled.has("Value")) {
                                            if (josnFiled.get("Value") instanceof String) {
                                                tabFields.value = josnFiled.getString("Value");
                                            } else {
                                                JSONArray jsonValues = josnFiled.getJSONArray("Value");
                                                if (jsonValues != null && jsonValues.length() > 0) {
                                                    for (int k = 0; k < jsonValues.length(); k++) {
                                                        tabFields.valuesList.add(jsonValues.getString(k));
                                                    }
                                                }
                                            }
                                        }
                                        tabDto.tabFieldsBodyArrayList.add(tabFields);
                                    }
                                }
                            }
                        }
                        workFlowTemplateDto.tabDtoArrayList.add(tabDto);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return workFlowTemplateDto;
    }
}