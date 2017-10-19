package com.sherpa.mynelis.cigref.view.events;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.api.NelisInterface;
import com.sherpa.mynelis.cigref.api.ServiceGenerator;
import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.service.AuthenticationService;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_by_date, container, false);

        initData();
        initRecycler(view, container);

        return view;
    }

    private void initData() {
        NelisInterface client = ServiceGenerator.createService(NelisInterface.class);
        Call<ArrayList<CampaignModel>> campaigns = client.getInvitations(AuthenticationService.getmToken().getAccessToken());
        campaigns.enqueue(new Callback<ArrayList<CampaignModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CampaignModel>> call, Response<ArrayList<CampaignModel>> response) {
                System.out.println("Campaign !");
                mAdapter.setmDataset(response.body());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<CampaignModel>> call, Throwable t) {

            }
        });

//        EventCampaignService.getMyInvitations(new ServiceResponse<CampaignModel>() {
//            @Override
//            public void onSuccess(ArrayList<CampaignModel> datas) {
//                mAdapter.setmDataset(datas);
//                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(ServiceReponseErrorType error, String errorMessage) {
//
//            }
//        });
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
