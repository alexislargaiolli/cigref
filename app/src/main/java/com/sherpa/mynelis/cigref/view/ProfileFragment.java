package com.sherpa.mynelis.cigref.view;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.AuthenticationService;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;
import com.sherpa.mynelis.cigref.service.UtilsService;
import com.sherpa.mynelis.cigref.view.events.EventAdpader;
import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private EventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view_profil);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdpader(new ArrayList<CampaignModel>());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setEventListener(new EventAdpader.EventListener() {
            @Override
            public void onEventSelected(CampaignModel eventCampaign) {
                EventServices.goToEventDetail(getContext(), eventCampaign);
            }

            @Override
            public void onInvitationStatusChanged(final int position, final CampaignModel eventCampaign, final InvitationStatus status) {
                EventCampaignService.updateInvitationStatus(eventCampaign.getMyInvitation().getId(), status, new ServiceResponse<Invitation>() {
                    @Override
                    public void onSuccess(Invitation datas) {
                        mAdapter.getmDataset().get(position).setMyInvitation(datas);

                        if (InvitationStatus.ACCEPTED.equals(status)) {
                            mAdapter.getmDataset().get(position).addInvitation(datas);
                            EventServices.showEventRegisterSuccessAlert(getContext(), eventCampaign);
                        }
                        else{
                            mAdapter.getmDataset().get(position).removeInvitation(datas);
                        }

                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(ServiceReponseErrorType error, String errorMessage) {
//                        eventDetailsFragment.setInvitationInfo(previousStatus);
                        UtilsService.showErrorAlert(getContext(), getString(R.string.error_invitation_update));
                    }
                });
            }
        });

        ImageButton logoutBtn = (ImageButton) view.findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });

        initData();

        return view;
    }

    private void initData(){
        EventCampaignService.getMyAcceptedCampaigns(new ServiceResponse<ArrayList<CampaignModel>>() {
            @Override
            public void onSuccess(ArrayList<CampaignModel> datas) {
                onCampaignLoaded(datas);
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {

            }
        });
    }

    private void onCampaignLoaded(ArrayList<CampaignModel> campaigns) {
        if(campaigns != null) {
            mAdapter.setmDataset(campaigns);
            mAdapter.notifyDataSetChanged();
            for (int i = 0; i < campaigns.size(); i++) {
                final CampaignModel campaign = campaigns.get(i);
                final int position = i;
                EventCampaignService.getMyCampaignInvitation(campaign.getIdNelis(), new ServiceResponse<Invitation>() {
                    @Override
                    public void onSuccess(Invitation datas) {
                        campaign.setMyInvitation(datas);
                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(ServiceReponseErrorType error, String errorMessage) {

                    }
                });
                EventCampaignService.getCampaignInvitations(campaign.getIdNelis(), new ServiceResponse<ArrayList<Invitation>>() {
                    @Override
                    public void onSuccess(ArrayList<Invitation> datas) {
                        campaign.setInvitations(datas);
                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(ServiceReponseErrorType error, String errorMessage) {

                    }
                });
            }
        }
    }

    /**
     * Show a logout confirmation dialog
     */
    private void confirmLogout() {
        new AlertDialog.Builder(this.getActivity())
                .setMessage(getActivity().getResources().getString(R.string.profil_logout_confirm))
                .setCancelable(false)
                .setPositiveButton(getActivity().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton(getActivity().getResources().getString(R.string.no), null)
                .show();
    }

    /**
     * Call when user has confirmed logout
     */
    private void logout(){
        AuthenticationService.logout(getActivity());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}
