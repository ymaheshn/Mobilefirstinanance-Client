package domain.services.networking;

import java.util.HashMap;

import loans.model.LoanCollection;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface RemoteCommentEndpoint {
    @FormUrlEncoded
    @POST("/mff/api/savePortfolioRepayment")
    Call<LoanCollection> saveContractData(@Query("access_token") String accessToken, @FieldMap HashMap<String, String> body);
}
