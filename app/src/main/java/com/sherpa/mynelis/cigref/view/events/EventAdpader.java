package com.sherpa.mynelis.cigref.view.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.Event;

import java.util.Date;

/**
 * Created by Alexis Largaiolli on 16/10/2017.
 */

public class EventAdpader extends RecyclerView.Adapter<EventAdpader.ViewHolder> {
    private Event[] mDataset;
    private static final String EVENT_DAY_FORMAT = "%1$td";
    private static final String EVENT_MONTH_FORMAT = "%1$tb";
    private static String GUEST_COUNT_FORMAT;
    private EventListener eventListener;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View cardView;

        public ViewHolder(View v) {
            super(v);
            cardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventAdpader(Event[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdpader.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        GUEST_COUNT_FORMAT = parent.getContext().getResources().getString(R.string.event_guest_count);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        initItem(holder.cardView, position, "Assemblée Générale 2017", 2, new Date(), "Conférence CIGREF", 1);
    }

    private void initItem(View cardView, final int position, String eventName, int guestCount, Date eventDate, String eventType, int invitationStatus) {
        ((TextView) cardView.findViewById(R.id.eventName)).setText(eventName);

        String guestLabel = String.format(GUEST_COUNT_FORMAT, guestCount);
        ((TextView) cardView.findViewById(R.id.eventGuestCount)).setText(guestLabel);
        ((TextView) cardView.findViewById(R.id.eventName)).setText(eventName);

        String monthFormat = String.format(EVENT_MONTH_FORMAT, eventDate);
        ((TextView) cardView.findViewById(R.id.eventMonth)).setText(monthFormat);

        String dayFormat = String.format(EVENT_DAY_FORMAT, eventDate);
        ((TextView) cardView.findViewById(R.id.eventDay)).setText(dayFormat);

        if (invitationStatus == 1) {
            ((ImageButton) cardView.findViewById(R.id.yesButton)).setImageResource(R.mipmap.ic_yes_on);
        }
        if (invitationStatus == 2) {
            ((ImageButton) cardView.findViewById(R.id.noButton)).setImageResource(R.mipmap.ic_no_on);
        }

        ((ImageButton) cardView.findViewById(R.id.noButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeInvitationStatus(position, false);
            }
        });

        ((ImageButton) cardView.findViewById(R.id.yesButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onChangeInvitationStatus(position, false);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventSelected(position);
            }
        });
    }

    /**
     * Handler call after invitation has been accept or refused
     *
     * @param position index of the event in the dataset
     * @param accept   true if invitation has been accepted, false otherwise
     */
    private void onChangeInvitationStatus(int position, boolean accept) {
        System.out.println(accept ? "Accept " : "Refuse " + mDataset[position]);
        if(eventListener != null){
            eventListener.onInvitationStatusChanged(mDataset[position], accept);
        }
    }

    /**
     * Handler call when en event is clicked
     *
     * @param position index of the clicked event in dataet
     */
    private void onEventSelected(int position) {
        System.out.println("Event selected  " + mDataset[position]);
        if(eventListener != null){
            eventListener.onEventSelected(mDataset[position]);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public Event[] getmDataset() {
        return mDataset;
    }

    public void setmDataset(Event[] dataset) {
        mDataset = dataset;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public interface EventListener{
        public void onEventSelected(Event eventCampaign);
        public void onInvitationStatusChanged(Event eventCampaign, boolean accepted);
    }
}
