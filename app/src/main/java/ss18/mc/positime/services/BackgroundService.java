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

import okhttp3.internal.Util;
import ss18.mc.positime.dbmodel.Arbeitsort;
import ss18.mc.positime.dbmodel.Arbeitszeit;
import ss18.mc.positime.local.BenutzerDatabase;
import ss18.mc.positime.utils.Constants;

import ss18.mc.positime.utils.UtilFunctions;
import ss18.mc.positime.utils.Timer;


public class BackgroundService extends Service {
    private BroadcastReceiver mLocationBroadcastReceiver;
    private BroadcastReceiver mCommandBroadcastReceiver;
    private BenutzerDatabase mDb;
    private SharedPreferences mPref;
    private boolean mInPause;
    private Arbeitszeit mCurrentArbeitszeit;
    private Timer mPauseTimer;
    private Timer mMainTimer;
    private Location mLocation;
    private String mTempWorkplacename;
    @Override
    public void onCreate() {
        Log.d("BackgroundService", "onCreate: STARTED SERVICE");

        //init
        this.mPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.mDb = BenutzerDatabase.getBenutzerDatabase(this);
        this.mInPause = false;
        this.mCurrentArbeitszeit = null;
        this.mLocation = null;

        //register BroadcastReceivers
        mCommandBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getExtras().get("command").toString();

                switch (command) {
                    case "RESUME":
                        mInPause = false;
                        mPauseTimer.stopTimer();
                        mPauseTimer.setTime(0);
                        break;
                    case "PAUSE":
                        //increase pauseAmount in DB
                        if (mCurrentArbeitszeit != null) {
                            mCurrentArbeitszeit.setAmountBreaks(mCurrentArbeitszeit.getAmountBreaks() + 1);
                        }
                        //update db
                        mDb.arbeitszeitDAO().updateArbeitszeit(mCurrentArbeitszeit);
                        mInPause = true;
                        mPauseTimer = new Timer(1000);
                        mPauseTimer.setTime(mCurrentArbeitszeit.getBreaktime());
                        mPauseTimer.startTimer();
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

        mMainTimer = new Timer(1000, new Runnable() {
            @Override
            public void run() {
                update();
                mMainTimer.mHandler.postDelayed(this,mMainTimer.mInterval);
            }
        });
        mMainTimer.startTimer();
    }

    private void update(){
        if(this.mInPause && this.mCurrentArbeitszeit != null){
            this.mCurrentArbeitszeit.setBreaktime(mPauseTimer.getTime());
            //update db
            mDb.arbeitszeitDAO().updateArbeitszeit(this.mCurrentArbeitszeit);
            Intent i = new Intent("dashboard_informations");
            i.putExtra("current_workplace_name", mTempWorkplacename);
            i.putExtra("current_workplace_time", UtilFunctions.secondsToTimestring(this.mCurrentArbeitszeit.getWorktime()));
            i.putExtra("current_workplace_money_earned", UtilFunctions.calculateSalary(this.mCurrentArbeitszeit.getWorktime(),mDb.arbeitsortDAO().getMoneyPerHour(mTempWorkplacename)));
            i.putExtra("current_workplace_pause_minutes", this.mCurrentArbeitszeit.getBreaktime()/60);
            i.putExtra("current_workplace_pause_count", this.mCurrentArbeitszeit.getAmountBreaks());
            sendBroadcast(i);
            return;
        }

        if(this.mLocation != null) {
            //check if current location is in a saved Workplace
            for (Arbeitsort a : mDb.arbeitsortDAO().getArbeitsorteForUser(mPref.getString(Constants.EMAIL, null))) {
                Location workplaceLocation = new Location("");
                workplaceLocation.setLatitude(a.getLatA());
                workplaceLocation.setLongitude(a.getLongA());
                if (workplaceLocation.distanceTo(mLocation) < a.getRadiusA()) {
                    //Location is in Workplace
                    //get todays arbeitszeit
                    this.mCurrentArbeitszeit = this.findTodaysArbeitszeit(mDb.arbeitszeitDAO().getArbeitszeitenForArbeitsort(a.getPlaceName()), a);
                    if (this.mCurrentArbeitszeit != null) {
                        //update time for Workplace
                        if (this.mCurrentArbeitszeit.getStarttime() == null) {
                            this.mCurrentArbeitszeit.setStarttime(new Date());
                        }
                        this.mCurrentArbeitszeit.setWorktime(this.mCurrentArbeitszeit.getWorktime()+1);
                        this.mCurrentArbeitszeit.setEndtime(new Date());
                        //insert updated currAZ
                        mDb.arbeitszeitDAO().updateArbeitszeit(this.mCurrentArbeitszeit);
                    } else {
                        //create new Arbeitszeit
                        this.mCurrentArbeitszeit = new Arbeitszeit();
                        this.mCurrentArbeitszeit.setAmountBreaks(0);
                        this.mCurrentArbeitszeit.setWorkday(new Date());
                        this.mCurrentArbeitszeit.setArbeitsort_name(a.getPlaceName());
                        this.mCurrentArbeitszeit.setBreaktime(0);
                        this.mCurrentArbeitszeit.setArbeitszeitId(0);
                        this.mCurrentArbeitszeit.setStarttime(new Date());
                        this.mCurrentArbeitszeit.setEndtime(new Date());
                        this.mCurrentArbeitszeit.setWorktime(0);

                        //update db
                        mDb.arbeitszeitDAO().insertAll(this.mCurrentArbeitszeit);
                    }
                    mTempWorkplacename = a.getPlaceName();
                    Intent i = new Intent("dashboard_informations");
                    i.putExtra("current_workplace_name", a.getPlaceName());
                    i.putExtra("current_workplace_time", UtilFunctions.secondsToTimestring(this.mCurrentArbeitszeit.getWorktime()));
                    i.putExtra("current_workplace_money_earned", UtilFunctions.calculateSalary(this.mCurrentArbeitszeit.getWorktime(),mDb.arbeitsortDAO().getMoneyPerHour(a.getPlaceName())));
                    i.putExtra("current_workplace_pause_minutes", this.mCurrentArbeitszeit.getBreaktime()/60);
                    i.putExtra("current_workplace_pause_count", this.mCurrentArbeitszeit.getAmountBreaks());

                    sendBroadcast(i);
                    return;
                }
            }
        }
        //only gets executed when not inside workplace
        this.mCurrentArbeitszeit = null;
        Intent i = new Intent("dashboard_informations");
        i.putExtra("current_workplace_name", "0");
        sendBroadcast(i);
    }

    private void onLocationUpdate(double lat, double lon) {
        mLocation = new Location("");
        mLocation.setLatitude(lat);
        mLocation.setLongitude(lon);
    }

    Arbeitszeit findTodaysArbeitszeit(List<Arbeitszeit> AL, Arbeitsort a) {
        Calendar c = GregorianCalendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        for (Arbeitszeit atime : mDb.arbeitszeitDAO().getArbeitszeitenForArbeitsort(a.getPlaceName())) {
            c.setTime(atime.getWorkday());
            if (currentDate.get(Calendar.MONTH) == c.get(Calendar.MONTH) && currentDate.get(Calendar.YEAR) == c.get(Calendar.YEAR) && currentDate.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)) {
                //found Arbeitszeit for this date
                return atime;
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getApplicationContext(), LocationService.class));
        stopService(new Intent(getApplicationContext(), BackgroundService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        //not binded to specific Activity
        return null;
    }
}
