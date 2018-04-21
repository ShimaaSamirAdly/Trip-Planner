package project.iti.Singleton;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import project.iti.API.APIUrls;
import project.iti.API.ApiEndpointInterface;
import project.iti.Utility.Preferences;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by asmaa on 02/26/2018.
 */

/**
 * singleton class to manage my app and don't create more objects from my connection api
 */
public class ApiUtilities {


    public static ApiUtilities apiUtilities = null;
    private Retrofit retrofit;
    private ApiEndpointInterface apiEndpointInterface;
    private OkHttpClient.Builder httpClient;
    private Retrofit.Builder builder;
    private static Preferences preferencesClass;

    private ApiUtilities() {
        initialization();

    }

    public static synchronized ApiUtilities getInstance() {

        if (apiUtilities == null) {
            apiUtilities = new ApiUtilities();
        }
        return apiUtilities;
    }

    private void initialization() {
        // to cache request

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
//                .cache(cache)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });


        builder = new Retrofit.Builder()
                .baseUrl(APIUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.client(httpClient.build()).build();

        apiEndpointInterface = retrofit.create(ApiEndpointInterface.class);
    }

    public ApiEndpointInterface getApi() {
        if(apiEndpointInterface == null) {
            initialization();
        }
        return apiEndpointInterface;
    }


    public Preferences getRefrences(Context context) {
        if(preferencesClass == null) {
            preferencesClass = new Preferences(context);
        }
        return preferencesClass;
    }

}
