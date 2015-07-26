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

    public static final String TAG = "Model";

    private interface OnLocationListener {
        /**
         * Called from NON-UI thread.
         *
         * @param location
         */
        void onLocation(android.location.Location location);
    }

    private LocationManagerProxy mLocationManagerProxy;

    private OnLocationListener mListenerForReport = new OnLocationListener() {
        @Override
        public void onLocation(android.location.Location location) {
            // Upload my location to server
            DataRequest dataRequest = new DataRequest(new Data(location), null);   // Do NOT concern network response
            dataRequest.send();
        }
    };
    private OnLocationListener mListenerForSearch = new OnLocationListener() {
        @Override
        public void onLocation(android.location.Location location) {
            // Search nearby friends
            DataRequest dataRequest = new DataRequest(new Data(location), mOnSearchListener);
            dataRequest.send();
        }
    };

    private DataRequest.OnDataRequestListener mOnSearchListener;

    public LocationHelper() {
    }

    // Function
    public void init() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(MeetUApplication.getContext());
    }

    public void destroy() {
        mLocationManagerProxy.destroy();
    }

    public void reportSelf() {
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                android.location.Location location = new android.location.Location("");
                location.setLatitude(aMapLocation.getLatitude());
                location.setLongitude(aMapLocation.getLongitude());

                mListenerForReport.onLocation(location);
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

    /**
     * You'd better call `setOnSearchListener` first to retrieve the search result.
     */
    public void searchNearby() {
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                android.location.Location location = new android.location.Location("");
                location.setLatitude(aMapLocation.getLatitude());
                location.setLongitude(aMapLocation.getLongitude());

                mListenerForSearch.onLocation(location);
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

    public void setOnSearchListener(DataRequest.OnDataRequestListener onSearchListener) {
        mOnSearchListener = onSearchListener;
    }
}
