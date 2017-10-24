package com.sherpa.mynelis.cigref.view.events;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThemeListFragment extends Fragment {
    private ListView mListView;
    private ThemeSelectListener selectListener;
    private CampaignEventViewModel campaignViewModel;
    private static CampaignTypeModel[] themes;

    public ThemeListFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme_list, container, false);

        mListView = (ListView) view.findViewById(R.id.theme_list);



        campaignViewModel = ViewModelProviders.of(getActivity()).get(CampaignEventViewModel.class);
        campaignViewModel.getThemesObservable().observe(this, new Observer<CampaignTypeModel[]>() {
            @Override
            public void onChanged(@Nullable CampaignTypeModel[] campaignTypeModels) {
                themes = campaignTypeModels;
                ThemeAdapter themeListAdapter = new ThemeAdapter(getContext(), campaignTypeModels);
                mListView.setAdapter(themeListAdapter);
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectListener != null){
                    selectListener.onThemeSelect(themes[position]);
                }
            }
        });
        return view;
    }

    interface ThemeSelectListener{
        void onThemeSelect(CampaignTypeModel theme);
    }

    public ThemeSelectListener getSelectListener() {
        return selectListener;
    }

    public void setSelectListener(ThemeSelectListener selectListener) {
        this.selectListener = selectListener;
    }
}
