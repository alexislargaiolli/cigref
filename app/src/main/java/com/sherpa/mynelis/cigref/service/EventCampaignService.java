package com.sherpa.mynelis.cigref.service;

import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.api.ServiceGenerator;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatusPatch;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexis Largaiolli on 19/10/17.
 */

public class EventCampaignService {

    /**
     * Query the list of campaign i'm invited in
      * @param responseEvent
     */
    public static void getMyCampaigns(final ServiceResponse<ArrayList<CampaignModel>> responseEvent) {
        System.out.println("getMyCampaigns");
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<ArrayList<CampaignModel>> campaigns = client.getMyCampaigns(AuthenticationService.getmToken().getAccessToken());
        campaigns.enqueue(new Callback<ArrayList<CampaignModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CampaignModel>> call, Response<ArrayList<CampaignModel>> response) {
                responseEvent.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<CampaignModel>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    /**
     * Query the list of campaign where i accepted invitation
     * @param responseEvent
     */
    public static void getMyAcceptedCampaigns(final ServiceResponse<ArrayList<CampaignModel>> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<ArrayList<CampaignModel>> campaigns = client.getMyAcceptedCampaigns(AuthenticationService.getmToken().getAccessToken());
        campaigns.enqueue(new Callback<ArrayList<CampaignModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CampaignModel>> call, Response<ArrayList<CampaignModel>> response) {
                responseEvent.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<CampaignModel>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    /**
     * Query the list of invitation of a campaign
     * @param campaignId id of the campaign
     * @param responseEvent
     */
    public static void getCampaignInvitations(int campaignId, final ServiceResponse<ArrayList<Invitation>> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<ArrayList<Invitation>> invitations = client.getCampaignInvitations(campaignId, AuthenticationService.getmToken().getAccessToken());
        invitations.enqueue(new Callback<ArrayList<Invitation>>() {
            @Override
            public void onResponse(Call<ArrayList<Invitation>> call, Response<ArrayList<Invitation>> response) {
                responseEvent.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Invitation>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    /**
     * Query my invitation of a given campaign
     * @param campaignId the campaign id
     * @param responseEvent
     */
    public static void getMyCampaignInvitation(int campaignId, final ServiceResponse<Invitation> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<Invitation> invitations = client.getMyCampaignInvitation(campaignId, AuthenticationService.getmToken().getAccessToken());
        invitations.enqueue(new Callback<Invitation>() {
            @Override
            public void onResponse(Call<Invitation> call, Response<Invitation> response) {
                responseEvent.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Invitation> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    public static void updateInvitationStatus(int invitationId, InvitationStatus status, final ServiceResponse<Invitation> responseEvent){
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<Invitation> invitation = client.updateInvitationStatus(invitationId, AuthenticationService.getmToken().getAccessToken(), new InvitationStatusPatch(status));
        invitation.enqueue(new Callback<Invitation>() {
            @Override
            public void onResponse(Call<Invitation> call, Response<Invitation> response) {
                responseEvent.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Invitation> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

}
