package ss18.mc.positime;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;


// You need to add "support-v4“ in: File -> Project Structure -> app -> Dependencies -> + -> Library Depenency

public class Workplace_Details extends FragmentActivity {

    FragmentTabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workplace__details);
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(
                tabHost.newTabSpec("day").setIndicator("Day", null),
                FragmentTab.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("week").setIndicator("Week", null),
                FragmentTab.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("month").setIndicator("Month", null),
                FragmentTab.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("custom").setIndicator("Custom", null),
                FragmentTab.class, null);


    }

    @Override
    protected void onResume(){
        super.onResume();
        TabHost tabhost =(FragmentTabHost) findViewById(android.R.id.tabhost);
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
}
