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

import java.text.DateFormat;
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

        String formattedDate = DateFormat.getDateInstance().format(event.getClosedDate());

        String contentPlain = context.getResources().getString(R.string.transfert_mail_content_plain);
        String formattedContentPlain = String.format(contentPlain, event.getTitle(), event.getDescription(), event.getEventPlace(), formattedDate);

        String contentHTML = context.getResources().getString(R.string.transfert_mail_content_html);
        String formattedContentHTML = String.format(contentHTML, event.getTitle(), event.getDescription(), event.getEventPlace(), formattedDate);

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

    /**
     * Show an dialog box after event register success to propose user to add event to phone calendar
     * @param context
     * @param event
     */
    public static void showEventRegisterSuccessAlert(final Context context, final CampaignModel event) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.DialogStyle);
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
