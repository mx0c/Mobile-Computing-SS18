package ss18.mc.positime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ss18.mc.positime.utils.Constants;

public class DashboardActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {
    private static String TAG = "DashboardActivity";
    SharedPreferences mSharedPreferences;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    TextView nav_name;
    TextView nav_mail;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Initialization
        initView();
        initSharedPreferences();
        initNavigation();

        //Only for testing purposes. Can be deleted. This demonstrates how to get the email of the currently logged in user
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(mSharedPreferences.getString(Constants.EMAIL, ""));
    }


    /*
        ####################################################################
                                Initialization
        ####################################################################
     */
    //Put view initializations in here
    private void initView() {
        //TODO init your views here
    }

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    /*
     ####################################################################
                          Navigation & Toolbar
     ####################################################################
    */
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
}
