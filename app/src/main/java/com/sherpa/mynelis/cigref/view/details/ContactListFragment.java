package com.sherpa.mynelis.cigref.view.details;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ContactListFragment extends Fragment {
    private CampaignModel mEvent;
    private CampaignEventViewModel campaignViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details_contact_list, container, false);
        mEvent = (CampaignModel) getArguments().getSerializable(EventDetailsActivity.EVENT_ARGUMENT_KEY);
        campaignViewModel = ViewModelProviders.of(this).get(CampaignEventViewModel.class);
        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            ContactListRecyclerViewAdapter adapter = new ContactListRecyclerViewAdapter(mEvent.getInvitations(), getContext());
            recyclerView.setAdapter(adapter);
            campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {
                System.out.println("ContactListFragment");
                mEvent = campaignModels.stream().filter(a -> a.getIdNelis() == mEvent.getIdNelis()).findFirst().get();
                adapter.setmContacts(mEvent.getInvitations());
                adapter.notifyDataSetChanged();
            });
        }
        return view;
    }

    public void updateContactList(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ContactListRecyclerViewAdapter(mEvent.getInvitations(), getContext()));
    }
}
