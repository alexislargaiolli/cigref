package com.sherpa.mynelis.cigref.view;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.BuildConfig;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.AuthenticationService;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.view.events.CampaignEventAdpader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements MainActivity.BackNavitationListerner{

    private CampaignEventViewModel campaignViewModel;
    private RecyclerView mRecyclerView;
    private CampaignEventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private float bottomNavigationElevation;
    private int disconnectLabelClickCount = 0;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view_profil);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CampaignEventAdpader(new ArrayList<CampaignModel>());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setEventListener(new CampaignEventAdpader.EventListener() {
            @Override
            public void onEventSelected(CampaignModel eventCampaign) {
                EventServices.goToEventDetail(getContext(), eventCampaign);
            }

            @Override
            public void onInvitationStatusChanged(final int position, final CampaignModel eventCampaign, final InvitationStatus status) {
                EventCampaignRepository.getInstance().changeInvitationStatus(eventCampaign, status, getContext());
            }
        });
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -6);
        Date today = calendar.getTime();
        campaignViewModel = ViewModelProviders.of(getActivity()).get(CampaignEventViewModel.class);
        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
            List<CampaignModel> acceptedCampaigns = Stream.of(campaignModels).filter(c-> c.getClosedDate().after(today) && c.isAccepted()).toList();
            mAdapter.setmDataset(acceptedCampaigns);
            mAdapter.notifyDataSetChanged();
        });

        ImageButton logoutBtn = (ImageButton) view.findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });

        SwipeRefreshLayout mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        EventCampaignRepository.getInstance().fullLoad(true);
                    }
                }
        );

        campaignViewModel.getCampaignLoading().observe(this, loading -> {
            mySwipeRefreshLayout.setRefreshing(loading);
        });

        TextView disconnectLabel = view.findViewById(R.id.disconnectLabel);
        TextView buildNumberLabel = view.findViewById(R.id.buildNumberLabel);
        disconnectLabel.setOnClickListener(e -> {
            disconnectLabelClickCount++;
            if(disconnectLabelClickCount == 5){
                buildNumberLabel.setText("Build " + BuildConfig.VERSION_NAME);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bar);
            bottomNavigationElevation = navigation.getElevation();
            navigation.setElevation(0);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bar);
            navigation.setElevation(bottomNavigationElevation);
        }
    }

    /**
     * Show a logout confirmation dialog
     */
    private void confirmLogout() {
        new AlertDialog.Builder(this.getActivity())
                .setMessage(getActivity().getResources().getString(R.string.profil_logout_confirm))
                .setCancelable(false)
                .setPositiveButton(getActivity().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton(getActivity().getResources().getString(R.string.no), null)
                .show();
    }

    /**
     * Call when user has confirmed logout
     */
    private void logout(){
        AuthenticationService.getInstance().logout(getActivity());
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
