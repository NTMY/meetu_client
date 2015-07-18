package walfud.meetu.model;

import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;

import walfud.meetu.MeetUApplication;

/**
 * Created by song on 2015/6/23.
 */
public class Data {
    private String mId;
    private double mLatitude;
    private double mLongitude;

    public Data(String id, double latitude, double longitude) {
        mId = id;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public Data(Location location) {
        mId = ((TelephonyManager) (MeetUApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}
