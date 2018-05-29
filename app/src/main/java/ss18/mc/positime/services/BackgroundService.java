package ss18.mc.positime.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;

import java.util.Date;

//mocked classes
class Workplace{
    Workplace(double lat,double lon,int rad){
        this.lat = lat;
        this.lon = lon;
        this.radius = rad;
    }
    public double lat;
    public double lon;
    public int radius;
    public String name;
    public Date start;
    public Date end;
}

public class BackgroundService extends Service {
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate() {
        //start LocationService
        Intent i =new Intent(getApplicationContext(),LocationService.class);
        startService(i);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onLocationUpdate((double)intent.getExtras().get("latitude"), (double)intent.getExtras().get("longitude"));
            }
        };
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    private void onLocationUpdate(double lat, double lon){
        Location myLocation = new Location("");
        myLocation.setLongitude(lon);
        myLocation.setLatitude(lat);
        //check if current location is in a saved Workplace
        for(Workplace w : this.getWorkplaces()){
            Location workplaceLocation = new Location("");
            workplaceLocation.setLatitude(w.lat);
            workplaceLocation.setLongitude(w.lon);
            if(workplaceLocation.distanceTo(myLocation) < w.radius){
                //update database with time

                //send Broadcast Dashboard Informations
                Intent i = new Intent("dashboard_informations");
                i.putExtra("current_workplace_name",w.name);
                i.putExtra("current_workplace_time",this.calculateTimePassed(w.start,w.end));
                //etc etc
                sendBroadcast(i);
            }else{
                //send Broadcast information for no Workplace
                Intent i = new Intent("dashboard_informations");
                i.putExtra("current_workplace_name","");
                sendBroadcast(i);
            }
        }
    }

    long calculateTimePassed(Date start, Date end){
        long diffInMillies = start.getTime() - end.getTime();
        return new Date(diffInMillies).getTime();
    }

    Double calculateMoneyEarned(double time, double earningsPerHour){
        return time*earningsPerHour;
    }

    private Workplace[] getWorkplaces(){
        Workplace[] workplaces = {new Workplace(1.2,1.2,10),new Workplace(1.3,1.3,20)};
        return workplaces;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop LocationService
        Intent i =new Intent(getApplicationContext(),LocationService.class);
        stopService(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //not binded to specific Activity
        return null;
    }
}
