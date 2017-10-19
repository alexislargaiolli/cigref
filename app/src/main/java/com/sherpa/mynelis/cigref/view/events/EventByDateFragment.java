package com.sherpa.mynelis.cigref.view.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventByDateFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private EventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public EventByDateFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_by_date, container, false);

        initRecycler(view, container);
        initData();

        return view;
    }

    private void initData() {
        EventCampaignService.getMyCampaigns(new ServiceResponse<ArrayList<CampaignModel>>() {
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

    private void initRecycler(View view, ViewGroup container) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdpader(new ArrayList<CampaignModel>());
        mAdapter.setEventListener(new EventAdpader.EventListener() {
            @Override
            public void onEventSelected(CampaignModel eventCampaign) {
                EventServices.goToEventDetail(getContext(), eventCampaign);
            }

            @Override
            public void onInvitationStatusChanged(CampaignModel eventCampaign, boolean accepted) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

}
