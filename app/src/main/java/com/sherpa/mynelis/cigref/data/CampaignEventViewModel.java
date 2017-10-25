package com.sherpa.mynelis.cigref.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;
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

    MutableLiveData<Boolean> campaignLoading;

    MutableLiveData<List<CampaignModel>> campaignsObservable;

    MutableLiveData<CampaignTypeModel[]> themesObservable;

    public CampaignEventViewModel() {
    }

    public MutableLiveData<CampaignTypeModel[]> getThemesObservable() {
        if(themesObservable == null){
            themesObservable = EventCampaignRepository.getInstance().loadThemes();
        }
        return themesObservable;
    }

    public LiveData<List<CampaignModel>> getCampaignsObservable() {
        if(campaignsObservable == null){
            campaignsObservable = EventCampaignRepository.getInstance().fullLoad(false);
        }
        return campaignsObservable;
    }

    public MutableLiveData<Boolean> getCampaignLoading() {
        if(campaignLoading == null){
            campaignLoading = EventCampaignRepository.getInstance().getCampaignLoading();
        }
        return campaignLoading;
    }
}
