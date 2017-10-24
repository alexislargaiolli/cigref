package com.sherpa.mynelis.cigref.service;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.api.ServiceGenerator;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeListModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatusPatch;
import com.sherpa.mynelis.cigref.utils.APIError;
import com.sherpa.mynelis.cigref.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexis Largaiolli on 19/10/17.
 */
@SuppressLint("LongLogTag")
public class EventCampaignService {

    private static final String TAG = "EventCampaignService";



    private static EventCampaignService instance;

    private EventCampaignService(){

    }

    /**
     * Query the list of campaign i'm invited in
      * @param responseEvent
     */
    public void getMyCampaigns(final ServiceResponse<List<CampaignModel>> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<List<CampaignModel>> campaigns = client.getMyCampaigns(AuthenticationService.getInstance().getmToken().getAccessToken());
        campaigns.enqueue(new Callback<List<CampaignModel>>() {

            @Override
            public void onResponse(Call<List<CampaignModel>> call, Response<List<CampaignModel>> response) {
                if(response.isSuccessful()) {
                    responseEvent.onSuccess(response.body());
                }
                else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.w(TAG + ".getMyCampaigns()", error.message());
                    responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, error.message());
                }
            }

            @Override
            public void onFailure(Call<List<CampaignModel>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    /**
     * Query the list of campaign where i accepted invitation
     * @param responseEvent
     */
    public void getMyAcceptedCampaigns(final ServiceResponse<List<CampaignModel>> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<List<CampaignModel>> campaigns = client.getMyAcceptedCampaigns(AuthenticationService.getInstance().getmToken().getAccessToken());
        campaigns.enqueue(new Callback<List<CampaignModel>>() {
            @Override
            public void onResponse(Call<List<CampaignModel>> call, Response<List<CampaignModel>> response) {
                if(response.isSuccessful()) {
                    responseEvent.onSuccess(response.body());
                }
                else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.w(TAG + ".getMyAcceptedCampaigns()", error.message());
                    responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, error.message());
                }
            }

            @Override
            public void onFailure(Call<List<CampaignModel>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    /**
     * Query the list of invitation of a campaign
     * @param campaignId id of the campaign
     * @param responseEvent
     */
    public void getCampaignInvitations(int campaignId, final ServiceResponse<List<Invitation>> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<List<Invitation>> invitations = client.getCampaignInvitations(campaignId, AuthenticationService.getInstance().getmToken().getAccessToken());
        invitations.enqueue(new Callback<List<Invitation>>() {
            @Override
            public void onResponse(Call<List<Invitation>> call, Response<List<Invitation>> response) {
                if(response.isSuccessful()) {
                    List<Invitation> invitas = response.body();
                    responseEvent.onSuccess(response.body());
                }
                else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.w(TAG + ".getCampaignInvitations()", error.message());
                    responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, error.message());
                }
            }

            @Override
            public void onFailure(Call<List<Invitation>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    /**
     * Query my invitation of a given campaign
     * @param campaignId the campaign id
     * @param responseEvent
     */
    public void getMyCampaignInvitation(int campaignId, final ServiceResponse<Invitation> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<Invitation> invitations = client.getMyCampaignInvitation(campaignId, AuthenticationService.getInstance().getmToken().getAccessToken());
        invitations.enqueue(new Callback<Invitation>() {
            @Override
            public void onResponse(Call<Invitation> call, Response<Invitation> response) {
                if(response.isSuccessful()) {
                    responseEvent.onSuccess(response.body());
                }
                else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.w(TAG + ".getMyCampaignInvitation()", error.message());
                    responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, error.message());
                }
            }

            @Override
            public void onFailure(Call<Invitation> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    public void updateInvitationStatus(int invitationId, InvitationStatus status, final ServiceResponse<Invitation> responseEvent){
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<Invitation> invitation = client.updateInvitationStatus(invitationId, AuthenticationService.getInstance().getmToken().getAccessToken(), new InvitationStatusPatch(status));
        invitation.enqueue(new Callback<Invitation>() {
            @Override
            public void onResponse(Call<Invitation> call, Response<Invitation> response) {
                if(response.isSuccessful()) {
                    responseEvent.onSuccess(response.body());
                }
                else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.w(TAG + ".updateInvitationStatus()", error.message());
                    responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, error.message());
                }
            }

            @Override
            public void onFailure(Call<Invitation> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    public void getCampaignTypeList(final ServiceResponse<CampaignTypeListModel> responseEvent) {
        NelisInterface client = ServiceGenerator.createNelisClient();
        Call<CampaignTypeListModel> typeList = client.getCampaignTypeList(AuthenticationService.getInstance().getmToken().getAccessToken());
        typeList.enqueue(new Callback<CampaignTypeListModel>() {
            @Override
            public void onResponse(Call<CampaignTypeListModel> call, Response<CampaignTypeListModel> response) {
                if(response.isSuccessful()) {
                    responseEvent.onSuccess(response.body());
                }
                else{
                    APIError error = ErrorUtils.parseError(response);
                    Log.w(TAG + ".getCampaignTypeList()", error.message());
                    responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, error.message());
                }
            }

            @Override
            public void onFailure(Call<CampaignTypeListModel> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

    public static EventCampaignService getInstance() {
        if(instance == null){
            instance = new EventCampaignService();
        }
        return instance;
    }
}
