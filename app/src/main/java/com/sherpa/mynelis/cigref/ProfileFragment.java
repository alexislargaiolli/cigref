package com.sherpa.mynelis.cigref;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.sherpa.mynelis.cigref.events.EventAdpader;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private EventAdpader mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset = new String[]{"item1", "item2", "item3"};

    public ProfileFragment() {
        // Required empty public constructor
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
        mAdapter = new EventAdpader(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        ImageButton logoutBtn = (ImageButton) view.findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmLogout();
            }
        });

        return view;
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

    }


}
