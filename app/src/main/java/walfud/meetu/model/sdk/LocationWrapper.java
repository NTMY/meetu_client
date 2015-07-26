package walfud.meetu.model.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

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
public class LocationWrapper {

    public static final String TAG = "LocationService";

    public interface OnLocationListener {
        /**
         * Called from NON-UI thread.
         * @param location
         */
        void onLocation(android.location.Location location);
    }
    private OnLocationListener mLocationListener;

    private LocationManagerProxy mLocationManagerProxy;
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (mLocationListener != null) {

                android.location.Location location = new android.location.Location("");
                location.setLatitude(aMapLocation.getLatitude());
                location.setLongitude(aMapLocation.getLongitude());

                mLocationListener.onLocation(location);
            }
        }

        @Override
        public void onLocationChanged(android.location.Location location) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    // Function
    public void init() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(MeetUApplication.getContext());
    }

    public void destory() {
        mLocationManagerProxy.destroy();
    }

    // Model Function
    public void setOnLocationListener(OnLocationListener locationListener) {
        mLocationListener = locationListener;
    }
    public void requestLocationData() {
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, mAMapLocationListener);
    }
}