package com.sherpa.mynelis.cigref.view.search;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;
import com.sherpa.mynelis.cigref.model.invitations.InvitationStatus;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.view.events.CampaignEventAdpader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private CampaignEventAdpader mAdapter;
    private CampaignEventViewModel campaignViewModel;
    private String searchPattern;
    private LinearLayout noResultView;
    private LinearLayout beforeSearchView;
    Date today;

    public SearchFragment() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -6);
        today = calendar.getTime();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        noResultView = view.findViewById(R.id.noResultView);
        noResultView.setVisibility(View.GONE);
        beforeSearchView = view.findViewById(R.id.beforeSearchView);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.event_search_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        EditText searchField = (EditText) view.findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString(), campaignViewModel.getCampaignsObservable().getValue());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
            if(this.searchPattern != null && !this.searchPattern.isEmpty()){
                search(this.searchPattern, campaignModels);
            }
        });

        return view;
    }

    private void search(String text, List<CampaignModel> allCampaigns){
        beforeSearchView.setVisibility(View.GONE);
        this.searchPattern = text;
        List<CampaignModel> results = null;
        if(text != null && !text.isEmpty() && allCampaigns != null) {
             results = Stream.of(allCampaigns).filter(c -> {
                return c.getClosedDate().after(today) && (c.getTitle().toLowerCase().contains(text)
                        || c.getType().getLabelFr().toLowerCase().contains(text)
                        || (c.getDescription() != null ? c.getDescription().toLowerCase().contains(text) : false)
                        || (c.getEventOrganizer() != null ? c.getEventOrganizer().toLowerCase().contains(text) : false)
                        || (c.getEventPlace() != null ? c.getEventPlace().toLowerCase().contains(text) : false));
            }).toList();
        }
        else{
            results = new ArrayList<CampaignModel>();
        }
        if(results.isEmpty()){
            noResultView.setVisibility(View.VISIBLE);
        }
        else{
            noResultView.setVisibility(View.GONE);
        }
        mAdapter.setmDataset(results);
        mAdapter.notifyDataSetChanged();
    }

}
