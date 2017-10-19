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
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDetailsFragment extends Fragment implements OnMapReadyCallback {
    public static String EVENT_ARGUMENT_KEY = "event";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private View mRootView;
    private CampaignModel mEvent = null;
    private TextView mSeeMore;
    private MapView mMapView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mEvent = (CampaignModel) bundle.getSerializable(EVENT_ARGUMENT_KEY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_event_details, container, false);

        ImageView eventImage = (ImageView) mRootView.findViewById(R.id.eventDetailsImage);
        TextView eventTitle = (TextView) mRootView.findViewById(R.id.eventDetailsTitle);
        TextView eventType = (TextView) mRootView.findViewById(R.id.eventDetailsType);
        TextView eventRegisteredCount = (TextView) mRootView.findViewById(R.id.eventDetailsRegisteredCount);

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

//        Picasso.with(getContext()).load(mEvent.getImageUrl()).into(eventImage);
        eventTitle.setText(mEvent.getTitle());
        eventType.setText(mEvent.getType().getLabelFr());
//        eventRegisteredCount.setText(getString(R.string.event_details_participant_count, mEvent.getParticpantsList().size()));
        eventRegisteredCount.setText(getString(R.string.event_details_participant_count, 0));
        this.setContactList();
        this.setRegisteredButtons();
        this.setMailSendButton();
        this.setEventDetails();

        return mRootView;
    }

    private void setContactList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EVENT_ARGUMENT_KEY, mEvent);

        ContactListFragment contactListFragment = new ContactListFragment();
        contactListFragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .add(R.id.eventDetailsRegisteredList, contactListFragment).commit();
    }

    private void setRegisteredButtons() {
        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) getActivity().findViewById(R.id.eventDetails);
        final TextView eventIsRegistered = (TextView) mRootView.findViewById(R.id.eventDetailsIsRegistered);
        final ImageButton goButton = (ImageButton) mRootView.findViewById(R.id.eventDetailsGo);
        final ImageButton notGoButton = (ImageButton) mRootView.findViewById(R.id.eventDetailsNotGo);

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                getFragmentManager().beginTransaction().addToBackStack(InvitationFragment.BACK_STACK_OPENED_NAME).commit();
            }
        });

        notGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO call API NOT GO
                goButton.setSelected(false);
                notGoButton.setSelected(true);
                eventIsRegistered.setText(getString(R.string.event_details_are_you_going));
            }
        });

//        if (mEvent.isRegistered()) {
        if(true){
            goButton.setSelected(true);
            notGoButton.setSelected(false);
            eventIsRegistered.setText(getString(R.string.event_details_you_are_registered));
        } else {
            goButton.setSelected(false);
            notGoButton.setSelected(false);
            eventIsRegistered.setText(getString(R.string.event_details_are_you_going));
//            if (mEvent.isNotGo()) {
//                notGoButton.setSelected(true);
//            }
        }
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
        TextView eventAddressStreet = (TextView) mRootView.findViewById(R.id.eventDetailsAddressStreet);
        TextView eventAddressCity = (TextView) mRootView.findViewById(R.id.eventDetailsAddressCity);

        TextView eventDate = (TextView) mRootView.findViewById(R.id.eventDetailsDate);
        TextView eventDateTime = (TextView) mRootView.findViewById(R.id.eventDetailsDateTime);

        TextView eventAnimator = (TextView) mRootView.findViewById(R.id.eventDetailsAnimator);

        eventDescription.setText(mEvent.getDescription());
        eventDescription.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        showHideSeeMore(eventDescription);
                    }
                });

        eventAddressName.setText(mEvent.getEventPlace());
//        eventAddressStreet.setText(mEvent.getEventAdress().getStreet());
//        eventAddressCity.setText(mEvent.getEventAdress().getPostalCodeWithCity());
//        eventDate.setText(mEvent.getEventDate().getDate());
//        eventDateTime.setText(mEvent.getEventDate().getTime());

        String animateBy = getString(R.string.event_details_animate_by) + " " + mEvent.getEventOrganizer();
        eventAnimator.setText(animateBy);
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
            if(mEvent.getEventPlace() != null && !mEvent.getEventPlace().isEmpty()) {
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
}
