package com.sherpa.mynelis.cigref.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeListModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alexis Largaiolli on 23/10/2017.
 */
public class EventCampaignRepository {
    private static final EventCampaignRepository ourInstance = new EventCampaignRepository();

    public static EventCampaignRepository getInstance() {
        return ourInstance;
    }

    private MutableLiveData<Boolean> campaignLoading;
    private MutableLiveData<List<CampaignModel>> campaigns;
    private MutableLiveData<CampaignTypeModel[]> themes;
    private List<CampaignModel> campaignModelList;

    private EventCampaignRepository() {
        campaigns = new MutableLiveData<List<CampaignModel>>();
        campaignLoading = new MutableLiveData<Boolean>();
    }

    public MutableLiveData<List<CampaignModel>> fullLoad(boolean refresh) {
        if (campaignModelList != null && !refresh) {
            return campaigns;
        }
        campaignLoading.setValue(true);
        EventCampaignService.getInstance().getMyCampaigns(new ServiceResponse<List<CampaignModel>>() {
            @Override
            public void onSuccess(List<CampaignModel> datas) {
                if (datas != null) {
                    campaignModelList = datas;
                    final int totalCampaignCount = datas.size();
                    final List<Boolean> myInvitationRetrived = new ArrayList<Boolean>();
                    final List<Boolean> invitationsRetrived = new ArrayList<Boolean>();
                    for (int i = 0; i < campaignModelList.size(); i++) {
                        final CampaignModel campaign = campaignModelList.get(i);
                        final int position = i;
                        EventCampaignService.getInstance().getMyCampaignInvitation(campaign.getIdNelis(), new ServiceResponse<Invitation>() {
                            @Override
                            public void onSuccess(Invitation datas) {
                                campaignModelList.get(position).setMyInvitation(datas);
                                myInvitationRetrived.add(true);
                                if (invitationsRetrived.size() == totalCampaignCount && myInvitationRetrived.size() == totalCampaignCount) {
                                    campaigns.setValue(campaignModelList);
                                    campaignLoading.setValue(false);
                                }
                            }

                            @Override
                            public void onError(ServiceReponseErrorType error, String errorMessage) {
                                campaignLoading.setValue(false);
                            }
                        });
                        EventCampaignService.getInstance().getCampaignInvitations(campaign.getIdNelis(), new ServiceResponse<List<Invitation>>() {
                            @Override
                            public void onSuccess(List<Invitation> datas) {
                                campaignModelList.get(position).setInvitations(datas);
                                invitationsRetrived.add(true);
                                if (invitationsRetrived.size() == totalCampaignCount && myInvitationRetrived.size() == totalCampaignCount) {
                                    campaigns.setValue(campaignModelList);
                                    campaignLoading.setValue(false);
                                }
                            }

                            @Override
                            public void onError(ServiceReponseErrorType error, String errorMessage) {
                                campaignLoading.setValue(false);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                campaignLoading.setValue(false);
            }
        });
        return campaigns;
    }

    public MutableLiveData<CampaignTypeModel[]> loadThemes(){
        themes = new MutableLiveData<CampaignTypeModel[]>();
        EventCampaignService.getInstance().getCampaignTypeList(new ServiceResponse<CampaignTypeListModel>() {
            @Override
            public void onSuccess(CampaignTypeListModel datas) {
                if(datas != null && datas.getTypes() != null){
                    themes.setValue(datas.getTypes());
                }
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {

            }
        });
        return themes;
    }

    public void changeInvitationStatus(final CampaignModel campaign, InvitationStatus status, Context context) {
        // Keep previous state to restore it if update fail
        final InvitationStatus previousStatus = campaign.getMyInvitation().getStatus();
        //Apply change before making http update
        final CampaignModel c = Stream.of(campaignModelList).filter(a -> a.getIdNelis() == campaign.getIdNelis()).findFirst().get();
        c.getMyInvitation().setStatus(status);
        if (InvitationStatus.ACCEPTED.equals(status)) {
            c.addInvitation(c.getMyInvitation());
        } else {
            c.removeInvitation(c.getMyInvitation());
        }
        campaigns.setValue(campaignModelList);
        EventCampaignService.getInstance().updateInvitationStatus(campaign.getMyInvitation().getId(), status, new ServiceResponse<Invitation>() {
            @Override
            public void onSuccess(Invitation invitation) {
                if (InvitationStatus.ACCEPTED.equals(invitation.getStatus())) {
                    EventServices.showEventRegisterSuccessAlert(context, campaign);
                }
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                // If fail, restore previous state
                CampaignModel c = Stream.of(campaignModelList).filter(a -> a.getIdNelis() == campaign.getIdNelis()).findFirst().get();
                c.getMyInvitation().setStatus(previousStatus);
                if (InvitationStatus.ACCEPTED.equals(previousStatus)) {
                    c.addInvitation(c.getMyInvitation());
                } else {
                    c.removeInvitation(c.getMyInvitation());
                }
                campaigns.setValue(campaignModelList);
            }
        });
    }

    public MutableLiveData<Boolean> getCampaignLoading() {
        return campaignLoading;
    }

    public void setCampaignLoading(MutableLiveData<Boolean> campaignLoading) {
        this.campaignLoading = campaignLoading;
    }
}
