package com.sherpa.mynelis.cigref;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SampleAdapter extends FragmentPagerAdapter {
    Context ctxt=null;

    public SampleAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);
        this.ctxt=ctxt;
    }

    @Override
    public int getCount() {
        return(2);
    }

    @Override
    public Fragment getItem(int position) {
//        return(EditorFragment.newInstance(position));
        switch(position){
            case 0 :
                return new EventByDateFragment();
            case 1 :
                return new EventByPopularityFragment();
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        switch(position){
            case 0 :
                return "By date";
            case 1 :
                return "By pop";
        }
        return null;
    }
}