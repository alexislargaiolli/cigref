package com.sherpa.mynelis.cigref.api;

import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface NelisInterface {

    String CLIENT_ID = "9rt01Y6eubJfeg78WTD6fB8yMb2cOgbs";
    String CLIENT_SECRET = "5NDtQ2AzDjjR8208HaI874ZEjL0q17pn";
    String API_ROOT = "sherpa.mynelis.com";
    String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";


    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<AccessToken> getToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret,
                               @Field("grant_type") String grantType, @Field("username") String username,
                               @Field("password") String password);


    @GET("api/v4/eventcampaigns/me/invited")
    Call<ArrayList<CampaignModel>> getInvitations(@Query("access_token") String accessToken);

}
