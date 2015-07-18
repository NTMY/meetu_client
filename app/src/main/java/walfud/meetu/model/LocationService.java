package walfud.meetu.model;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;

/**
 * Created by song on 2015/6/24.
 */
public class LocationService extends Service {

    public static final String TAG = "LocationService";

    private static final long UPDATE_INTERVAL = 2000;

    private LocationManagerProxy mLocationManagerProxy;
    private AMapLocationListener mAMapLocationListener;

    private double mLatitude;
    private double mLongitude;

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManagerProxy = LocationManagerProxy.getInstance(MeetUApplication.getContext());
        mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mLatitude = aMapLocation.getLatitude();
                mLongitude = aMapLocation.getLongitude();

                Log.d(TAG, String.format("onLocationChanged: '%f' '%f'", mLatitude, mLongitude));
            }

            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, UPDATE_INTERVAL, 0, mAMapLocationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLocationManagerProxy.removeUpdates(mAMapLocationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    // Model Function
    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(mLatitude);
        location.setLongitude(mLongitude);

        return location;
    }

    public static final Intent SERVICE_INTENT = new Intent(MeetUApplication.getContext(), LocationService.class);
    public static void startService() {
        MeetUApplication.getContext().startService(SERVICE_INTENT);
    }
    public static void stopService() {
        MeetUApplication.getContext().stopService(SERVICE_INTENT);
    }
    public static boolean isServiceRunning() {
        return Utils.isServiceRunning(MeetUApplication.getContext(), SERVICE_INTENT);
    }
}