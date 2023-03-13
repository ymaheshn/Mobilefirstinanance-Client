package network;

import base.MFFResponse;
import base.MFFResponseNew;
import dashboard.models.ApplyNewCardResponse;
import dashboard.models.CardDetailsList;
import dashboard.models.DashboardCount;
import dashboard.models.ProfileCount;
import loans.model.BusinessDocumentsModel;
import loans.model.CollectionPortfolioDetailsResponse;
import loans.model.CollectionPortfolioResponse;
import loans.model.DashBoardGraphResponse;
import loans.model.LoansPortfolioResponse;
import loans.model.SavedPortfolioResponse;
import loans.model.TermsDataDTO;
import login.model.EntityResponse;
import login.model.LoginRequest;
import login.model.LoginResponse;
import login.model.MobileNumberLoginRequest;
import login.model.MobileOTPResponse;
import onboard.ClientDataDTO;
import onboard.ProfileDetailsDTO;
import onboard.model.ApplyCardResponse;
import onboard.model.ClientDashboardModel;
import onboard.model.CreditCardResponse;
import onboard.model.LinkProfileDTO;
import onboard.model.LinkProfileStatus;
import onboard.model.ProfileDetailsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import signup.model.Carrier;
import signup.model.Payload;
import signup.model.PayloadVerifyOtp;
import signup.model.VerifyOtpResponse;
import vas.models.BlockCardsPayload;
import vas.models.CardBlockStatusResponse;
import vas.models.CheckBalanceResponse;

public interface MFFApiService {
    @POST("/LoginService/login")
    Call<MFFResponse<LoginResponse>> login(@Body LoginRequest request);

    @POST("/LoginService/login")
    Call<MFFResponse<LoginResponse>> loginMobileNumber(@Body MobileNumberLoginRequest request);

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

    @GET("/PortfolioService/searchPortfolio")
    Call<CollectionPortfolioResponse> searchCollectionUsingHierarchy(@Query("access_token") String accessToken,
                                                                     @Query("group") String name,
                                                                     @Query("pageNumber") int pageNumber,
                                                                     @Query("numberOfRecords") int numberOfRecords);

    @GET("/PortfolioService/searchPortfolio")
    Call<CollectionPortfolioResponse> searchCollectionUsingNationalId(@Query("access_token") String accessToken,
                                                                      @Query("nationalId") String name,
                                                                      @Query("pageNumber") int pageNumber,
                                                                      @Query("numberOfRecords") int numberOfRecords);

