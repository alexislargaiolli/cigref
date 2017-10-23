package com.sherpa.mynelis.cigref.view.details;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.ServiceResponse;
import com.sherpa.mynelis.cigref.service.UtilsService;
import com.sherpa.mynelis.cigref.view.events.EventsFragment;
import com.sherpa.mynelis.cigref.view.events.InvitationStatusEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sherpa.mynelis.cigref.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EVENT_ARGUMENT_KEY = "event";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private int eventId;
    private CampaignModel mEvent = null;
    private TextView mSeeMore;
    private MapView mMapView;
    private TextView eventIsRegistered;
    private ImageButton goButton;
    private ImageButton notGoButton;
    private CampaignEventViewModel campaignViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();
        mEvent = (CampaignModel) intent.getSerializableExtra(EVENT_ARGUMENT_KEY);
        System.out.println("Init value "+mEvent.getInvitations().size());
        EventsFragment frag = (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.event_main_fragment);
        campaignViewModel = ViewModelProviders.of(this).get(CampaignEventViewModel.class);
        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
            System.out.println("EventDetailsActivity observe");
            mEvent = campaignModels.stream().filter(a -> a.getIdNelis() == mEvent.getIdNelis()).findFirst().get();
            System.out.println("Updated "+mEvent.getInvitations().size());
            this.updateInvitationInfo();
            this.setRegisteredButtons();
        });

        this.initMapView(savedInstanceState);
        this.updateInvitationInfo();
        this.setHeaderEventInfo();
        this.setContactList();
        this.setRegisteredButtons();
        this.setMailSendButton();
        this.setEventDetails();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    private void initMapView(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.scrollGesturesEnabled(false);
    }

    private void updateInvitationInfo() {
        TextView eventRegisteredCount = (TextView) findViewById(R.id.eventDetailsRegisteredCount);
        eventRegisteredCount.setText(getString(R.string.event_details_participant_count, mEvent.getInvitations().size()));
    }

    private void setHeaderEventInfo() {
        final ImageView eventImage = (ImageView) findViewById(R.id.eventDetailsImage);
        TextView eventTitle = (TextView) findViewById(R.id.eventDetailsTitle);
        TextView eventType = (TextView) findViewById(R.id.eventDetailsType);
        System.out.println(mEvent.getPosterUrl());
        Picasso.with(this).load(mEvent.getPosterUrl()).into(eventImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                eventImage.setImageResource(R.drawable.forum);
            }
        });
        eventTitle.setText(mEvent.getTitle());
        eventType.setText(mEvent.getType().getLabelFr());
    }

    private void setContactList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_ARGUMENT_KEY, mEvent);
        ContactListFragment contactListFragment = new ContactListFragment();
        contactListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.eventDetailsRegisteredList, contactListFragment).commit();
    }

    private void setRegisteredButtons() {
//        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) getActivity().findViewById(R.id.eventDetails);
        eventIsRegistered = (TextView) findViewById(R.id.eventDetailsIsRegistered);
        goButton = (ImageButton) findViewById(R.id.eventDetailsGo);
        notGoButton = (ImageButton) findViewById(R.id.eventDetailsNotGo);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventCampaignRepository.getInstance().changeInvitationStatus(mEvent, InvitationStatus.ACCEPTED);
            }
        });

        notGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventCampaignRepository.getInstance().changeInvitationStatus(mEvent, InvitationStatus.REFUSED);
            }
        });

        if (mEvent.getMyInvitation().getStatus().equals(InvitationStatus.ACCEPTED)) {
            goButton.setSelected(true);
            notGoButton.setSelected(false);
            eventIsRegistered.setText(getString(R.string.event_details_you_are_registered));
        } else {
            goButton.setSelected(false);
            notGoButton.setSelected(false);
            eventIsRegistered.setText(getString(R.string.event_details_are_you_going));
            if (mEvent.getMyInvitation().getStatus().equals(InvitationStatus.REFUSED)) {
                notGoButton.setSelected(true);
            }
        }
    }

    /**
     * Update yes button, no button and guest count message based on a given invitation status
     *
     * @param status the user invitation status
     */
    protected void setInvitationInfo(final InvitationStatus status) {
        goButton.setSelected(InvitationStatus.ACCEPTED.equals(status));
        notGoButton.setSelected(InvitationStatus.REFUSED.equals(status));
        int messageId = status != null && status.equals(InvitationStatus.ACCEPTED) ? R.string.event_details_you_are_registered : R.string.event_details_are_you_going;
        eventIsRegistered.setText(getString(messageId));
    }

    private void setMailSendButton() {
        ImageButton mailSendButton = (ImageButton) findViewById(R.id.eventDetailsSendMail);
        mailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventServices.sendEventByMail(EventDetailsActivity.this, mEvent);
            }
        });
    }

    private void setShowDescriptionDetailAction() {
        mSeeMore = (TextView) findViewById(R.id.eventDetailsShowDescription);
        mSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this);
                builder.setTitle(mEvent.getTitle()).setMessage(mEvent.getDescription());
                builder.setPositiveButton(R.string.general_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void setEventDetails() {
        final TextView eventDescription = (TextView) findViewById(R.id.eventDetailsDescription);
        this.setShowDescriptionDetailAction();

        TextView eventAddressName = (TextView) findViewById(R.id.eventDetailsAddressName);
        LinearLayout placeContainerView = (LinearLayout) findViewById(R.id.placeContainerView);


        TextView eventDate = (TextView) findViewById(R.id.eventDetailsDate);
        LinearLayout dateContainerView = (LinearLayout) findViewById(R.id.dateContainerView);

        TextView eventAnimator = (TextView) findViewById(R.id.eventDetailsAnimator);
        LinearLayout animatorContainerView = (LinearLayout) findViewById(R.id.animatorContainerView);

        if (mEvent.getDescription() != null && !mEvent.getDescription().isEmpty()) {
            eventDescription.setText(mEvent.getDescription());
            eventDescription.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            showHideSeeMore(eventDescription);
                        }
                    });
        } else {
            LinearLayout descriptionContainerView = (LinearLayout) findViewById(R.id.descriptionContainerView);
            descriptionContainerView.setVisibility(View.GONE);
        }

        if (mEvent.getEventPlace() != null && !mEvent.getEventPlace().isEmpty()) {
            eventAddressName.setText(mEvent.getEventPlace());
        } else {
            placeContainerView.setVisibility(View.GONE);
        }

        if (mEvent.getEventDate() != null && !mEvent.getEventDate().isEmpty()) {
            eventDate.setText(mEvent.getEventDate());
        } else {
            dateContainerView.setVisibility(View.GONE);
        }

        if (mEvent.getEventOrganizer() != null && !mEvent.getEventOrganizer().isEmpty()) {
            String animateBy = getString(R.string.event_details_animate_by) + " " + mEvent.getEventOrganizer();
            eventAnimator.setText(animateBy);
        } else {
            animatorContainerView.setVisibility(View.GONE);
        }
    }

    private void showHideSeeMore(TextView eventDescription) {
        Layout layout = eventDescription.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0) {
                int ellipsisCount = layout.getEllipsisCount(lines - 1);
                if (ellipsisCount > 0) {
                    mSeeMore.setVisibility(View.VISIBLE);
                } else {
                    mSeeMore.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        try {
            if (mEvent.getEventPlace() != null && !mEvent.getEventPlace().isEmpty()) {
                Geocoder geo = new Geocoder(this, Locale.getDefault());
                List<Address> adresses = geo.getFromLocationName(mEvent.getEventPlace(), 1);
                if (!adresses.isEmpty()) {
                    Address a = adresses.get(0);
                    LatLng position = new LatLng(a.getLatitude(), a.getLongitude());

                    map.addMarker(new MarkerOptions().position(position)
                            .title(mEvent.getEventPlace()));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
//        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//        if (backStackEntryCount > 0) {
//            int lastBackStackEntryIndex = backStackEntryCount - 1;
//            if (getSupportFragmentManager().getBackStackEntryAt(lastBackStackEntryIndex).getName().equals(InvitationFragment.BACK_STACK_OPENED_NAME)) {
//                mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//                getSupportFragmentManager().popBackStack(InvitationFragment.BACK_STACK_OPENED_NAME, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                return;
//            }
//        }

        super.onBackPressed();
    }
}