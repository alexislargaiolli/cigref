package com.sherpa.mynelis.cigref.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sherpa.mynelis.cigref.R;

import static android.support.v4.content.res.TypedArrayUtils.getString;


public class EventTabAdapter extends FragmentPagerAdapter {
    Context ctxt=null;
    EventByDateFragment byDate;
    EventByPopularityFragment byPop;

    public EventTabAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
        byDate = new EventByDateFragment();
        byPop = new EventByPopularityFragment();
    }

    @Override
    public int getCount() {
        return(2);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 :
                return byDate;
            case 1 :
                return byPop;
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
}