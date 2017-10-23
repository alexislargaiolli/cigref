package com.sherpa.mynelis.cigref.view.events;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventServices;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventByDateFragment extends Fragment {

    private CampaignEventAdpader mAdapter;
    private CampaignEventViewModel campaignViewModel;

    public EventByDateFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_by_date, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CampaignEventAdpader(new ArrayList<CampaignModel>());
        mAdapter.setEventListener(new CampaignEventAdpader.EventListener() {
            @Override
            public void onEventSelected(CampaignModel eventCampaign) {
                EventServices.goToEventDetail(getContext(), eventCampaign);
            }

            @Override
            public void onInvitationStatusChanged(final int position, final CampaignModel eventCampaign, final InvitationStatus status) {
                EventCampaignRepository.getInstance().changeInvitationStatus(eventCampaign, status, getContext());
            }
        });

        mRecyclerView.setAdapter(mAdapter);

        campaignViewModel = ViewModelProviders.of(getActivity()).get(CampaignEventViewModel.class);
        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
            List<CampaignModel> sortedCampaigns =
                    Stream.of(campaignModels)
                            .sortBy(CampaignModel::getClosedDate)
                            .toList();
            mAdapter.setmDataset(sortedCampaigns);
            mAdapter.notifyDataSetChanged();
        });

        return view;
    }
}
