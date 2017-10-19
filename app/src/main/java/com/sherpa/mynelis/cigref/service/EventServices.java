package com.sherpa.mynelis.cigref.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.view.details.EventDetailsActivity;
import com.sherpa.mynelis.cigref.view.details.EventDetailsFragment;
import com.sherpa.mynelis.cigref.model.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Alexis Largaiolli on 18/10/2017.
 */
public class EventServices {

    public static final String MY_PERMISSIONS_REQUEST_READ_CONTACTS = "MY_PERMISSIONS_REQUEST_READ_CONTACTS";

    /**
     * Open client mail to share an event campaign by mail
     * @param event
     */
    public static void sendEventByMail(Context context, CampaignModel event) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String contentPlain = context.getResources().getString(R.string.transfert_mail_content_plain);
        String formattedContentPlain = String.format(contentPlain, event.getTitle(), event.getDescription(), event.getEventPlace(), event.getClosedDate());

        String contentHTML = context.getResources().getString(R.string.transfert_mail_content_html);
        String formattedContentHTML = String.format(contentHTML, event.getTitle(), event.getDescription(), event.getEventPlace(), event.getClosedDate());

        String title = context.getResources().getString(R.string.transfert_mail_title);
        String formattedTitle = String.format(title, event.getTitle());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, formattedTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, formattedContentPlain);
        emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, formattedContentHTML);
        context.startActivity(Intent.createChooser(emailIntent, context.getResources().getString(R.string.transfert_mail_chooser)));
    }

    /**
     * Navigate to the event detail activity
     * @param context
     * @param event
     */
    public static void goToEventDetail(Context context, CampaignModel event) {
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(EventDetailsFragment.EVENT_ARGUMENT_KEY, event);
        context.startActivity(intent);
    }

    /**
     * Add event to calendar.
     * @param context
     * @param event
     * @throws SecurityException
     */
    public static void addEventToCalendar(Context context, CampaignModel event) throws SecurityException {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        Long startTime = event.getClosedDate().getTime();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DURATION, "PT1H");
//        values.put(Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, event.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
        values.put(CalendarContract.Events.CALENDAR_ID, 0);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));
        cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

}