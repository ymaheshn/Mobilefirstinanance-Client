package interfaces;

import retrofit2.Response;

public interface ApiCallResponseListener {

    void onSuccess(Response responseData);

    void onFailure(String error);
}
