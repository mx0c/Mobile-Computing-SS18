package ss18.mc.positime.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;


public class BackgroundService extends Service {
    private BroadcastReceiver broadcastReceiver;
    private BenutzerDatabase db;


    @Override
    public void onCreate() {
        //start LocationService
        Intent i =new Intent(getApplicationContext(),LocationService.class);
        startService(i);

        this.db = BenutzerDatabase.getBenutzerDatabase(this);

        //register new BroadcastReceiver
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
        for(Arbeitsort a : db.arbeitsortDAO().getAll()){
            Location workplaceLocation = new Location("");
            workplaceLocation.setLatitude(a.getLatA());
            workplaceLocation.setLongitude(a.getLongA());
            if(workplaceLocation.distanceTo(myLocation) < a.getRadiusA()){
                //TODO: update database with time

                //send Broadcast Dashboard Informations
                //get todays arbeitszeit
                Arbeitszeit currAZ = this.findTodaysArbeitszeit(db.arbeitszeitDAO().getArbeitszeitenForArbeitsort(a.getPlaceName()),a);
                if(currAZ == null){
                    //create new Arbeitszeit
                    currAZ = new Arbeitszeit(0,0,a.getPlaceName(),new Date(),new Date(),new Date(),0);
                    db.arbeitszeitDAO().insertAll(currAZ);
                }
                Date time = this.calculateTimePassed(currAZ.getStarttime(),currAZ.getEndtime());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(time);

                Intent i = new Intent("dashboard_informations");
                i.putExtra("current_workplace_name",a.getPlaceName());
                i.putExtra("current_workplace_time_hours", calendar.get(Calendar.HOUR));
                i.putExtra("current_workplace_time_minutes",calendar.get(Calendar.MINUTE));
                i.putExtra("current_workplace_money_earned",this.calculateMoneyEarned(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE), a.getMoneyPerhour()));
                //etc etc
                sendBroadcast(i);
                return;
            }
        }
        //only gets executed when not inside workplace
        Intent i = new Intent("dashboard_informations");
        i.putExtra("current_workplace_name",0);
        sendBroadcast(i);
    }

    Arbeitszeit findTodaysArbeitszeit(List<Arbeitszeit> AL, Arbeitsort a){
        Calendar c = GregorianCalendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        for(Arbeitszeit atime : db.arbeitszeitDAO().getArbeitszeitenForArbeitsort(a.getPlaceName())){
            c.setTime(atime.getWorkday());
            if(currentDate.get(Calendar.DATE) == c.get(Calendar.DATE)){
                //found Arbeitszeit for this date
                return atime;
            }
        }
        return null;
    }

    Date calculateTimePassed(Date start, Date end){
        long diffInMillies = start.getTime() - end.getTime();
        return new Date(diffInMillies);
    }

    Double calculateMoneyEarned(int hours, int minutes , double earningsPerHour){
        String t = String.valueOf(hours) + "." + String.valueOf(minutes);
        return Double.parseDouble(t)*earningsPerHour;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop LocationService
        Intent i = new Intent(getApplicationContext(),LocationService.class);
        stopService(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //not binded to specific Activity
        return null;
    }
}
