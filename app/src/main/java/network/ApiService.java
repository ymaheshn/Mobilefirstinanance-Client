package network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import interfaces.ApiCallResponseListener;
import onboard.model.ProfileDetailsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vas.models.BlockCardsPayload;
import vas.models.CardBlockStatusResponse;
import vas.models.CheckBalanceResponse;

public class ApiService {

    MFFApiWrapper retrofitApi;
    MFFApiService apiInterface;
    private ApiCallResponseListener mainClass;
    Context mContext;

    public ApiService(ApiCallResponseListener mClass, Context ctx) {

        retrofitApi = MFFApiWrapper.getInstance();
        apiInterface = retrofitApi.service;
        mainClass = mClass;
        mContext = ctx;
    }

    public void blockCards(Context context, String token, BlockCardsPayload blockCardsPayload) {
        Call<CardBlockStatusResponse> call = apiInterface.blockCards(token, blockCardsPayload);
        call.enqueue(new Callback<CardBlockStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<CardBlockStatusResponse> call, @NonNull Response<CardBlockStatusResponse> response) {
                if (response.isSuccessful()) {
                    mainClass.onSuccess(response);
                } else {
                    try {
                        Gson gson = new Gson();
                        MyErrorMessage message = gson.fromJson(response.errorBody() != null ? response.errorBody().charStream() : null, MyErrorMessage.class);
                        mainClass.onFailure(message.getMessage());

                    } catch (Exception e) {
                        mainClass.onFailure(e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CardBlockStatusResponse> call, @NonNull Throwable t) {
                mainClass.onFailure("Error while getting prescription notifications data " + t.getMessage());
            }
        });
    }

    public void checkBalance(String token, String linkedProfileId) {
        Call<CheckBalanceResponse> call = apiInterface.checkBalanceApi(token, linkedProfileId);
        call.enqueue(new Callback<CheckBalanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckBalanceResponse> call, @NonNull Response<CheckBalanceResponse> response) {
                if (response.isSuccessful()) {
                    mainClass.onSuccess(response);
                } else {
                    try {
                        Gson gson = new Gson();
                        MyErrorMessage message = gson.fromJson(response.errorBody() != null ? response.errorBody().charStream() : null, MyErrorMessage.class);
                        mainClass.onFailure(message.getMessage());

                    } catch (Exception e) {
                        mainClass.onFailure(e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CheckBalanceResponse> call, @NonNull Throwable t) {
                mainClass.onFailure("Error while getting prescription notifications data " + t.getMessage());
            }
        });

    }

    public void getLinkedProfileId(int pageNumber, int numberOfRecords, String token) {
        Call<ProfileDetailsResponse> call = apiInterface.getLinkedProfileId(pageNumber, numberOfRecords, token);
        call.enqueue(new Callback<ProfileDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileDetailsResponse> call, @NonNull Response<ProfileDetailsResponse> response) {
                if (response.isSuccessful()) {
                    mainClass.onSuccess(response);
                } else {
                    try {
                        Gson gson = new Gson();
                        MyErrorMessage message = gson.fromJson(response.errorBody() != null ? response.errorBody().charStream() : null, MyErrorMessage.class);
                        mainClass.onFailure(message.getMessage());

                    } catch (Exception e) {
                        mainClass.onFailure(e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileDetailsResponse> call, @NonNull Throwable t) {
                mainClass.onFailure("Error while getting prescription notifications data " + t.getMessage());
            }
        });
    }

}
