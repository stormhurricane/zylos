package Client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//https://github.com/ventaDevTeam/codeexample-client-server/blob/master/client/src/main/java/de/unidue/example/frontend/RetrofitClient.java
//Stand: 24.05.2021 19:55 Uhr
public class RetrofitClient {

    private static Retrofit testClient = null;

    public static Retrofit getClient(String url){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder().setLenient().create();

        if(testClient == null)
            testClient = new Retrofit.Builder().
                    baseUrl(url).client(client).
                    addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        return testClient;
    }
}