package ss18.mc.positime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import android.widget.TextView;

import ss18.mc.positime.utils.Constants;

import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;


// You need to add "support-v4“ in: File -> Project Structure -> app -> Dependencies -> + -> Library Depenency

public class Workplace_Details extends AppCompatActivity implements OnNavigationItemSelectedListener{

    //FragmentTabHost tabHost;

    FrameLayout frameLayout;
    TabLayout tabLayout;

    NavigationView navigationView;
    DrawerLayout drawer;
    View headerView;
    TextView nav_name;
    TextView nav_mail;
    Toolbar toolbar;
    SharedPreferences mSharedPreferences;
    private static String TAG = "Workplace Details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplace__details);

        initSharedPreferences();
        initNavigation();

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);

        TabLayout.Tab day = tabLayout.newTab();
        day.setText("Day");
        tabLayout.addTab(day);

        TabLayout.Tab week = tabLayout.newTab();
        week.setText("Week");
        tabLayout.addTab(week);

        TabLayout.Tab month = tabLayout.newTab();
        month.setText("Month");
        tabLayout.addTab(month);

        TabLayout.Tab custom = tabLayout.newTab();
        custom.setText("Custom");
        tabLayout.addTab(custom);

        String actual_workplace = getIntent().getStringExtra("workplace");

        Overview_Day start_view = new Overview_Day();
        start_view.workplace = actual_workplace;
        Fragment f_start= start_view;

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, f_start);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:

                        Overview_Day ov_day =new Overview_Day();
                        ov_day.workplace = actual_workplace;
                        fragment = ov_day;
                        break;

                    case 1:
                        Overview_Week ov_week= new Overview_Week();
                        ov_week.workplace = actual_workplace;
                        fragment = ov_week;
                        break;

                    case 2:

                        Overview_Month ov_month= new Overview_Month();
                        ov_month.workplace = actual_workplace;
                        fragment = ov_month;
                        break;

                    case 3:
                        fragment = new Overview_Custom();
                        break;
                }

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void initNavigation() {
        //Navigation Initialization
        String nameBuffer;
        String mail;
        Intent profileActivity = new Intent(this, ProfileActivity.class);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        TextView navigation_name = (TextView) headerView.findViewById(R.id.nav_firstLastName);
        TextView navigation_mail = (TextView) headerView.findViewById(R.id.nav_email);

        nav_name = (TextView) headerView.findViewById(R.id.nav_firstLastName);
        nav_mail = (TextView) headerView.findViewById(R.id.nav_email);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.dashboard_activity);
        setSupportActionBar(toolbar);

        String actual_workplace = getIntent().getStringExtra("workplace");
        getSupportActionBar().setTitle("Overview " + actual_workplace);

        nameBuffer = mSharedPreferences.getString(Constants.FIRSTNAME, "Firstname") + " " + mSharedPreferences.getString(Constants.LASTNAME, "Lastname");
        mail = mSharedPreferences.getString(Constants.EMAIL, "Your Email");


        try {
            navigation_name.setText(nameBuffer); //Set text on view
            navigation_mail.setText(mail);
        } catch (NullPointerException e) {
            Log.e(TAG, "Couldn't fill navigation TextVievs with user data", e);
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(profileActivity);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Define all intents
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        Intent workplaceIntent = new Intent(this, Workplace.class);
        Intent overviewIntent = new Intent(this, Overview.class);
        //TODO Statistics intent
        //TODO Export intent
        //TODO import intent

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            startActivity(dashboardIntent);
        } else if (id == R.id.nav_overview) {
            startActivity(overviewIntent);
        } else if (id == R.id.nav_workplaces) {
            startActivity(workplaceIntent);
        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_export) {

        } else if (id == R.id.nav_import) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


}
