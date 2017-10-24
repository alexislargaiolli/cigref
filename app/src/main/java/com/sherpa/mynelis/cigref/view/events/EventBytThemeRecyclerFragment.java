package com.sherpa.mynelis.cigref.view.events;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.model.campaign.CampaignTypeModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnEventByThemeFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventBytThemeRecyclerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventBytThemeRecyclerFragment extends Fragment {
    private static final String ARG_THEME = "theme";
    private OnEventByThemeFragmentInteractionListener mListener;
    private CampaignTypeModel selectedTheme;

    public EventBytThemeRecyclerFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param theme the selected theme to use to filter campaign
     * @return A new instance of fragment EventBytThemeRecyclerFragment.
     */
    public static EventBytThemeRecyclerFragment newInstance(CampaignTypeModel theme) {
        EventBytThemeRecyclerFragment fragment = new EventBytThemeRecyclerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_THEME, theme);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedTheme = (CampaignTypeModel) getArguments().getSerializable(ARG_THEME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_byt_theme_recycler, container, false);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnEventByThemeFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
