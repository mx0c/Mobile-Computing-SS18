package ss18.mc.positime.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.WindowManager;

public class LocationService extends Service{

    private LocationListener mListener;
    private LocationManager mLocationManager;
    private final static int MIN_TIME_BW_UPDATES = 10000; //10 seconds
    private final static int MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //not binded to specific Activity
        return null;
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onCreate() {
        mListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                i.putExtra("longitude",location.getLongitude());
                i.putExtra("latitude",location.getLatitude());
                sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
            }
        };

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        String provider = mLocationManager.getBestProvider(criteria, true);
        mLocationManager.requestLocationUpdates(provider,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,mListener);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onDestroy() {
        super.onDestroy();
        if(mLocationManager != null){
            mLocationManager.removeUpdates(mListener);
        }
    }
}