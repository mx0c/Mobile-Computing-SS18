package ss18.mc.positime;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private final static int MIN_DISTANCE_CHANGE_FOR_UPDATES = 5;
    private final static int MIN_TIME_BW_UPDATES = 10000;

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
                final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                alertDialog.setTitle("GPS is Disabled");
                alertDialog.setMessage("Do you want to enable GPS?");
                alertDialog.setButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            dialog.dismiss();
                        }
                });
                alertDialog.setButton2("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                alertDialog.show();
            }
        };

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,mListener);
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