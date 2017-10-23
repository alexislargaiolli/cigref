package com.sherpa.mynelis.cigref.view.agenda;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;
import com.sherpa.mynelis.cigref.view.events.CampaignEventAdpader;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    MaterialCalendarView calendarView;
    private RecyclerView mRecyclerView;
    private CampaignEventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CampaignModel> myDataset;
    private AgendaDecorator decorator;
    private CampaignEventViewModel campaignViewModel;


    public AgendaFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_agenda, container, false);

        initRecycler(view, container);
        initCalendar(view);
        initData();
        return view;
    }

    public void onEventSelected(ArrayList<CampaignModel> campaigns){
        mAdapter.setmDataset(campaigns);
        mAdapter.notifyDataSetChanged();
    }

    public void clearSelection(){
        mAdapter.setmDataset(new ArrayList<CampaignModel>());
        mAdapter.notifyDataSetChanged();
    }

    private void initData(){
        campaignViewModel = ViewModelProviders.of(getActivity()).get(CampaignEventViewModel.class);
        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
            onCampaignLoaded(campaignModels);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void onCampaignLoaded(List<CampaignModel> campaigns) {
        if(campaigns != null) {
            for (CampaignModel campaign : campaigns) {
                addEventToCalendar(campaign);
            }
            calendarView.state().edit().commit();
        }
    }

    private void initRecycler(View view, ViewGroup container){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view_agenda);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CampaignEventAdpader(new ArrayList<CampaignModel>());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setEventListener(new CampaignEventAdpader.EventListener() {
            @Override
            public void onEventSelected(CampaignModel eventCampaign) {
                EventServices.goToEventDetail(getContext(), eventCampaign);
            }

            @Override
            public void onInvitationStatusChanged(int position, CampaignModel eventCampaign, InvitationStatus status) {
                EventCampaignRepository.getInstance().changeInvitationStatus(eventCampaign, status, getContext());
            }
        });
    }

    private void initCalendar(View view){
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        decorator = new AgendaDecorator();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                ArrayList<CampaignModel> events = decorator.getEvents(date);
                if(events != null){
                    onEventSelected(events);
                }
                else{
                    clearSelection();
                }
            }
        });
        calendarView.addDecorator(decorator);
    }

    private void addEventToCalendar(CampaignModel campaign){
        decorator.addEvent(campaign.getClosedDate(), campaign);
    }

}
