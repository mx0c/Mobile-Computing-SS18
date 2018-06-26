package ss18.mc.positime.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;


public class BackgroundService extends Service {
    private BroadcastReceiver mLocationBroadcastReceiver;
    private BroadcastReceiver mCommandBroadcastReceiver;
    private BenutzerDatabase mDb;
    private SharedPreferences mPref;
    private boolean mInPause;
    private Arbeitszeit mCurrentArbeitszeit;

    @Override
    public void onCreate() {
        //start LocationService
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        startService(i);
        this.mPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.mDb = BenutzerDatabase.getBenutzerDatabase(this);

        //init
        this.mCurrentArbeitszeit = null;

        //register BroadcastReceivers
        mCommandBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getExtras().get("command").toString();

                switch (command) {
                    case "RESUME":
                        mInPause = true;
                        break;
                    case "PAUSE":
                        //increase pauseAmount in DB
                        if (mCurrentArbeitszeit != null) {
                            mCurrentArbeitszeit.setAmountBreaks(mCurrentArbeitszeit.getAmountBreaks() + 1);
                        }
                        //update db
                        mDb.arbeitszeitDAO().updateArbeitszeit(mCurrentArbeitszeit);
                        mInPause = false;
                        break;
                }
            }
        };
        registerReceiver(mCommandBroadcastReceiver, new IntentFilter("background_commands"));

        mLocationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onLocationUpdate((double) intent.getExtras().get("latitude"), (double) intent.getExtras().get("longitude"));
            }
        };
        registerReceiver(mLocationBroadcastReceiver, new IntentFilter("location_update"));
    }

    private void onLocationUpdate(double lat, double lon) {
        if(this.mInPause && this.mCurrentArbeitszeit != null){
            this.mCurrentArbeitszeit.setBreaktime(this.mCurrentArbeitszeit.getBreaktime()+10);
            //update db
            mDb.arbeitszeitDAO().updateArbeitszeit(this.mCurrentArbeitszeit);
            return;
        }
        Location myLocation = new Location("");
        myLocation.setLongitude(lon);
        myLocation.setLatitude(lat);
        //check if current location is in a saved Workplace
        for (Arbeitsort a : mDb.arbeitsortDAO().getArbeitsorteForUser(mPref.getString(Constants.EMAIL,null))) {
            Location workplaceLocation = new Location("");
            workplaceLocation.setLatitude(a.getLatA());
            workplaceLocation.setLongitude(a.getLongA());
            if (workplaceLocation.distanceTo(myLocation) < a.getRadiusA()) {
                //Location is in Workplace
                //get todays arbeitszeit
                this.mCurrentArbeitszeit = this.findTodaysArbeitszeit(mDb.arbeitszeitDAO().getArbeitszeitenForArbeitsort(a.getPlaceName()), a);
                if(this.mCurrentArbeitszeit != null){
                    if(this.mCurrentArbeitszeit.getStarttime() == null){
                        this.mCurrentArbeitszeit.setStarttime(new Date());
                    }
                    this.mCurrentArbeitszeit.setEndtime(new Date());
                    //insert updated or new currAZ into db
                    mDb.arbeitszeitDAO().insertAll(this.mCurrentArbeitszeit);
                } else{
                    //create new Arbeitszeit
                    this.mCurrentArbeitszeit.setAmountBreaks(0);
                    this.mCurrentArbeitszeit.setWorkday(new Date());
                    this.mCurrentArbeitszeit.setArbeitsort_name(a.getPlaceName());
                    this.mCurrentArbeitszeit.setBreaktime(0);
                    this.mCurrentArbeitszeit.setArbeitszeitId(0);

                    //update db
                    mDb.arbeitszeitDAO().updateArbeitszeit(this.mCurrentArbeitszeit);
                }

                Date time = this.calculateTimePassed(this.mCurrentArbeitszeit.getStarttime(), this.mCurrentArbeitszeit.getEndtime());
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setTime(time);

                Intent i = new Intent("dashboard_informations");

                i.putExtra("current_workplace_name", a.getPlaceName());
                i.putExtra("current_workplace_time_hours", calendar.get(Calendar.HOUR));
                i.putExtra("current_workplace_time_minutes", calendar.get(Calendar.MINUTE));
                i.putExtra("current_workplace_money_earned", this.calculateMoneyEarned(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), a.getMoneyPerhour()));
                i.putExtra("current_workplace_pause_seconds",this.mCurrentArbeitszeit.getBreaktime());
                i.putExtra("current_workplace_pause_count",this.mCurrentArbeitszeit.getAmountBreaks());

                sendBroadcast(i);
                return;
            }
        }

        //only gets executed when not inside workplace or pause is active
        this.mCurrentArbeitszeit = null;
        Intent i = new Intent("dashboard_informations");

        i.putExtra("current_workplace_name", "0");
        sendBroadcast(i);
    }

    Arbeitszeit findTodaysArbeitszeit(List<Arbeitszeit> AL, Arbeitsort a) {
        Calendar c = GregorianCalendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        for (Arbeitszeit atime : mDb.arbeitszeitDAO().getArbeitszeitenForArbeitsort(a.getPlaceName())) {
            c.setTime(atime.getWorkday());
            if (currentDate.get(Calendar.DATE) == c.get(Calendar.DATE)) {
                //found Arbeitszeit for this date
                return atime;
            }
        }
        return null;
    }

    Date calculateTimePassed(Date start, Date end) {
        long diffInMillies = start.getTime() - end.getTime();
        return new Date(diffInMillies);
    }

    Double calculateMoneyEarned(int hours, int minutes, double earningsPerHour) {
        String t = String.valueOf(hours) + "." + String.valueOf(minutes);
        return Double.parseDouble(t) * earningsPerHour;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop LocationService
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        stopService(i);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //not binded to specific Activity
        return null;
    }
}
