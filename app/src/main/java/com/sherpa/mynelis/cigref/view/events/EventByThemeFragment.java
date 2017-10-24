package com.sherpa.mynelis.cigref.view.events;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.annimon.stream.Stream;
import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;
import com.sherpa.mynelis.cigref.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that let get event list by selecting a theme among a list of theme
 * Use the {@link EventByThemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventByThemeFragment extends Fragment implements ThemeListFragment.ThemeSelectListener, EventBytThemeRecyclerFragment.OnEventByThemeFragmentInteractionListener, MainActivity.BackNavitationListerner{

    private CampaignEventViewModel campaignViewModel;
    private CampaignEventAdpader mAdapter;
    private FrameLayout themeListContainer;
    private RecyclerView mRecyclerView;
    private CampaignTypeModel currentTheme;
    private EventBytThemeRecyclerFragment eventBytThemeRecyclerFragment;
            ThemeListFragment themeListFragment;

    public EventByThemeFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EventByThemeFragment.
     */
    public static EventByThemeFragment newInstance() {
        EventByThemeFragment fragment = new EventByThemeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        themeListFragment = new ThemeListFragment();
        themeListFragment.setSelectListener(this);
        getFragmentManager().beginTransaction().add(R.id.by_theme_container, themeListFragment).addToBackStack("Liste").commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_by_theme, container, false);

        themeListContainer = (FrameLayout) view.findViewById(R.id.by_theme_container);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.event_by_theme_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(container.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CampaignEventAdpader(new ArrayList<CampaignModel>());

        mRecyclerView.setAdapter(mAdapter);

        campaignViewModel = ViewModelProviders.of(getActivity()).get(CampaignEventViewModel.class);

        campaignViewModel.getCampaignsObservable().observe(this, campaignModels -> {

        });

        return view;
    }

    @Override
    public void onThemeSelect(CampaignTypeModel theme) {
        currentTheme = theme;
        List<CampaignModel> campaigns = Stream.of(campaignViewModel.getCampaignsObservable().getValue()).filter(t -> t.getType().getIdNelis() == theme.getIdNelis()).toList();
//        mAdapter.setmDataset(campaigns);
//        mAdapter.notifyDataSetChanged();
//        themeListContainer.setVisibility(View.GONE);
//        mRecyclerView.setVisibility(View.VISIBLE);

        if(eventBytThemeRecyclerFragment == null) {
            eventBytThemeRecyclerFragment = EventBytThemeRecyclerFragment.newInstance(theme);
            Bundle bundle = new Bundle();
            bundle.putSerializable(EventBytThemeRecyclerFragment.ARG_THEME, theme);
            eventBytThemeRecyclerFragment.setArguments(bundle);
        }
        else{
            eventBytThemeRecyclerFragment.selectTheme(currentTheme);
        }
        getFragmentManager().beginTransaction().replace(R.id.by_theme_container, eventBytThemeRecyclerFragment).addToBackStack("Liste").commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onBackPressed() {
        if(currentTheme != null){
            currentTheme = null;
            getFragmentManager().beginTransaction().replace(R.id.by_theme_container, themeListFragment).addToBackStack("Liste").commit();
            return true;
        }
        return false;
    }
}
