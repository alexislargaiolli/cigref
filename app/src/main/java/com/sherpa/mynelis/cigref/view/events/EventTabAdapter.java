package com.sherpa.mynelis.cigref.view.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sherpa.mynelis.cigref.R;


public class EventTabAdapter extends FragmentPagerAdapter {
    Context ctxt=null;
    EventByDateFragment eventByDateFragment;
    EventByPopularityFragment eventByPopularityFragment;

    public EventTabAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
        eventByDateFragment = new EventByDateFragment();
        eventByPopularityFragment = new EventByPopularityFragment();
    }

    @Override
    public int getCount() {
        return(2);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 :
                return eventByDateFragment;
            case 1 :
                return eventByPopularityFragment;
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        switch(position){
            case 0 :
                return ctxt.getResources().getString(R.string.title_event_bydate);
//            case 1 :
//                return ctxt.getResources().getString(R.string.title_event_bytheme);
            case 1 :
                return ctxt.getResources().getString(R.string.title_event_bypopularity);
        }
        return null;
    }

    public EventByDateFragment getEventByDateFragment() {
        return eventByDateFragment;
    }

    public void setEventByDateFragment(EventByDateFragment eventByDateFragment) {
        this.eventByDateFragment = eventByDateFragment;
    }

    public EventByPopularityFragment getEventByPopularityFragment() {
        return eventByPopularityFragment;
    }

    public void setEventByPopularityFragment(EventByPopularityFragment eventByPopularityFragment) {
        this.eventByPopularityFragment = eventByPopularityFragment;
    }
}