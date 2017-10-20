package com.sherpa.mynelis.cigref.view.details;

import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.view.events.InvitationStatusEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements OnMapReadyCallback {
    public static final String EVENT_ARGUMENT_KEY = "event";
    public static final String INVITATIONS_ARGUMENT_KEY = "invitations";
    public static final String MY_INVITATION_ARGUMENT_KEY = "my_invitation";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private View mRootView;
    private CampaignModel mEvent = null;
    private ArrayList<Invitation> mInvitations = null;
    private Invitation mMyInvitation = null;
    private TextView mSeeMore;
    private MapView mMapView;
    private TextView eventIsRegistered;
    private ImageButton goButton;
    private ImageButton notGoButton;

    private InvitationStatusEventListener invitationStatusEventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mEvent = (CampaignModel) bundle.getSerializable(EVENT_ARGUMENT_KEY);
        mInvitations = (ArrayList<Invitation>) bundle.getSerializable(INVITATIONS_ARGUMENT_KEY);
        mMyInvitation = (Invitation) bundle.getSerializable(MY_INVITATION_ARGUMENT_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        this.initMapView(savedInstanceState);
        this.updateInvitationInfo();

        this.setHeaderEventInfo();
        this.setContactList();
        this.setRegisteredButtons();
        this.setMailSendButton();
        this.setEventDetails();

        return mRootView;
    }

    private void initMapView(Bundle savedInstanceState) {
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) mRootView.findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        GoogleMapOptions options = new GoogleMapOptions();
        options.scrollGesturesEnabled(false);
    }

    private void updateInvitationInfo() {
        TextView eventRegisteredCount = (TextView) mRootView.findViewById(R.id.eventDetailsRegisteredCount);
        eventRegisteredCount.setText(getString(R.string.event_details_participant_count, mInvitations.size()));
    }

    private void setHeaderEventInfo() {
        final ImageView eventImage = (ImageView) mRootView.findViewById(R.id.eventDetailsImage);
        TextView eventTitle = (TextView) mRootView.findViewById(R.id.eventDetailsTitle);
        TextView eventType = (TextView) mRootView.findViewById(R.id.eventDetailsType);
        System.out.println(mEvent.getPosterUrl());
        Picasso.with(getContext()).load(mEvent.getPosterUrl()).into(eventImage, new Callback() {
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
        bundle.putSerializable(INVITATIONS_ARGUMENT_KEY, mInvitations);

        ContactListFragment contactListFragment = new ContactListFragment();
        contactListFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .add(R.id.eventDetailsRegisteredList, contactListFragment).commit();
    }

    private void setRegisteredButtons() {
//        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) getActivity().findViewById(R.id.eventDetails);
        eventIsRegistered = (TextView) mRootView.findViewById(R.id.eventDetailsIsRegistered);
        goButton = (ImageButton) mRootView.findViewById(R.id.eventDetailsGo);
        notGoButton = (ImageButton) mRootView.findViewById(R.id.eventDetailsNotGo);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitationStatusEventListener.onUpdateInvitationStatus(mMyInvitation, InvitationStatus.ACCEPTED);
            }
        });

        notGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invitationStatusEventListener.onUpdateInvitationStatus(mMyInvitation, InvitationStatus.REFUSED);
            }
        });

        if (mMyInvitation != null && mMyInvitation.getStatus().equals(InvitationStatus.ACCEPTED)) {
            goButton.setSelected(true);
            notGoButton.setSelected(false);
            eventIsRegistered.setText(getString(R.string.event_details_you_are_registered));
        } else {
            goButton.setSelected(false);
            notGoButton.setSelected(false);
            eventIsRegistered.setText(getString(R.string.event_details_are_you_going));
            if (mMyInvitation != null && mMyInvitation.getStatus().equals(InvitationStatus.REFUSED)) {
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
        ImageButton mailSendButton = (ImageButton) mRootView.findViewById(R.id.eventDetailsSendMail);
        mailSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventServices.sendEventByMail(getActivity(), mEvent);
            }
        });
    }

    private void setShowDescriptionDetailAction() {
        mSeeMore = (TextView) mRootView.findViewById(R.id.eventDetailsShowDescription);
        mSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        final TextView eventDescription = (TextView) mRootView.findViewById(R.id.eventDetailsDescription);
        this.setShowDescriptionDetailAction();

        TextView eventAddressName = (TextView) mRootView.findViewById(R.id.eventDetailsAddressName);
        LinearLayout placeContainerView = (LinearLayout) mRootView.findViewById(R.id.placeContainerView);


        TextView eventDate = (TextView) mRootView.findViewById(R.id.eventDetailsDate);
        LinearLayout dateContainerView = (LinearLayout) mRootView.findViewById(R.id.dateContainerView);

        TextView eventAnimator = (TextView) mRootView.findViewById(R.id.eventDetailsAnimator);
        LinearLayout animatorContainerView = (LinearLayout) mRootView.findViewById(R.id.animatorContainerView);

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
            LinearLayout descriptionContainerView = (LinearLayout) mRootView.findViewById(R.id.descriptionContainerView);
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
                Geocoder geo = new Geocoder(getContext(), Locale.getDefault());
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

    public void setInvitationStatusEventListener(InvitationStatusEventListener invitationStatusEventListener) {
        this.invitationStatusEventListener = invitationStatusEventListener;
    }

    public CampaignModel getmEvent() {
        return mEvent;
    }

    public void setmEvent(CampaignModel mEvent) {
        this.mEvent = mEvent;
    }

    public ArrayList<Invitation> getmInvitations() {
        return mInvitations;
    }

    public void setmInvitations(ArrayList<Invitation> mInvitations) {
        this.mInvitations = mInvitations;
    }

    public Invitation getmMyInvitation() {
        return mMyInvitation;
    }

    public void setmMyInvitation(Invitation mMyInvitation) {
        this.mMyInvitation = mMyInvitation;
    }
}
