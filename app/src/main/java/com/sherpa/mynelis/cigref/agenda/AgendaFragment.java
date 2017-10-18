package com.sherpa.mynelis.cigref.agenda;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.events.EventAdpader;
import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    MaterialCalendarView calendarView;
    private RecyclerView mRecyclerView;
    private EventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Event[] myDataset;


    public AgendaFragment() {
        myDataset = new Event[0];
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_agenda, container, false);

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

        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        final AgendaDecorator decorator = new AgendaDecorator();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                List<Event> events = decorator.getEvents(date);
                if(events != null){
                    onEventSelected(events);
                }
                else{
                    clearSelection();
                }
            }
        });

        CalendarDay today = CalendarDay.from(Calendar.getInstance());
        decorator.addEvent(today, EventFactory.createEvent());
        calendarView.addDecorator(decorator);

        return view;
    }

    public void onEventSelected(List<Event> events){
        mAdapter.setmDataset(events.toArray(new Event[events.size()]));
        mAdapter.notifyDataSetChanged();
    }

    public void clearSelection(){
        mAdapter.setmDataset(new Event[0]);
        mAdapter.notifyDataSetChanged();
    }

}
