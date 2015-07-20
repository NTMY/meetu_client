package walfud.meetu.model;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import java.util.Timer;
import java.util.TimerTask;

import walfud.meetu.MeetUApplication;
import walfud.meetu.ServiceBinder;
import walfud.meetu.Utils;

/**
 * Created by song on 2015/6/24.
 */
public class LocationService extends Service {

    public static final String TAG = "LocationService";

    public interface OnLocationListener {
        /**
         * Called from NON-UI thread.
         * @param location
         */
        void onLocation(Location location);
    }
    private OnLocationListener mLocationListener;

    private static final long UPDATE_INTERVAL = 2000;

    private LocationManagerProxy mLocationManagerProxy;

    private Timer mTimer = new Timer();
    private TimerTask mReportSelfTimerTask = new TimerTask() {

//        private Object mWaiter = new Object();

        private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (mLocationListener != null) {

                    Location location = new Location("");
                    location.setLatitude(aMapLocation.getLatitude());
                    location.setLongitude(aMapLocation.getLongitude());

                    mLocationListener.onLocation(location);
                }

//                mWaiter.notify();
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

        @Override
        public void run() {
            mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, mAMapLocationListener);
//            mLocationManagerProxy.setGpsEnable(false);

//            try {
//                mWaiter.wait(UPDATE_INTERVAL / 2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            mLocationManagerProxy.removeUpdates(mAMapLocationListener);
        }
    };
    private TimerTask mSearchOthersTimerTask = new TimerTask() {
        @Override
        public void run() {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        // Location SDK
        mLocationManagerProxy = LocationManagerProxy.getInstance(MeetUApplication.getContext());

        // Driver
        mTimer.schedule(mReportSelfTimerTask, 0, UPDATE_INTERVAL);
        mTimer.schedule(mSearchOthersTimerTask, 0, UPDATE_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mTimer.cancel();
        mLocationManagerProxy.destroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder<>(this);
    }

    // Model Function
    public void setOnLocationListener(OnLocationListener locationListener) {
        mLocationListener = locationListener;
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