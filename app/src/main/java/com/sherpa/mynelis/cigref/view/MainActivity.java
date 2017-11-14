package com.sherpa.mynelis.cigref.view;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

    private static final String PERMISSIONS_PREFERENCES = "permissions_preferences";
    private static final String PERMISSIONS_PREFERENCES_CALENDAR_KEY = "calendar_checked";
    private static final int REQUEST_CALENDAR_ID = 1;
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
            if (message != null) {
                UtilsService.showErrorAlert(this, message);
            }
        });

        changeFragment(new EventsFragment(), getResources().getString(R.string.title_events));
        checkPermissions();
    }

    private void checkPermissions() {
        SharedPreferences settings = getSharedPreferences(PERMISSIONS_PREFERENCES, 0);
        if (settings.getBoolean(PERMISSIONS_PREFERENCES_CALENDAR_KEY, false) && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, REQUEST_CALENDAR_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALENDAR_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                SharedPreferences settings = getSharedPreferences(PERMISSIONS_PREFERENCES, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(PERMISSIONS_PREFERENCES_CALENDAR_KEY, true);
                editor.commit();
                return;
            }
        }
    }

    private void changeFragment(Fragment fragment, String title) {
        getSupportActionBar().setTitle(title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof BackNavitationListerner) {
            if (((BackNavitationListerner) currentFragment).onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

    public interface BackNavitationListerner {
        boolean onBackPressed();
    }
}
