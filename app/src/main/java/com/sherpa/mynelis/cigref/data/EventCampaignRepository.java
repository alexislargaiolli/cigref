package com.sherpa.mynelis.cigref.data;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;
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
    private static final String ERROR_EVENT_MESSAGE = "Une erreur est survenue pendant la récupération des événements";
    private static final String ERROR_INVITATIONS_MESSAGE = "Une erreur est survenue pendant la récupération des invitations";
    private static final String ERROR_MY_INVITATION_MESSAGE = "Une erreur est survenue pendant la récupération de vos invitations";
    private static final String ERROR_INVITATION_UPDATE_MESSAGE = "Une erreur est survenue pendant le traitement de votre invitation. Veuillez réessayer.";
    private static final EventCampaignRepository ourInstance = new EventCampaignRepository();

    public static EventCampaignRepository getInstance() {
        return ourInstance;
    }

    private MutableLiveData<Boolean> campaignLoading;
    private MutableLiveData<Boolean> invitationLoading;
    private MutableLiveData<String> loadingError;
    private MutableLiveData<List<CampaignModel>> campaigns;
    private MutableLiveData<CampaignTypeModel[]> themes;
    private List<CampaignModel> campaignModelList;

    private EventCampaignRepository() {
        campaigns = new MutableLiveData<List<CampaignModel>>();
        campaignLoading = new MutableLiveData<Boolean>();
        invitationLoading = new MutableLiveData<Boolean>();
        loadingError = new MutableLiveData<String>();
    }

    public MutableLiveData<List<CampaignModel>> fullLoad(boolean refresh) {
        if (campaignModelList != null && !refresh) {
            return campaigns;
        }
        loadingError.setValue(null);
        campaignLoading.setValue(true);
        // Add error raised in a list to check if an error as already be raised, we cannot use a final boolean
        EventCampaignService.getInstance().getMyCampaigns(new ServiceResponse<List<CampaignModel>>() {
            @Override
            public void onSuccess(List<CampaignModel> datas) {
                if (datas != null) {
                    campaignModelList = datas;
                    campaigns.setValue(campaignModelList);
                    campaignLoading.setValue(false);
                }
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                campaignLoading.setValue(false);
                handleError(ERROR_EVENT_MESSAGE);
            }
        });
        return campaigns;
    }

    public MutableLiveData<CampaignTypeModel[]> loadThemes() {
        themes = new MutableLiveData<CampaignTypeModel[]>();
        EventCampaignService.getInstance().getCampaignTypeList(new ServiceResponse<CampaignTypeListModel>() {
            @Override
            public void onSuccess(CampaignTypeListModel datas) {
                if (datas != null && datas.getTypes() != null) {
                    themes.setValue(datas.getTypes());
                }
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                handleError(ERROR_INVITATIONS_MESSAGE);
            }
        });
        return themes;
    }

    public void loadInvitations(final CampaignModel campaign){
        this.invitationLoading.setValue(true);
        EventCampaignService.getInstance().getCampaignInvitations(campaign.getIdNelis(), new ServiceResponse<List<Invitation>>() {
            @Override
            public void onSuccess(List<Invitation> datas) {
                CampaignModel c = Stream.of(campaignModelList).filter(a -> a.getIdNelis() == campaign.getIdNelis()).findFirst().get();
                c.setInvitations(datas);
                c.setInvitationLoaded(true);
                campaigns.setValue(campaignModelList);
                invitationLoading.setValue(false);
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                invitationLoading.setValue(false);
                handleError(ERROR_EVENT_MESSAGE);
            }
        });
    }

    public void changeInvitationStatus(final CampaignModel campaign, InvitationStatus status, Context context) {
        // Keep previous state to restore it if update fail
        final InvitationStatus previousStatus = campaign.getMyStatus();
        //Apply change before making http update
        updateCampaignStatus(campaign, status);
        loadingError.setValue(null);

        EventCampaignService.getInstance().getMyCampaignInvitation(campaign.getIdNelis(), new ServiceResponse<Invitation>() {
            @Override
            public void onSuccess(Invitation datas) {
                campaign.setMyInvitation(datas);
                campaign.addInvitation(datas);
                updateCampaignStatus(campaign, status);
                EventCampaignService.getInstance().updateInvitationStatus(campaign.getMyInvitation().getId(), status, new ServiceResponse<Invitation>() {
                    @Override
                    public void onSuccess(Invitation invitation) {
                        if (InvitationStatus.ACCEPTED.equals(invitation.getStatus())) {
                            EventServices.showEventRegisterSuccessAlert(context, campaign);
                        } else {
                            EventServices.removeEventCalendar(context, campaign);
                        }
                    }

                    @Override
                    public void onError(ServiceReponseErrorType error, String errorMessage) {
                        // If fail, restore previous state
                        updateCampaignStatus(campaign, previousStatus);
                        loadingError.setValue(ERROR_MY_INVITATION_MESSAGE);
                        loadingError.setValue(null);
                    }
                });
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {
                updateCampaignStatus(campaign, previousStatus);
                handleError(ERROR_INVITATION_UPDATE_MESSAGE);
            }
        });
    }

    private void updateCampaignStatus(CampaignModel campaign, InvitationStatus status){
        CampaignModel c = Stream.of(campaignModelList).filter(a -> a.getIdNelis() == campaign.getIdNelis()).findFirst().get();
        c.changeInvitationStatus(status);
        if(c.getMyInvitation() != null) {
            Stream.of(c.getInvitations()).filter(i -> i.getId() == c.getMyInvitation().getId()).findFirst().get().setStatus(status);
        }
        campaigns.setValue(campaignModelList);
    }

    private void handleError(String message){
        loadingError.setValue(message);
        loadingError.setValue(null);
    }

    public MutableLiveData<Boolean> getCampaignLoading() {
        return campaignLoading;
    }

    public MutableLiveData<String> getLoadingError() {
        return loadingError;
    }

    public void setCampaignLoading(MutableLiveData<Boolean> campaignLoading) {
        this.campaignLoading = campaignLoading;
    }

    public MutableLiveData<Boolean> getInvitationLoading() {
        return invitationLoading;
    }
}
