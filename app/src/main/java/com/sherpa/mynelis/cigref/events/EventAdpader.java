package com.sherpa.mynelis.cigref.events;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sherpa.mynelis.cigref.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by jacques on 16/10/2017.
 */

public class EventAdpader extends RecyclerView.Adapter<EventAdpader.ViewHolder> {
    private String[] mDataset;
    private String MONTH_FORMAT = "%1$td";

    private String guestCountPattern;

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
    public EventAdpader(String[] myDataset) {
        mDataset = myDataset;
        guestCountPattern = "";
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EventAdpader.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        initItem(holder.cardView, "Event name", 2, new Date(), "Conférence CIGREF");
    }

    private void initItem(View cardView, String eventName, int guestCount, Date eventDate, String eventType){
        ((TextView) cardView.findViewById(R.id.eventName)).setText(eventName);

        String guestLabel = String.format(guestCountPattern, guestCount);
        ((TextView) cardView.findViewById(R.id.eventGuestCount)).setText(guestLabel);
        ((TextView) cardView.findViewById(R.id.eventName)).setText(eventName);

        String monthFormat = String.format(MONTH_FORMAT, eventDate);
        ((TextView) cardView.findViewById(R.id.eventMonth)).setText(monthFormat);

        ((ImageButton) cardView.findViewById(R.id.noButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ((ImageButton) cardView.findViewById(R.id.yesButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public String getGuestCountPattern() {
        return guestCountPattern;
    }

    public void setGuestCountPattern(String guestCountPattern) {
        this.guestCountPattern = guestCountPattern;
    }
}
