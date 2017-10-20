package com.sherpa.mynelis.cigref.view.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.invitations.Invitation;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ContactListFragment extends Fragment {
    private ArrayList<Invitation> mInvitations;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mInvitations = (ArrayList<Invitation>) getArguments().getSerializable(EventDetailsFragment.INVITATIONS_ARGUMENT_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details_contact_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            updateContactList(recyclerView);
        }
        return view;
    }

    public void updateContactList(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ContactListRecyclerViewAdapter(mInvitations, getContext()));
    }
}
