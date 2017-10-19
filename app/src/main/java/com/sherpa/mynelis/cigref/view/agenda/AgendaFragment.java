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
import com.sherpa.mynelis.cigref.view.events.EventAdpader;
import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    MaterialCalendarView calendarView;
    private RecyclerView mRecyclerView;
    private EventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CampaignModel> myDataset;


    public AgendaFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_agenda, container, false);

        initRecycler(view, container);
        initCalendar(view);


        return view;
    }

    public void onEventSelected(ArrayList<CampaignModel> events){
        mAdapter.setmDataset(events);
        mAdapter.notifyDataSetChanged();
    }

    public void clearSelection(){
        mAdapter.setmDataset(new ArrayList<CampaignModel>());
        mAdapter.notifyDataSetChanged();
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
        mAdapter = new EventAdpader(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initCalendar(View view){
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        final AgendaDecorator decorator = new AgendaDecorator();
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

}
