package com.sherpa.mynelis.cigref.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String endpoint, boolean newInstance) throws Exception {
        if(newInstance || retrofit == null) {
            try {
                String url = "";
                if(endpoint.contains("http"))
                    url = endpoint;
                else url = "http://" + endpoint;

                url = url.trim();
                retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            catch(Exception ex) {
                throw new Exception();
            }
        }
        return retrofit;
    }
}