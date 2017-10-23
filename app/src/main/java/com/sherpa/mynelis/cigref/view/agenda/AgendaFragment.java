package com.sherpa.mynelis.cigref.view.agenda;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
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
        for (int i = 0; i < campaigns.size(); i++) {
            final CampaignModel campaign = campaigns.get(i);
            final int position = i;
            EventCampaignService.getInstance().getMyCampaignInvitation(campaign.getIdNelis(), new ServiceResponse<Invitation>() {
                @Override
                public void onSuccess(Invitation datas) {
                    campaign.setMyInvitation(datas);
                    mAdapter.notifyItemChanged(position);
                }

                @Override
                public void onError(ServiceReponseErrorType error, String errorMessage) {

                }
            });
            EventCampaignService.getInstance().getCampaignInvitations(campaign.getIdNelis(), new ServiceResponse<List<Invitation>>() {
                @Override
                public void onSuccess(List<Invitation> datas) {
                    campaign.setInvitations(datas);
                    mAdapter.notifyItemChanged(position);
                }

                @Override
                public void onError(ServiceReponseErrorType error, String errorMessage) {

                }
            });
        }
    }

    public void clearSelection(){
        mAdapter.setmDataset(new ArrayList<CampaignModel>());
        mAdapter.notifyDataSetChanged();
    }

    private void initData(){
        EventCampaignService.getInstance().getMyAcceptedCampaigns(new ServiceResponse<List<CampaignModel>>() {
            @Override
            public void onSuccess(List<CampaignModel> datas) {
                onCampaignLoaded(datas);
            }

            @Override
            public void onError(ServiceReponseErrorType error, String errorMessage) {

            }
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

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CampaignEventAdpader(new ArrayList<CampaignModel>());
        mRecyclerView.setAdapter(mAdapter);
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

//        CalendarDay today = CalendarDay.from(Calendar.getInstance());
//        decorator.addEvent(today, EventFactory.createEvent());
        calendarView.addDecorator(decorator);
    }

    private void addEventToCalendar(CampaignModel campaign){
        decorator.addEvent(campaign.getClosedDate(), campaign);
    }

}
