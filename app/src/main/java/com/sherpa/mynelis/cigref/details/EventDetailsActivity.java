package com.sherpa.mynelis.cigref.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sherpa.mynelis.cigref.model.Event;
import com.sherpa.mynelis.cigref.model.EventFactory;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sherpa.mynelis.cigref.R;

public class EventDetailsActivity extends AppCompatActivity {

    private SlidingUpPanelLayout mSlidingUpPanelLayout;

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
        Event event = (Event) intent.getSerializableExtra(EventDetailsFragment.EVENT_ARGUMENT_KEY);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EventDetailsFragment.EVENT_ARGUMENT_KEY, event);

        EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        eventDetailsFragment.setArguments(bundle);

        InvitationFragment invitationFragment = new InvitationFragment();
        invitationFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.eventDetailsContainer, eventDetailsFragment)
                .add(R.id.eventDetailsInvitationContainer, invitationFragment).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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
