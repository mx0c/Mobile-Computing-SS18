package ss18.mc.positime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;

import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;
import ss18.mc.positime.utils.TimestampConverter;

public class Edit_details_day extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    DrawerLayout drawer;
    View headerView;
    TextView nav_name;
    TextView nav_mail;
    Toolbar toolbar;
    SharedPreferences mSharedPreferences;
    private static String TAG = "abc";

    private BenutzerDatabase db;
    Integer edit_arbeitszeit_id;
    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details_day);


        initSharedPreferences();
        initNavigation();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView date= findViewById(R.id.date);
        EditText start_text= findViewById(R.id.start_text);
        EditText stop_text= findViewById(R.id.stop_time);
        EditText pause_text = findViewById(R.id.pause_time);

        Intent i = getIntent();

        edit_arbeitszeit_id=  i.getIntExtra("id", 0);

        date.setText(i.getStringExtra("date"));
        start_text.setText(i.getStringExtra("startTime"));
        stop_text.setText(i.getStringExtra("endTime"));
        pause_text.setText(i.getStringExtra("pause") );
        TimestampConverter time = new TimestampConverter();

        db = BenutzerDatabase.getBenutzerDatabase(this);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Arbeitszeit arbeitszeit = db.arbeitszeitDAO().getArbeitszeitFromID(edit_arbeitszeit_id);
                String newPauseTime = pause_text.getText().toString();
                Integer newTimeInt = Integer.parseInt(newPauseTime);
                arbeitszeit.setBreaktime(newTimeInt);

                String newStartTime = date.getText().toString()+" "+ start_text.getText().toString();
                Date startDate= time.fromTimestamp(newStartTime);
                arbeitszeit.setStarttime(startDate);

                String newEndTime = date.getText().toString()+ " "+ stop_text.getText().toString();
                Date stopDate = time.fromTimestamp(newEndTime);
                arbeitszeit.setEndtime(stopDate);

                // Calculate timeSum and Salary

                db.arbeitszeitDAO().updateArbeitszeit(arbeitszeit);
                db.beginTransaction();
                Intent i = new Intent();
                setResult(2);
                finish();
            }
        });

        cancelButton= findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent t){

    }


    public void onLogoutClick(MenuItem view) {
        switch(view.getItemId()){
            case R.id.logout_icon:

                //Reset token
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.TOKEN,null);
                editor.putString(Constants.EMAIL,null);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        } else if (id == R.id.nav_overview) {

        } else if (id == R.id.nav_workplaces) {

        } else if (id == R.id.nav_statistics) {

        } else if (id == R.id.nav_export) {

        } else if (id == R.id.nav_import) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        getSupportActionBar().setTitle("Edit Day");

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
}
