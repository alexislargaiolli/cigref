package com.sherpa.mynelis.cigref.view.events;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.view.MainActivity;


class EventTabAdapter extends FragmentPagerAdapter implements MainActivity.BackNavitationListerner {
    private Context context = null;
    private EventByDateFragment eventByDateFragment;
    private EventByPopularityFragment eventByPopularityFragment;
    private EventByThemeFragment eventByThemeFragment;
    private Fragment currentFragment;

    EventTabAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        this.context = context;
        eventByDateFragment = new EventByDateFragment();
        eventByPopularityFragment = new EventByPopularityFragment();
        eventByThemeFragment = new EventByThemeFragment();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = getItem(position);
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return eventByDateFragment;
            case 1:
                return eventByThemeFragment;
            case 2:
                return eventByPopularityFragment;
        }
        return null;
    }

    @Override
    public String getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.title_event_bydate);
            case 1:
                return context.getResources().getString(R.string.title_event_bytheme);
            case 2:
                return context.getResources().getString(R.string.title_event_bypopularity);
        }
        return null;
    }

    @Override
    public boolean onBackPressed() {
        if (currentFragment != null && currentFragment instanceof MainActivity.BackNavitationListerner) {
            return ((MainActivity.BackNavitationListerner) currentFragment).onBackPressed();
        }
        return false;
    }
}