package com.sherpa.mynelis.cigref.view.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexis Largaiolli on 16/10/2017.
 */
public class CampaignEventAdpader extends RecyclerView.Adapter<CampaignEventAdpader.ViewHolder> {
    private List<CampaignModel> mDataset;
    private static final String EVENT_DAY_FORMAT = "%1$td";
    private static final String EVENT_MONTH_FORMAT = "%1$tb";
    private static String GUEST_COUNT_FORMAT;
    private EventListener eventListener;
    private Date today = new Date();

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
    public CampaignEventAdpader(List<CampaignModel> myDataset) {
        mDataset = myDataset;
        if (mDataset == null) {
            mDataset = new ArrayList<CampaignModel>();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CampaignEventAdpader.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        CampaignModel campaignModel = mDataset.get(position);
        int guestCount = campaignModel.getInvitations() != null ? campaignModel.getInvitations().size() : 0;
        initItem(holder.cardView, position, campaignModel.getTitle(), guestCount, campaignModel.getClosedDate(), campaignModel.getType().getLabelFr(), campaignModel.getMyInvitation());
    }

    private void initItem(View cardView, final int position, String eventName, int guestCount, Date eventDate, String eventType, Invitation myInvitation) {
        ImageButton yesButton = (ImageButton) cardView.findViewById(R.id.yesButton);
        ImageButton noButton = (ImageButton) cardView.findViewById(R.id.noButton);
        TextView eventTitleLabel = (TextView) cardView.findViewById(R.id.eventName);
        TextView eventGuestCountLabel = (TextView) cardView.findViewById(R.id.eventGuestCount);
        TextView eventMonthLabel = (TextView) cardView.findViewById(R.id.eventMonth);
        TextView eventDayLabel = (TextView) cardView.findViewById(R.id.eventDay);
        TextView eventTypeLabel = (TextView) cardView.findViewById(R.id.eventType);


        // Set label text values
        eventTitleLabel.setText(eventName);
        eventGuestCountLabel.setText(String.format(GUEST_COUNT_FORMAT, guestCount));
        eventMonthLabel.setText(String.format(EVENT_MONTH_FORMAT, eventDate));
        eventDayLabel.setText(String.format(EVENT_DAY_FORMAT, eventDate));
        eventTypeLabel.setText(eventType);

        if(eventDate.after(today)) {

            // Set yes / no button status
            yesButton.setVisibility(View.VISIBLE);
            noButton.setVisibility(View.VISIBLE);
            yesButton.setSelected(myInvitation != null && myInvitation.getStatus() == InvitationStatus.ACCEPTED);
            noButton.setSelected(myInvitation != null && myInvitation.getStatus() == InvitationStatus.REFUSED);

            // Attach listener to buttons
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChangeInvitationStatus(position, InvitationStatus.REFUSED);
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onChangeInvitationStatus(position, InvitationStatus.ACCEPTED);
                }
            });
        } else {
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
        }

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
     * @param status new status of the invitation
     */
    private void onChangeInvitationStatus(int position, InvitationStatus status) {
        if (eventListener != null) {
            eventListener.onInvitationStatusChanged(position, mDataset.get(position), status);
        }
    }

    /**
     * Handler call when en event is clicked
     *
     * @param position index of the clicked event in dataet
     */
    private void onEventSelected(int position) {
        System.out.println("Event selected  " + mDataset.get(position));
        if (eventListener != null) {
            eventListener.onEventSelected(mDataset.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public List<CampaignModel> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<CampaignModel> dataset) {
        mDataset = dataset;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public interface EventListener {
        public void onEventSelected(CampaignModel eventCampaign);

        public void onInvitationStatusChanged(int position, CampaignModel eventCampaign, InvitationStatus status);
    }
}
