package ss18.mc.positime;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

// https://stackoverflow.com/questions/2566350/how-to-always-run-a-service-in-the-background
// https://stackoverflow.com/questions/4459058/alarm-manager-example

public class LocationChecker extends BroadcastReceiver
{
    private LocationService mLocationService ;

    LocationChecker(Context c, int updatetime,int updatedistance){
        this.mLocationService = new LocationService(c,updatetime,updatedistance);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        mLocationService.getLocation();
        Toast.makeText(context, "Lon:"+ mLocationService.getLongitude() +" Lat:" + mLocationService.getLatitude() , Toast.LENGTH_LONG).show(); // For example
        Log.d("LocationChecker", "onReceive: GOOOOOTEM");
        wl.release();
    }

    public void setAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, LocationChecker.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, LocationChecker.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}