package com.sherpa.mynelis.cigref.view.details;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.View;
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
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventCampaignService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.service.UtilsService;
import com.sherpa.mynelis.cigref.view.events.EventsFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EVENT_ARGUMENT_KEY = "event";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int DESCRIPTION_EXPANDED_MAX_LINE = 100;
    private static final int DESCRIPTION_COLLAPSED_MAX_LINE = 4;

    private CampaignModel mEvent = null;
    private TextView mSeeMore;
    private MapView mMapView;
    private TextView eventIsRegistered;
    private ImageButton goButton;
    private ImageButton notGoButton;
    private CampaignEventViewModel campaignViewModel;
    private Date today = new Date();
    private boolean firstIgnored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Intent intent = getIntent();
        mEvent = (CampaignModel) intent.getSerializableExtra(EVENT_ARGUMENT_KEY);
        EventsFragment frag = (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.event_main_fragment);
        campaignViewModel = ViewModelProviders.of(this).get(CampaignEventViewModel.class);


        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
            if (firstIgnored) {
                mEvent = Stream.of(campaignModels).filter(a -> a.getIdNelis() == mEvent.getIdNelis()).findFirst().get();
                this.updateInvitationInfo();
                this.setRegisteredButtons();
            }
            firstIgnored = true;
        });

        EventCampaignRepository.getInstance().getLoadingError().observe(this, message -> {
            if (message != null) {
                UtilsService.showErrorAlert(this, message);
            }
        });

        mMapView = (MapView) findViewById(R.id.mapView);
        if (mEvent.getEventPlace() == null || mEvent.getEventPlace().isEmpty()) {
            mMapView.setVisibility(View.GONE);
        }

        this.initMapView(mMapView, savedInstanceState);
        this.updateInvitationInfo();
        this.setHeaderEventInfo();
        this.setContactList();
        this.setRegisteredButtons();
        this.setMailSendButton();
        this.setEventDetails();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    private void initMapView(MapView mMapView, Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.scrollGesturesEnabled(false);
    }

    /**
     * Set guest count label text
     */
    private void updateInvitationInfo() {
        TextView eventRegisteredCount = (TextView) findViewById(R.id.eventDetailsRegisteredCount);
        eventRegisteredCount.setText(getString(R.string.event_details_participant_count, mEvent.getGuestCount()));
    }

    /**
     * Set title, event type and image
     */
    private void setHeaderEventInfo() {
        final ImageView eventImage = (ImageView) findViewById(R.id.eventDetailsImage);
        TextView eventTitle = (TextView) findViewById(R.id.eventDetailsTitle);
        TextView eventType = (TextView) findViewById(R.id.eventDetailsType);
        String url = EventCampaignService.getInstance().buildEventPosterURL(mEvent.getIdNelis());
        Picasso.with(this).load(url).fit().into(eventImage);
        eventTitle.setText(mEvent.getTitle());
        eventType.setText(mEvent.getType().getLabelFr());
    }

    /**
     * Set the guest pictures
     */
    private void setContactList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_ARGUMENT_KEY, mEvent);
        ContactListFragment contactListFragment = new ContactListFragment();
        contactListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.eventDetailsRegisteredList, contactListFragment).commit();
    }

    /**
     * Set the accepte / refuse buttons status
     */
    private void setRegisteredButtons() {
        eventIsRegistered = (TextView) findViewById(R.id.eventDetailsIsRegistered);
        goButton = (ImageButton) findViewById(R.id.eventDetailsGo);
        notGoButton = (ImageButton) findViewById(R.id.eventDetailsNotGo);

        if (mEvent.getClosedDate().after(today)) {
            goButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventCampaignRepository.getInstance().changeInvitationStatus(mEvent, InvitationStatus.ACCEPTED, EventDetailsActivity.this);
                }
            });

            notGoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventCampaignRepository.getInstance().changeInvitationStatus(mEvent, InvitationStatus.REFUSED, EventDetailsActivity.this);
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
            goButton.setVisibility(View.VISIBLE);
            notGoButton.setVisibility(View.VISIBLE);
        } else {
            goButton.setVisibility(View.GONE);
            notGoButton.setVisibility(View.GONE);
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

    /**
     * Attach action to send mail button
     */
    private void setMailSendButton() {
        ImageButton mailSendButton = (ImageButton) findViewById(R.id.eventDetailsSendMail);
        mailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventServices.sendEventByMail(EventDetailsActivity.this, mEvent);
            }
        });
    }

    /**
     * Set event description label
     */
    private void setShowDescriptionDetailAction(TextView eventDescription) {
        mSeeMore = (TextView) findViewById(R.id.eventDetailsShowDescription);
        mSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventDescription.getMaxLines() == DESCRIPTION_COLLAPSED_MAX_LINE) {
                    eventDescription.setMaxLines(DESCRIPTION_EXPANDED_MAX_LINE);
                    mSeeMore.setText(getString(R.string.general_see_less));
                } else {
                    eventDescription.setMaxLines(DESCRIPTION_COLLAPSED_MAX_LINE);
                    mSeeMore.setText(getString(R.string.general_see_more));
                }
            }
        });
    }

    /**
     * Set organizer, place and date labels
     */
    private void setEventDetails() {
        final TextView eventDescription = (TextView) findViewById(R.id.eventDetailsDescription);
        this.setShowDescriptionDetailAction(eventDescription);

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

        if (mEvent.getEventDate() != null) {
            String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(mEvent.getClosedDate());
            eventDate.setText(date + " " + mEvent.getEventDate());
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
                int maxLine = eventDescription.getMaxLines();
                if (maxLine != DESCRIPTION_EXPANDED_MAX_LINE) {
                    if (ellipsisCount > 0) {
                        mSeeMore.setVisibility(View.VISIBLE);
                    } else {
                        mSeeMore.setVisibility(View.INVISIBLE);
                    }
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
                mMapView.setVisibility(View.VISIBLE);
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