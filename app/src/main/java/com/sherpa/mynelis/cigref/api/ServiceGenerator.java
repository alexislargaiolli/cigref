package com.sherpa.mynelis.cigref.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherpa.mynelis.cigref.service.AuthenticationInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alexis Largaiolli on 19/10/17.
 */
public class ServiceGenerator {

    private static Gson gson = new GsonBuilder()
            .setDateFormat(NelisInterface.DATE_FORMAT)
            .create();

    private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(NelisInterface.API_ROOT).addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    private static AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor();

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static <S> S createService(Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            httpClient.addInterceptor(authInterceptor);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    private static NelisInterface nelisClient;

    public static NelisInterface createNelisClient(){
        if(nelisClient == null){
            nelisClient = createService(NelisInterface.class);
        }
        return nelisClient;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }
}