package walfud.meetu.model;

import walfud.meetu.model.sdk.LocationWrapper;

/**
 * Created by song on 2015/7/25.
 */
public class LocationHelper {

    public static final String TAG = "Model";

    private LocationWrapper mLocationWrapper;
    private LocationWrapper.OnLocationListener mListenerForReport = new LocationWrapper.OnLocationListener() {
        @Override
        public void onLocation(android.location.Location location) {
            // Upload my location to server
            DataRequest dataRequest = new DataRequest(new Data(location), null);   // Do NOT concern network response
            dataRequest.send();
        }
    };
    private LocationWrapper.OnLocationListener mListenerForSearch = new LocationWrapper.OnLocationListener() {
        @Override
        public void onLocation(android.location.Location location) {
            // Search nearby friends
            DataRequest dataRequest = new DataRequest(new Data(location), mOnSearchListener);
            dataRequest.send();
        }
    };

    private DataRequest.OnDataRequestListener mOnSearchListener;

    public LocationHelper() {
        mLocationWrapper = new LocationWrapper();
    }

    public void init() {
        mLocationWrapper.init();
    }
    public void destroy() {
        mLocationWrapper.destory();
    }

    public void reportSelf() {
        mLocationWrapper.setOnLocationListener(mListenerForReport);
        mLocationWrapper.requestLocationData();
    }

    /**
     * You'd better call `setOnSearchListener` first to retrieve the search result.
     */
    public void searchNearby() {
        mLocationWrapper.setOnLocationListener(mListenerForSearch);
        mLocationWrapper.requestLocationData();
    }
    public void setOnSearchListener(DataRequest.OnDataRequestListener onSearchListener) {
        mOnSearchListener = onSearchListener;
    }
}
