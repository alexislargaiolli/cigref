package com.sherpa.mynelis.cigref.service;

import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.api.ServiceGenerator;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexis Largaiolli on 19/10/17.
 */

public class EventCampaignService {

    public static void getMyInvitations(final ServiceResponse<CampaignModel> responseEvent) {
        NelisInterface client = ServiceGenerator.createService(NelisInterface.class);
        Call<ArrayList<CampaignModel>> campaigns = client.getInvitations(AuthenticationService.getmToken().getAccessToken());
        campaigns.enqueue(new Callback<ArrayList<CampaignModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CampaignModel>> call, Response<ArrayList<CampaignModel>> response) {
                System.out.println("Campaiugn okokok");
                responseEvent.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<CampaignModel>> call, Throwable t) {
                responseEvent.onError(ServiceResponse.ServiceReponseErrorType.NETWORK, t.getMessage());
            }
        });
    }

}
