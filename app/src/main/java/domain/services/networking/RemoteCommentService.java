package domain.services.networking;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.odedtech.mff.client.R;

import java.io.IOException;
import java.util.HashMap;

import Utilities.PreferenceConnector;
import loans.model.LoanCollection;
import networking.WebServiceURLs;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RemoteCommentService {

    private static RemoteCommentService instance;

    private final Retrofit retrofit;

    public RemoteCommentService() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(WebServiceURLs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RemoteCommentService getInstance() {
        if (instance == null) {
            instance = new RemoteCommentService();
        }
        return instance;
    }

    public void addComment(Context context, LoanCollection comment) throws IOException, RemoteException {
        RemoteCommentEndpoint service = retrofit.create(RemoteCommentEndpoint.class);
        HashMap<String, String> params = new HashMap<>();

        params.put("amount", "" + comment.getLoanAmount());
        params.put("contractCode", "" + comment.getContractCode());

        Response<LoanCollection> response = service.saveContractData(PreferenceConnector.readString(context, context.getString(R.string.accessToken), ""),
                params).execute();

        if (response == null || !response.isSuccessful() || response.errorBody() != null) {
            throw new RemoteException(response);
        }
    }
}
