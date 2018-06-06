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
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import org.w3c.dom.Text;

import ss18.mc.positime.utils.Constants;

public class DashboardActivity extends AppCompatActivity implements OnNavigationItemSelectedListener{
    SharedPreferences mSharedPreferences;
    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dasboard");
        setSupportActionBar(toolbar);

        TextView workplace_dash = (TextView) findViewById(R.id.workplace_dashboard);
        workplace_dash.setText("Hochschule Reutlingen");
        TextView working_time_dash = (TextView) findViewById(R.id.worktime_dashboard);
        working_time_dash.setText("8 h 2 min 3 s");
        TextView pause_dash = (TextView)findViewById(R.id.pause_dashboard);
        pause_dash.setText("Pause: 5 h");

        /*SIDEBAR*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Only for testing purposes. Can be deleted. This demonstrates how to get the email of the currently logged in user
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //TextView textView = (TextView) findViewById(R.id.textView2);
        //textView.setText(mSharedPreferences.getString(Constants.EMAIL, ""));

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
                                                   Fragment fragment = new FirstFragment();
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
