package com.sherpa.mynelis.cigref.details;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sherpa.mynelis.cigref.EventServices;
import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sherpa.mynelis.cigref.R;

public class EventDetailsActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.eventDetails);
        mSlidingUpPanelLayout.setTouchEnabled(false);

        // With this we can close invitation slide by clicked to overlay. B
        // ut its only work with touchEnable(true).
        // And with touchEnable(true), slide would be closed when user touch it
//        mSlidingUpPanelLayout.setFadeOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }
//        });

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra(EventDetailsFragment.EVENT_ARGUMENT_KEY);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EventDetailsFragment.EVENT_ARGUMENT_KEY, event);

        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        eventDetailsFragment.setArguments(bundle);

        InvitationFragment invitationFragment = new InvitationFragment();
        invitationFragment.setArguments(bundle);
        invitationFragment.setInvitationEvent(new InvitationFragment.InvitationEvent() {
            @Override
            public void onAddToCalendar() {
                if (ActivityCompat.checkSelfPermission(EventDetailsActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EventDetailsActivity.this,
                            new String[]{Manifest.permission.WRITE_CALENDAR},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    addToCalendar();
                }
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.eventDetailsContainer, eventDetailsFragment)
                .add(R.id.eventDetailsInvitationContainer, invitationFragment).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addToCalendar();

                }
                return;
            }
        }
    }

    private void addToCalendar() {
        EventServices.addEventToCalendar(EventDetailsActivity.this, event);
        showConfirmEventAddedToCalendar();
    }

    private void showConfirmEventAddedToCalendar() {
        new AlertDialog.Builder(EventDetailsActivity.this)
                .setMessage(EventDetailsActivity.this.getResources().getString(R.string.add_to_calandar_success))
                .setCancelable(true)
                .show();
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            int lastBackStackEntryIndex = backStackEntryCount - 1;
            if (getSupportFragmentManager().getBackStackEntryAt(lastBackStackEntryIndex).getName().equals(InvitationFragment.BACK_STACK_OPENED_NAME)) {
                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                getSupportFragmentManager().popBackStack(InvitationFragment.BACK_STACK_OPENED_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return;
            }
        }

        super.onBackPressed();
    }
}
