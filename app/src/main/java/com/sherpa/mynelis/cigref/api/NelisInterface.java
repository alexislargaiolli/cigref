package com.sherpa.mynelis.cigref.api;

import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeListModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatusPatch;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface NelisInterface {

    String CLIENT_ID = "9rt01Y6eubJfeg78WTD6fB8yMb2cOgbs";
    String CLIENT_SECRET = "5NDtQ2AzDjjR8208HaI874ZEjL0q17pn";
    //String API_ROOT = "https://sherpa.mynelis.com";
    String API_ROOT = "https://info.cigref.fr";
    String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<AccessToken> getToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret,
                               @Field("grant_type") String grantType, @Field("username") String username,
                               @Field("password") String password);


    @GET("/api/v4/eventcampaigns/me/invited")
    Call<List<CampaignModel>> getMyCampaigns(@Query("access_token") String accessToken);

    @GET("/api/v4/eventcampaigns/me/accepted")
    Call<List<CampaignModel>> getMyAcceptedCampaigns(@Query("access_token") String accessToken);

    @GET("/api/v4/eventcampaigns/{campaignId}/invitations")
    Call<List<Invitation>> getCampaignInvitations(@Path("campaignId") int campaignId, @Query("access_token") String accessToken);

    @GET("/api/v4/eventcampaigns/{campaignId}/invitations/me")
    Call<Invitation> getMyCampaignInvitation(@Path("campaignId") int campaignId, @Query("access_token") String accessToken);

    @GET("/api/v4/lists/44")
    Call<CampaignTypeListModel> getCampaignTypeList(@Query("access_token") String accessToken);

    @PATCH("/api/v4/eventcampaigns/invitations/{invitationId}")
    Call<Invitation> updateInvitationStatus(@Path("invitationId") int invitationId, @Query("access_token") String accessToken, @Body InvitationStatusPatch patch);
}
