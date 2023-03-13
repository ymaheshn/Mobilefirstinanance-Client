package network;

import com.odedtech.mff.client.BuildConfig;

import networking.UnsafeOkHttpClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MFFApiWrapper {

    public MFFApiService service;

    private MFFApiWrapper() {
        initRetrofit();
    }

    private static MFFApiWrapper instance;

    public static MFFApiWrapper getInstance() {
        if (instance == null) {
            instance = new MFFApiWrapper();
        }
        return instance;
    }

    private void initRetrofit() {
        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MFFApiService.class);
    }
}
