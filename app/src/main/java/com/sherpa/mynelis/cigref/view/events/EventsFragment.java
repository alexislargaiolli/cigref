package com.sherpa.mynelis.cigref.view.events;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.view.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment implements MainActivity.BackNavitationListerner {
    EventTabAdapter tabAdapter;

    public EventsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        tabAdapter = new EventTabAdapter(getActivity(), getChildFragmentManager());

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(tabAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        return tabAdapter.onBackPressed();
    }
}