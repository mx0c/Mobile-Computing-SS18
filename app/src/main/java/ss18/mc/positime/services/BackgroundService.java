package ss18.mc.positime.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

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
                //intent.getExtras().get("latitude")
                //intent.getExtras().get("longitude")
            }
        };
    registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
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
