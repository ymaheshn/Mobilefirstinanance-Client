package network;

import base.MFFResponse;
import base.MFFResponseNew;
import loans.model.CollectionPortfolioDetailsResponse;
import loans.model.CollectionPortfolioResponse;
import loans.model.SavedPortfolioResponse;
import login.model.EntityResponse;
import login.model.LoginRequest;
import login.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MFFApiService {
    @POST("/LoginService/login")
    Call<MFFResponse<LoginResponse>> login(@Body LoginRequest request);

    @GET("/UserService/users/root")
    Call<MFFResponseNew<EntityResponse>> getEntityDetails(@Query("access_token") String accessToken);


    @GET("/PortfolioService/searchPortfolio")
    Call<CollectionPortfolioResponse> getCollections(@Query("access_token") String accessToken,
                                                     @Query("pageNumber") int pageNumber,
                                                     @Query("numberOfRecords") int numberOfRecords);

    @GET("/PortfolioService/searchPortfolio")
    Call<CollectionPortfolioResponse> searchCollectionUsingName(@Query("access_token") String accessToken,
                                                                @Query("Name") String name,
                                                                @Query("pageNumber") int pageNumber,
                                                                @Query("numberOfRecords") int numberOfRecords);


    @GET("/PortfolioService/geteventsBycontractUUID")
    Call<CollectionPortfolioDetailsResponse> getPortfolioCollectionDetails(@Query("access_token") String accessToken,
                                                                           @Query("contractUUID") String contractUUID,
                                                                           @Query("eventType") String eventType);

    @Headers({
            "Content-Type: application/json",
    })
    @POST("/PortfolioService/savePortfolioRepayment")
    Call<SavedPortfolioResponse> savePortfolio(@Query("access_token") String accessToken, @Body String array);
}