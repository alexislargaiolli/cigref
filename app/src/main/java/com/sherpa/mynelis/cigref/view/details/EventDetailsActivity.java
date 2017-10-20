package com.sherpa.mynelis.cigref.view.details;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;
import com.sherpa.mynelis.cigref.service.UtilsService;
import com.sherpa.mynelis.cigref.view.events.InvitationStatusEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sherpa.mynelis.cigref.R;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private CampaignModel mEvent;
    private ArrayList<Invitation> mInvitations;
    private Invitation mMyInvitation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

//        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.eventDetails);
//        mSlidingUpPanelLayout.setTouchEnabled(false);

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
        mEvent = (CampaignModel) intent.getSerializableExtra(EventDetailsFragment.EVENT_ARGUMENT_KEY);
        mInvitations = (ArrayList<Invitation>) intent.getSerializableExtra(EventDetailsFragment.INVITATIONS_ARGUMENT_KEY);
        mMyInvitation = (Invitation) intent.getSerializableExtra(EventDetailsFragment.MY_INVITATION_ARGUMENT_KEY);

        Bundle bundle = new Bundle();
        bundle.putSerializable(EventDetailsFragment.EVENT_ARGUMENT_KEY, mEvent);
        bundle.putSerializable(EventDetailsFragment.INVITATIONS_ARGUMENT_KEY, mInvitations);
        bundle.putSerializable(EventDetailsFragment.MY_INVITATION_ARGUMENT_KEY, mMyInvitation);

        final EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
        eventDetailsFragment.setArguments(bundle);
        eventDetailsFragment.setInvitationStatusEventListener(new InvitationStatusEventListener() {
            @Override
            public void onUpdateInvitationStatus(Invitation invitation, final InvitationStatus status) {
                final InvitationStatus previousStatus = status;
                eventDetailsFragment.setInvitationInfo(status);
                EventCampaignService.updateInvitationStatus(mMyInvitation.getId(), status, new ServiceResponse<Invitation>() {
                    @Override
                    public void onSuccess(Invitation datas) {
                        mMyInvitation = datas;
                        eventDetailsFragment.setmMyInvitation(datas);
                        if(InvitationStatus.ACCEPTED.equals(status)) {
                            EventServices.showEventRegisterSuccessAlert(EventDetailsActivity.this, mEvent);
                        }
                    }

                    @Override
                    public void onError(ServiceReponseErrorType error, String errorMessage) {
                        eventDetailsFragment.setInvitationInfo(previousStatus);
                        UtilsService.showErrorAlert(EventDetailsActivity.this, getString(R.string.error_invitation_update));
                    }
                });
            }
        });

//        InvitationFragment invitationFragment = new InvitationFragment();
//        invitationFragment.setArguments(bundle);
//        invitationFragment.setInvitationEvent(new InvitationFragment.InvitationEvent() {
//            @Override
//            public void onAddToCalendar() {
//                if (ActivityCompat.checkSelfPermission(EventDetailsActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(EventDetailsActivity.this,
//                            new String[]{Manifest.permission.WRITE_CALENDAR},
//                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//                } else {
//                    addToCalendar();
//                }
//            }
//        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.eventDetailsContainer, eventDetailsFragment).commit();
//                .add(R.id.eventDetailsInvitationContainer, invitationFragment).commit();

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