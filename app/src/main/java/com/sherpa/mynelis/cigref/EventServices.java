package com.sherpa.mynelis.cigref;

import android.content.Context;
import android.content.Intent;

import com.sherpa.mynelis.cigref.details.EventDetailsActivity;
import com.sherpa.mynelis.cigref.details.EventDetailsFragment;
import com.sherpa.mynelis.cigref.model.Event;

/**
 * Created by jacques on 18/10/2017.
 */
public class EventServices {

    /**
     * Open client mail to share an event campaign by mail
     * @param event
     */
    public static void sendEventByMail(Context context, Event event){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        String contentPlain = context.getResources().getString(R.string.transfert_mail_content_plain);
        String formattedContentPlain = String.format(contentPlain, event.getTitle(), event.getDescription(), event.getFormattedAddress(), event.getEventDate().getDate());

        String contentHTML = context.getResources().getString(R.string.transfert_mail_content_html);
        String formattedContentHTML = String.format(contentHTML, event.getTitle(), event.getDescription(), event.getFormattedAddress(), event.getEventDate().getDate());

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
    public static void goToEventDetail(Context context, Event event){
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(EventDetailsFragment.EVENT_ARGUMENT_KEY, event);
        context.startActivity(intent);
    }

}
