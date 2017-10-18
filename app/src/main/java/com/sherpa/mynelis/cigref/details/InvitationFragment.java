package com.sherpa.mynelis.cigref.details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.sherpa.mynelis.cigref.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvitationFragment extends Fragment {

    public static String BACK_STACK_OPENED_NAME = "invation_opened";

    private View mRootView;

    public InvitationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_event_details_invitation, container, false);

        this.setIGoButtonListener();
        this.addEventToCalendar();

        return mRootView;
    }

    private void addEventToCalendar() {
        TextView addToCalendar = (TextView)mRootView.findViewById(R.id.invitationAddToCalendar);
        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add to calendar
            }
        });
    }

    private void setIGoButtonListener() {
        Button iGoButton = (Button)mRootView.findViewById(R.id.invitationIGoButton);
        iGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO call API
                boolean isRegistered = true;
                if (isRegistered) {
                    // TODO sendData to parent fragment
                    // TODO select buttons
                    // TODO change i go text
                } else {
                    // TODO error message ?
                }
            }
        });
    }

}
