package walfud.meetu.model;

import android.location.Location;

import org.meetu.model.LocationCurr;

import java.util.Date;

/**
 * Created by song on 2015/6/23.
 */
public class Data {
    private int mUserId;
    private double mLatitude;
    private double mLongitude;
    private Date mUploadTime;

    public Data() {
    }
    public Data(int userId, double latitude, double longitude, Date uploadTime) {
        mUserId = userId;
        mLatitude = latitude;
        mLongitude = longitude;
        mUploadTime = uploadTime;
    }
    public Data(Location location) {
        this(0, location);
        mUserId = findUserId();
    }
    public Data(int userId, Location location) {
        this(userId, location.getLatitude(), location.getLongitude(), new Date());
    }
    public Data(LocationCurr locationCurr) {
        this(locationCurr.getUserId(), locationCurr.getLatitude(), locationCurr.getLongitude(), locationCurr.getUploadTime());
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
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

    public Date getUploadTime() {
        return mUploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        mUploadTime = uploadTime;
    }

    public LocationCurr toLocationCurr() {
        LocationCurr locationCurr = new LocationCurr();
        locationCurr.setUserId(mUserId);
        locationCurr.setLatitude(mLatitude);
        locationCurr.setLongitude(mLongitude);

        return locationCurr;
    }

    //

    /**
     * Find the user id. Firstly search from memory, if not exists, read value from preference.
     * @return -1 if failed.
     */
    private int findUserId() {
        // TODO:
        return 10001;
    }
}
