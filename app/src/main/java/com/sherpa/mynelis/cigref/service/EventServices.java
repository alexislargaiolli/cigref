package com.sherpa.mynelis.cigref.service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.view.details.EventDetailsActivity;

import java.util.Calendar;

/**
 * Created by Alexis Largaiolli on 18/10/2017.
 */
public class EventServices {

    public static final String MY_PERMISSIONS_REQUEST_READ_CONTACTS = "MY_PERMISSIONS_REQUEST_READ_CONTACTS";

    /**
     * Open client mail to share an event campaign by mail
     *
     * @param event
     */
    public static void sendEventByMail(Context context, CampaignModel event) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

        String contentPlain = context.getResources().getString(R.string.transfert_mail_content_plain);
        String formattedContentPlain = String.format(contentPlain, event.getTitle(), event.getDescription(), event.getEventPlace(), event.getClosedDate());

        String contentHTML = context.getResources().getString(R.string.transfert_mail_content_html);
        String formattedContentHTML = String.format(contentHTML, event.getTitle(), event.getDescription(), event.getEventPlace(), event.getClosedDate());

        String title = context.getResources().getString(R.string.transfert_mail_title);
        String formattedTitle = String.format(title, event.getTitle());
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, formattedTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, formattedContentPlain);
//        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(formattedContentHTML));
        context.startActivity(Intent.createChooser(emailIntent, context.getResources().getString(R.string.transfert_mail_chooser)));
    }

    /**
     * Navigate to the event detail activity
     *
     * @param context
     * @param event
     */
    public static void goToEventDetail(Context context, CampaignModel event) {
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(EventDetailsActivity.EVENT_ARGUMENT_KEY, event);
//        intent.putExtra(EventDetailsFragment.INVITATIONS_ARGUMENT_KEY, event.getInvitations());
//        intent.putExtra(EventDetailsFragment.MY_INVITATION_ARGUMENT_KEY, event.getMyInvitation());
        context.startActivity(intent);
    }

    /**
     * Add event to calendar.
     *
     * @param context
     * @param event
     * @throws SecurityException
     */
    public static void addEventToCalendar(Context context, CampaignModel event) throws SecurityException {
//        ContentResolver cr = context.getContentResolver();
//        ContentValues values = new ContentValues();
//        Long startTime = event.getClosedDate().getTime();
//        values.put(CalendarContract.Events.DTSTART, startTime);
//        values.put(CalendarContract.Events.DURATION, "PT1H");
////        values.put(Events.DTEND, endMillis);
//        values.put(CalendarContract.Events.TITLE, event.getTitle());
//        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
//        values.put(CalendarContract.Events.CALENDAR_ID, 0);
//        values.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));
//        cr.insert(CalendarContract.Events.CONTENT_URI, values);

        Calendar beginTime = Calendar.getInstance();
        beginTime.setTime(event.getClosedDate());
        Calendar endTime = Calendar.getInstance();
        endTime.setTime(event.getClosedDate());
        endTime.add(Calendar.HOUR_OF_DAY, 1);

        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, event.getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, event.getDescription())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, event.getEventPlace());
        context.startActivity(intent);
    }

    public static void showEventRegisterSuccessAlert(final Context context, final CampaignModel event) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(context.getString(R.string.event_register_success_title))
                .setMessage(context.getString(R.string.event_register_success_message))
                .setPositiveButton(R.string.event_register_success_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EventServices.addEventToCalendar(context, event);
                    }
                })
                .setNegativeButton(R.string.event_register_success_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
