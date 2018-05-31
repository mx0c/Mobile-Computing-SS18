package ss18.mc.positime;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ss18.mc.positime.utils.Constants;

public class DashboardActivity extends AppCompatActivity implements OnNavigationItemSelectedListener{
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dasboard");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Only for testing purposes. Can be deleted. This demonstrates how to get the email of the currently logged in user
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(mSharedPreferences.getString(Constants.EMAIL, ""));
    }


    //When logout is clicked, remove token and go back to login
    public void onLogoutClick(MenuItem view) {
        switch(view.getItemId()){
            case R.id.logout_icon:
                //Reset all the saved data
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(Constants.TOKEN,null);
                editor.putString(Constants.EMAIL,null);
                editor.putString(Constants.FIRSTNAME,null);
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
}
