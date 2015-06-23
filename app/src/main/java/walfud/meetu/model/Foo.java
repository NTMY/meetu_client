package walfud.meetu.model;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

import walfud.meetu.MeetUApplication;
import walfud.meetu.presenter.MainActivityPresenter;

/**
 * Created by song on 2015/6/21.
 */
public class Foo {

    public static final String TAG = "Foo";

    private static final long UPDATE_INTERVAL = 2000;

    private MainActivityPresenter mPresenter;

    public Foo(MainActivityPresenter mainActivityPresenter) {
        mPresenter = mainActivityPresenter;
    }

    private double mLatitude;
    private double mLongitude;

    private LocationManagerProxy mLocationManagerProxy;
    private AMapLocationListener mAMapLocationListener;
    public void init() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(MeetUApplication.getApplication());
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

    public void release() {
        mLocationManagerProxy.removeUpdates(mAMapLocationListener);
    }

    public Location getLocation() {
        Location location = new Location("");
        location.setLatitude(mLatitude);
        location.setLongitude(mLongitude);

        return location;
    }

    public String toUrlRequest(Location location) {
        String id = ((TelephonyManager) (MeetUApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return String.format("http://45.55.26.123:8080/meetu/meet?id=%s&latitude=%.6f&longitude=%.6f", id, latitude, longitude);
    }

    public interface onHttpPostResponse {
        void onResponse(String response);
    }
    public void httpPost(final String request, final onHttpPostResponse onHttpPostResponse) {
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                HttpClient httpClient = new DefaultHttpClient();
                // TODO: set timeout
                try {
                    HttpPost httpPost = new HttpPost(request);
                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();
                    if (httpEntity != null) {
                        InputStream inputStream = httpEntity.getContent();
                        byte[] buf = new byte[2048];
                        inputStream.read(buf);
                        inputStream.close();

                        return new String(buf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                onHttpPostResponse.onResponse(s);
            }
        }.execute();
    }
}
