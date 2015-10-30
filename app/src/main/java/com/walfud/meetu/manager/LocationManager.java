package com.walfud.meetu.manager;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.walfud.meetu.MeetUApplication;

/**
 * Created by song on 2015/7/25.
 */
public class LocationManager {

    public static final String TAG = "LocationManager";
    private static LocationManager sInstance;

    public interface OnLocationListener {
        /**
         * Called from NON-UI thread.
         *
         * @param aMapLocation
         */
        void onLocation(AMapLocation aMapLocation);
    }

    private Context mContext;
    private LocationManagerProxy mLocationManagerProxy;

    private LocationManager(Context context) {
        mContext = context;
    }

    // Function
    public void init() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);
    }

    public void destroy() {
        mLocationManagerProxy.destroy();
    }

    public void getLocation(final OnLocationListener listener) {
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                //
                if (listener != null) {
                    listener.onLocation(aMapLocation);
                }
            }

            @Override
            public void onLocationChanged(android.location.Location location) {
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
        });
    }

    // Internal


    // Helper
    public static LocationManager getInstance() {
        if (sInstance == null) {
            sInstance = new LocationManager(MeetUApplication.getContext());
        }

        return sInstance;
    }
}
