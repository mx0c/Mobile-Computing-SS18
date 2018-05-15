package ss18.mc.positime;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by marius on 15.05.2018.
 */

//https://stackoverflow.com/questions/2566350/how-to-always-run-a-service-in-the-background

public class MainBackgroundService extends Service{
    private Timer mTimer;
    private LocationService mLocationService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTimer = new Timer(1000);
        mLocationService = new LocationService(this,10000,5);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(Intent intent, int startid) {

    }
}
