package walfud.meetu.model;

import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import walfud.meetu.MeetUApplication;

/**
 * Created by song on 2015/7/25.
 */
public class LocationHelper {

    public static final String TAG = "LocationHelper";

    public interface OnLocationListener {
        /**
         * Called from NON-UI thread.
         *
         * @param aMapLocation
         */
        void onLocation(AMapLocation aMapLocation);
    }

    private LocationManagerProxy mLocationManagerProxy;

    public LocationHelper() {
    }

    // Function
    public void init() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(MeetUApplication.getContext());
    }

    public void destroy() {
        mLocationManagerProxy.destroy();
    }

    public void getLocation(final OnLocationListener listener) {
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
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
}
