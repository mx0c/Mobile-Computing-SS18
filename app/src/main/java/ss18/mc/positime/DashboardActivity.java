package ss18.mc.positime;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.content.IntentFilter;
import org.w3c.dom.Text;

import ss18.mc.positime.utils.Constants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
public class DashboardActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    private static String TAG = "DashboardActivity";
    SharedPreferences mSharedPreferences;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    TextView nav_name;
    TextView nav_mail;
    Toolbar toolbar;
    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    Fragment fragment;
    BroadcastReceiver broadcastReceiver;
    ImageView imgPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, new FirstFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        //Initialization
        initView();
        initSharedPreferences();
        initNavigation();
        initBackgroundServiceInfo();
        play_pause_listener();
        imgPlay = (ImageView)findViewById(R.id.play_work);
        imgPlay.setAlpha(100);


    }
    public void play_pause_listener(){
        ImageView imgClick = (ImageView)findViewById(R.id.click_pause);
        imgClick.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent("background_commands");
                i.putExtra("command","PAUSE");
                sendBroadcast(i);
                imgPlay.setAlpha(255);
                imgClick.setAlpha(100);
                Toast.makeText(DashboardActivity.this, "Paused", Toast.LENGTH_LONG).show();

            }
        });
        ImageView imgClickPlay = (ImageView)findViewById(R.id.play_work);
        imgClickPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent("background_commands");
                i.putExtra("command","RESUME");
                sendBroadcast(i);
                Toast.makeText(DashboardActivity.this, "Play", Toast.LENGTH_LONG).show();
                imgPlay.setAlpha(100);
                imgClick.setAlpha(255);
            }
        });

    }
    public void initBackgroundServiceInfo(){
        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                TextView workplace_dash = (TextView) findViewById(R.id.workplace_dashboard);
                TextView working_time_dash = (TextView) findViewById(R.id.worktime_dashboard);
                TextView pause_dash = (TextView) findViewById(R.id.pause_dashboard);
                Bundle test = intent.getExtras();
                String current_name = (String) test.get("current_workplace_name");
                if(current_name.equals("0")){
                    workplace_dash.setText("not in workplace");
                    imgPlay.setAlpha(255);
                }else{
                    String[] current_time = test.getString("current_workplace_time").split("\\.");
                    String current_time_pause = test.get("current_workplace_pause_minutes").toString();
                    working_time_dash.setText(current_time[0] +" hours "+current_time[1] + " minutes");

                    pause_dash.setText("Pause: "+current_time_pause+" min");
                    workplace_dash.setText(current_name);

                }
            }
        };
        registerReceiver(broadcastReceiver,new IntentFilter("dashboard_informations"));

    }

    /*
        ####################################################################
                                Initialization
        ####################################################################
     */
    //Put view initializations in here
    private void initView() {
        /*TABS*/
        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
        tabLayout = (TabLayout) findViewById(R.id.simpleTabLayout);
        // Create the tabs
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Now"); // set the Text for the first Tab
        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("Today"); // set the Text for the second Tab
        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
        TabLayout.Tab thirdTab = tabLayout.newTab();
        thirdTab.setText("Total"); // set the Text for the first Tab
        tabLayout.addTab(thirdTab); // add  the tab at in the TabLayout
        TabLayout.Tab fourthTab = tabLayout.newTab();
        fourthTab.setText("Calender"); // set the Text for the fourth Tab
        tabLayout.addTab(fourthTab); // add  the tab at in the TabLayout




        // perform setOnTabSelectedListener event on TabLayout
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                //Fragment fragment = new FirstFragment();
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new FirstFragment();
                        break;
                    case 1:
                        fragment = new SecondFragment();
                        break;
                    case 2:
                        fragment = new ThirdFragment();
                        break;
                    case 3:
                        fragment = new FourthFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleFrameLayout, fragment);
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

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


    /*
   ####################################################################
                        Navigation & Toolbar
   ####################################################################
  */
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

        /*SIDEBAR*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }




    //When logout is clicked, remove token and go back to login
    public void onLogoutClick(MenuItem view) {
        switch (view.getItemId()) {
            case R.id.logout_icon:
                //Reset all the saved data
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.TOKEN, null);
                editor.putString(Constants.EMAIL, null);
                editor.putString(Constants.FIRSTNAME, null);
                editor.putString(Constants.LASTNAME, null);
                editor.putString(Constants.PASSWORD, null);
                editor.apply();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_activity, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        Intent workplaceIntent = new Intent(this, Workplace.class);
        Intent overviewIntent = new Intent(this, Overview.class);
        Intent developmentIntent = new Intent(this, UnderWork.class);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            startActivity(dashboardIntent);
        } else if (id == R.id.nav_overview) {
            startActivity(overviewIntent);
        } else if (id == R.id.nav_workplaces) {
            startActivity(workplaceIntent);
        } else if (id == R.id.nav_statistics) {
            startActivity(developmentIntent);
        } else if (id == R.id.nav_export) {
            startActivity(developmentIntent);
        } else if (id == R.id.nav_import) {
            startActivity(developmentIntent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
