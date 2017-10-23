package com.sherpa.mynelis.cigref.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.CampaignEventViewModel;
import com.sherpa.mynelis.cigref.service.EventServices;
import com.sherpa.mynelis.cigref.view.agenda.AgendaFragment;
import com.sherpa.mynelis.cigref.view.details.EventDetailsFragment;
import com.sherpa.mynelis.cigref.view.events.EventsFragment;
import com.sherpa.mynelis.cigref.view.events.InvitationStatusEventListener;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    changeFragment(new EventsFragment(), item.getTitle().toString());
                    return true;
                case R.id.navigation_agenda:
                    changeFragment(new AgendaFragment(), item.getTitle().toString());
                    return true;
                case R.id.navigation_profile:
                    changeFragment(new ProfileFragment(), item.getTitle().toString());
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        changeFragment(new EventsFragment(), getResources().getString(R.string.title_events));
    }

    private void changeFragment(Fragment fragment, String title){
        getSupportActionBar().setTitle(title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}
