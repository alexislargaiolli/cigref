package com.sherpa.mynelis.cigref.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sherpa.mynelis.cigref.R;
import com.sherpa.mynelis.cigref.data.EventCampaignRepository;
import com.sherpa.mynelis.cigref.service.UtilsService;
import com.sherpa.mynelis.cigref.utils.BottomNavigationViewHelper;
import com.sherpa.mynelis.cigref.view.agenda.AgendaFragment;
import com.sherpa.mynelis.cigref.view.events.EventsFragment;
import com.sherpa.mynelis.cigref.view.search.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;

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
                case R.id.navigation_search:
                    changeFragment(new SearchFragment(), item.getTitle().toString());
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        EventCampaignRepository.getInstance().getLoadingError().observe(this, message -> {
            if(message != null) {
                UtilsService.showErrorAlert(this, message);
            }
        });

        changeFragment(new EventsFragment(), getResources().getString(R.string.title_events));
    }

    private void changeFragment(Fragment fragment, String title){
        getSupportActionBar().setTitle(title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if(currentFragment instanceof BackNavitationListerner){
            if(((BackNavitationListerner) currentFragment).onBackPressed()){
                return;
            }
        }
        super.onBackPressed();
    }

    public interface BackNavitationListerner{
        boolean onBackPressed();
    }
}
