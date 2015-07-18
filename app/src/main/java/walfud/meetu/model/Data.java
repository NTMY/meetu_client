package walfud.meetu.model;

import android.content.Context;
import android.location.Location;
import android.telephony.TelephonyManager;

import walfud.meetu.MeetUApplication;

/**
 * Created by song on 2015/6/23.
 */
public class Data {
    private String mImei;
    private double mLatitude;
    private double mLongitude;

    public Data() {
    }
    public Data(String imei, double latitude, double longitude) {
        mImei = imei;
        mLatitude = latitude;
        mLongitude = longitude;
    }
    public Data(Location location) {
        mImei = ((TelephonyManager) (MeetUApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
    }

    public String getImei() {
        return mImei;
    }

    public void setImei(String imei) {
        mImei = imei;
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