    @GET("/PortfolioService/searchPortfolio")
    Call<CollectionPortfolioResponse> searchCollectionUsingIdentifier(@Query("access_token") String accessToken,
                                                                      @Query("Identifier") String name,
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

    @GET("/ProfileService/profile/count")
    Call<MFFResponseNew<ProfileCount>> getTotalClient(@Query("access_token") String accessToken);

    @GET("/WorkflowService/workflows/count")
    Call<MFFResponseNew<DashboardCount>> getOnBoardDetailsCount(@Query("access_token") String accessToken);

    @GET("/PortfolioService/contracts/delinquency/count/{date}")
    Call<DashBoardGraphResponse> getGraphDetails(@Path("date") String date, @Query("access_token") String accessToken);

    @GET("/PortfolioService/bulkReceiveAndDeals")
    //replace with check balance service
    Call<LoansPortfolioResponse> getLoans(@Query("access_token") String accessToken,
                                          @Query("pageNumber") int pageNumber,
                                          @Query("numberOfRecords") int numberOfRecords,
                                          @Query("eventType") String eventType);

    @GET("/PortfolioService/bulkReceiveAndDeals")
    Call<LoansPortfolioResponse> searchLoansUsingName(@Query("access_token") String accessToken,
                                                      @Query("Name") String name,
                                                      @Query("pageNumber") int pageNumber,
                                                      @Query("numberOfRecords") int numberOfRecords,
                                                      @Query("eventType") String eventType);

    @GET("/PortfolioService/bulkReceiveAndDeals")
    Call<LoansPortfolioResponse> searchLoansUsingHierarchy(@Query("access_token") String accessToken,
                                                           @Query("group") String name,
                                                           @Query("pageNumber") int pageNumber,
                                                           @Query("numberOfRecords") int numberOfRecords,
                                                           @Query("eventType") String eventType);

    @GET("/PortfolioService/bulkReceiveAndDeals")
    Call<LoansPortfolioResponse> searchLoansUsingNationalId(@Query("access_token") String accessToken,
                                                            @Query("nationalId") String name,
                                                            @Query("pageNumber") int pageNumber,
                                                            @Query("numberOfRecords") int numberOfRecords,
                                                            @Query("eventType") String eventType);

    @GET("/PortfolioService/bulkReceiveAndDeals")
    Call<LoansPortfolioResponse> searchLoansUsingIdentifier(@Query("access_token") String accessToken,
                                                            @Query("identifier") String name,
                                                            @Query("pageNumber") int pageNumber,
                                                            @Query("numberOfRecords") int numberOfRecords,
                                                            @Query("eventType") String eventType);

    @GET("/ProfileService/files/{profileID}/business")
    Call<BusinessDocumentsModel> getBusinessDocuments(@Path("profileID") String name, @Query("access_token") String accessToken);

    @GET("/ProfileService/searchProfiles?")
    Call<ClientDataDTO> getProfileDetailsByName(@Query("access_token") String accessToken,
                                                  @Query("pageNumber") int pageNumber,
                                                  @Query("numberOfRecords") int numberOfRecords,
                                                  @Query("name") String name);

    @GET("/ProfileService/searchProfiles?")
    Call<ProfileDetailsDTO> getProfileDetailsByHierarchy(@Query("access_token") String accessToken,
                                                         @Query("pageNumber") int pageNumber,
                                                         @Query("numberOfRecords") int numberOfRecords,
                                                         @Query("hierarchy") String name);

    @GET("/ProfileService/searchProfiles?")
    Call<ProfileDetailsDTO> getProfileDetailsByNationalId(@Query("access_token") String accessToken,
                                                          @Query("pageNumber") int pageNumber,
                                                          @Query("numberOfRecords") int numberOfRecords,
                                                          @Query("nationalId") String name);

    @GET("/ProfileService/searchProfiles?")
    Call<ProfileDetailsDTO> getProfileDetailsByIdentifier(@Query("access_token") String accessToken,
                                                          @Query("pageNumber") int pageNumber,
                                                          @Query("numberOfRecords") int numberOfRecords,
                                                          @Query("identifier") String name);

    @GET("/PortfolioService/getcontractsBycontractUUID?")
    Call<TermsDataDTO> getTermsDetails(@Query("contractUUID") String contractUUID,
                                       @Query("access_token") String accessToken,
                                       @Query("pageNumber") int pageNumber,
                                       @Query("numberOfRecords") int numberOfRecords);

    @POST("/ThirdpartyService/twilio/sendOtp")
    Call<Carrier> setPayloadEmail(@Body Payload payload);

    @POST("/ThirdpartyService/twilio/verifyOtp")
    Call<VerifyOtpResponse> setPayloadVerifyEmail(@Body PayloadVerifyOtp payload);

    @POST("/ThirdpartyService/twilio/sendOtp")
    Call<Carrier> setPayloadMobile(@Body Payload payload);

    @POST("/ThirdpartyService/twilio/verifyOtp")
    Call<VerifyOtpResponse> setPayloadVerifyMobile(@Body PayloadVerifyOtp payload);

    @GET("/WorkflowService/workflows?")
    Call<ClientDashboardModel> getDashBoardDetails(@Query("access_token") String accessToken);

    @POST("/WorkflowService/workflowToProfiles?")
    Call<LinkProfileStatus> getLinkClient(@Body LinkProfileDTO likProfile, @Query("access_token") String accessToken);

    @GET("/LoginService/verify/phoneNumber")
    Call<MobileOTPResponse> sendOtpToUser(@Query("phoneNumber") String phoneNumber);

    @GET("/ThirdpartyService/cards")
    Call<CreditCardResponse> getCardDetails(@Query("status") String active,@Query("access_token") String accessToken);

    @GET("/PortfolioService/savingContracts")
    Call<ApplyNewCardResponse> getLinkCardDetails(@Query("linkedProfileId") String linkedProfileId, @Query("access_token") String accessToken);

    @POST("/ThirdpartyService/applyCard")
    Call<ApplyCardResponse> applyCard(@Query("access_token") String accessToken, @Body CardDetailsList cardDetail);

    @GET("/ProfileService/profiles")
    Call<ProfileDetailsResponse> getLinkedProfileId(@Query("pageNumber") int pageNumber,@Query("numberOfRecords") int numberOfRecords,@Query("access_token") String accessToken);

    @PUT("/ThirdpartyService/cardStatus")
    Call<CardBlockStatusResponse> blockCards(@Query("access_token") String accessToken, @Body BlockCardsPayload blockCardsPayload);

    @GET("/PortfolioService/Contracts")
    Call<CheckBalanceResponse> checkBalanceApi(@Query("linkedProfileId") String linkedProfileId, @Query("access_token") String accessToken);


}