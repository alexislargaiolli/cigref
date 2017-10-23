package com.sherpa.mynelis.cigref.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexis Largaiolli on 21/10/17.
 */
public class CampaignEventViewModel extends ViewModel {

    MutableLiveData<List<CampaignModel>> campaignsObservable;

    public CampaignEventViewModel() {
    }

    public LiveData<List<CampaignModel>> getCampaignsObservable() {
        if(campaignsObservable == null){
            campaignsObservable = new MutableLiveData<List<CampaignModel>>();
            loadCampaigns();
        }
        return campaignsObservable;
    }

    private void loadCampaigns(){
        campaignsObservable = EventCampaignRepository.getInstance().fullLoad();
    }

}
