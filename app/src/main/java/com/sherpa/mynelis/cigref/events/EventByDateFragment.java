package com.sherpa.mynelis.cigref.events;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.EventServices;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.details.EventDetailsActivity;
import com.sherpa.mynelis.cigref.details.EventDetailsFragment;
import com.sherpa.mynelis.cigref.details.InvitationFragment;
import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventByDateFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private EventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Event[] myDataset;

    public EventByDateFragment() {
        myDataset = EventFactory.createEvents(4);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_event_by_date, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new EventAdpader(myDataset);
        mAdapter.setEventListener(new EventAdpader.EventListener() {
            @Override
            public void onEventSelected(Event eventCampaign) {
                EventServices.goToEventDetail(getContext(), eventCampaign);
            }

            @Override
            public void onInvitationStatusChanged(Event eventCampaign, boolean accepted) {

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

}
