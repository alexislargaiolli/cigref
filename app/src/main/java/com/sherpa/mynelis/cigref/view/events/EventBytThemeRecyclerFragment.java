package com.sherpa.mynelis.cigref.view.events;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEventByThemeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventBytThemeRecyclerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventBytThemeRecyclerFragment extends Fragment {
    public static final String ARG_THEME = "theme";
    private OnEventByThemeFragmentInteractionListener mListener;
    private CampaignTypeModel selectedTheme;
    private CampaignEventAdpader mAdapter;
    private CampaignEventViewModel campaignViewModel;
    private TextView selectedThemeTitle;
    private TextView emptyMessage;
    private Date today;

    public EventBytThemeRecyclerFragment() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -6);
        today = calendar.getTime();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param theme the selected theme to use to filter campaign
     * @return A new instance of fragment EventBytThemeRecyclerFragment.
     */
    public static EventBytThemeRecyclerFragment newInstance(CampaignTypeModel theme) {
        EventBytThemeRecyclerFragment fragment = new EventBytThemeRecyclerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_THEME, theme);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedTheme = (CampaignTypeModel) getArguments().getSerializable(ARG_THEME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_byt_theme_recycler, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.event_by_theme_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        selectedThemeTitle = (TextView) view.findViewById(R.id.selectedThemeTitle);
        emptyMessage = (TextView) view.findViewById(R.id.emptyMessage);

        mAdapter = new CampaignEventAdpader(new ArrayList<CampaignModel>());
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

        mRecyclerView.setAdapter(mAdapter);

        campaignViewModel = ViewModelProviders.of(getActivity()).get(CampaignEventViewModel.class);
        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
            updateTheme(campaignModels);
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

        return view;
    }

    private void updateTheme(List<CampaignModel> allCampaigns){
        List<CampaignModel> campaignsByTheme = Stream.of(allCampaigns).
                filter(c -> c.getClosedDate().after(today) && c.getType().getIdNelis() == selectedTheme.getIdNelis())
                .toList();
        mAdapter.setmDataset(campaignsByTheme);
        mAdapter.notifyDataSetChanged();
        selectedThemeTitle.setText(selectedTheme.getLabelFr());
        if(campaignsByTheme.isEmpty()){
            emptyMessage.setVisibility(View.VISIBLE);
        }
        else{
            emptyMessage.setVisibility(View.GONE);
        }
    }

    public void selectTheme(CampaignTypeModel theme){
        this.selectedTheme = theme;
        updateTheme(campaignViewModel.getCampaignsObservable().getValue());
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEventByThemeFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
