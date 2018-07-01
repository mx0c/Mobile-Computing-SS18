package ss18.mc.positime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.DatabaseInitializer;
import ss18.mc.positime.utils.MyCustomAdapter;

public class Workplace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = "Workplace";
    BenutzerDatabase db;
    SharedPreferences mSharedPreferences;
    DrawerLayout drawer;
    NavigationView navigationView;
    View headerView;
    TextView nav_name;
    TextView nav_mail;
    Toolbar toolbar;
    FloatingActionButton fab;
    Intent wp_add_edit;
    MyCustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplace);

        //Initialization
        initView();
        initSharedPreferences();
        initNavigation();
        initWorkplaceList(); //Initialize the Workplaces to generate the ListView

        //FloatingActionButton Listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wp_add_edit = new Intent(getBaseContext(), Workplace_add_edit.class);
                wp_add_edit.putExtra("source", "add");
                startActivity(wp_add_edit);
            }
        });
    }

    /*
        ####################################################################
                                Initialization
        ####################################################################
     */
    //Put view initializations in here
    private void initView() {
        //Floating Button
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //TODO init your views here
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
        toolbar.setTitle(R.string.workplace_activity);
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

    private void initSharedPreferences() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void initWorkplaceList() {
        BenutzerDatabase db = BenutzerDatabase.getBenutzerDatabase(this);
        //Temporary
        //DatabaseInitializer.populateSync(db);

        String userMail = mSharedPreferences.getString(Constants.EMAIL, null);

        List<Arbeitsort> workplaces = db.arbeitsortDAO().getArbeitsorteForUser(userMail);
        ArrayList<String> workplace_names = new ArrayList<String>();

        Log.d(TAG, "Workplaces found for user with email " + userMail + ": " + workplaces.size());

        //instantiate custom adapter
        adapter = new MyCustomAdapter(workplaces, this);

        //handle listview and assign adapter
        ListView lView = (ListView) findViewById(R.id.workplace_list_view);
        lView.setAdapter(adapter);
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

                finish(); //prevent from going back to activity
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


    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}
