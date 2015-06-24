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

    public static String toUrlRequest(Location location) {
        String id = ((TelephonyManager) (MeetUApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return String.format("http://45.55.26.123:8080/meetu/meet?id=%s&latitude=%.6f&longitude=%.6f", id, latitude, longitude);
    }

    public static interface onHttpPostResponse {
        void onResponse(String response);
    }
    public static void httpPost(final String request, final onHttpPostResponse onHttpPostResponse) {
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
