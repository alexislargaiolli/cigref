package com.sherpa.mynelis.cigref.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

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
    List<CampaignModel> campaigns;

    public CampaignEventViewModel() {
        System.out.println(">>>>>>>>>>>>> CampaignEventViewModel <<<<<<<<<<<<<<<");
    }

    public MutableLiveData<List<CampaignModel>> getCampaignsObservable() {
        if(campaignsObservable == null){
            campaignsObservable = new MutableLiveData<List<CampaignModel>>();
            loadCampaigns();
        }
        return campaignsObservable;
    }

    public void changeInvitationStatus(final CampaignModel campaign, InvitationStatus status){
        // Keep previous state to restore it if update fail
        final InvitationStatus previousStatus = campaign.getMyInvitation().getStatus();

        // Apply change before making http update
        final CampaignModel c = campaigns.stream().filter(a -> a.getIdNelis() == campaign.getIdNelis()).findFirst().get();
        c.getMyInvitation().setStatus(status);
        if(InvitationStatus.ACCEPTED.equals(status)){
            c.addInvitation(c.getMyInvitation());
        }
        else{
            c.removeInvitation(c.getMyInvitation());
        }
        campaignsObservable.setValue(campaigns);
        EventCampaignService.getInstance().updateInvitationStatus(campaign.getMyInvitation().getId(), status, new ServiceResponse<Invitation>() {
            @Override
            public void onSuccess(Invitation invitation) {
                // If success do nothing
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                // If fail, restore previous state
                CampaignModel c = campaigns.stream().filter(a -> a.getIdNelis() == campaign.getIdNelis()).findFirst().get();
                c.getMyInvitation().setStatus(previousStatus);
                if(InvitationStatus.ACCEPTED.equals(previousStatus)){
                    c.addInvitation(c.getMyInvitation());
                }
                else{
                    c.removeInvitation(c.getMyInvitation());
                }
                campaignsObservable.setValue(campaigns);
                //eventDetailsFragment.setInvitationInfo(previousStatus);
                //UtilsService.showErrorAlert(getContext(), getString(R.string.error_invitation_update));
            }
        });
    }

    private void loadCampaigns(){
        EventCampaignService.getInstance().getMyCampaigns(new ServiceResponse<List<CampaignModel>>() {
            @Override
            public void onSuccess(List<CampaignModel> datas) {
                if(datas != null) {
                    campaigns = datas;
                    final int totalCampaignCount = datas.size();
                    final List<Boolean> myInvitationRetrived = new ArrayList<Boolean>();
                    final List<Boolean> invitationsRetrived = new ArrayList<Boolean>();
                    for (int i = 0; i < campaigns.size(); i++) {
                        final CampaignModel campaign = campaigns.get(i);
                        final int position = i;
                        EventCampaignService.getInstance().getMyCampaignInvitation(campaign.getIdNelis(), new ServiceResponse<Invitation>() {
                            @Override
                            public void onSuccess(Invitation datas) {
                                campaigns.get(position).setMyInvitation(datas);
                                myInvitationRetrived.add(true);
                                if (invitationsRetrived.size() == totalCampaignCount && myInvitationRetrived.size() == totalCampaignCount) {
                                    setCampaigns(campaigns);
                                }
                            }

                            @Override
                            public void onError(ServiceReponseErrorType error, String errorMessage) {

                            }
                        });
                        EventCampaignService.getInstance().getCampaignInvitations(campaign.getIdNelis(), new ServiceResponse<List<Invitation>>() {
                            @Override
                            public void onSuccess(List<Invitation> datas) {
                                campaigns.get(position).setInvitations(datas);
                                invitationsRetrived.add(true);
                                if (invitationsRetrived.size() == totalCampaignCount && myInvitationRetrived.size() == totalCampaignCount) {
                                    setCampaigns(campaigns);
                                }
                            }

                            @Override
                            public void onError(ServiceReponseErrorType error, String errorMessage) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {

            }
        });
    }

    private void setCampaigns(List<CampaignModel> campaigns){
        this.campaigns = campaigns;
        campaignsObservable.setValue(this.campaigns);
    }

}
