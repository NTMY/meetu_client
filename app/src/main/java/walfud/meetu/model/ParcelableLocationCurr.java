package walfud.meetu.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.meetu.model.LocationCurr;

/**
 * Created by walfud on 2015/8/7.
 */
public class ParcelableLocationCurr implements Parcelable {
    private Integer userId;
    private Double longitude;
    private Double latitude;
    private String address;
    private String business;
    private ParcelableDate uploadTime;
    private Double minLong;
    private Double maxLong;
    private Double minLat;
    private Double maxLat;
    private ParcelableUser u;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId != null ? userId : 0);
        dest.writeDouble(longitude != null ? longitude : 0);
        dest.writeDouble(latitude != null ? latitude : 0);
        dest.writeString(address != null ? address : "");
        dest.writeString(business != null ? business : "");
        dest.writeParcelable(uploadTime, flags);
        dest.writeDouble(minLong != null ? minLong : 0);
        dest.writeDouble(maxLong != null ? maxLong : 0);
        dest.writeDouble(minLat != null ? minLat : 0);
        dest.writeDouble(maxLat != null ? maxLat : 0);
        dest.writeParcelable(u, flags);
    }

    public static final Parcelable.Creator<ParcelableLocationCurr> CREATOR = new Creator<ParcelableLocationCurr>() {
        @Override
        public ParcelableLocationCurr createFromParcel(Parcel source) {
            return new ParcelableLocationCurr(source);
        }

        @Override
        public ParcelableLocationCurr[] newArray(int size) {
            return new ParcelableLocationCurr[0];
        }
    };

    public ParcelableLocationCurr(Parcel source) {
        userId = source.readInt();
        longitude = source.readDouble();
        latitude = source.readDouble();
        address = source.readString();
        business = source.readString();
        uploadTime = source.readParcelable(ParcelableLocationCurr.class.getClassLoader());
        minLong = source.readDouble();
        maxLong = source.readDouble();
        minLat = source.readDouble();
        maxLat = source.readDouble();
        u = source.readParcelable(ParcelableLocationCurr.class.getClassLoader());
    }

    public ParcelableLocationCurr(LocationCurr locationCurr) {
        if (locationCurr == null) {
            return;
        }

        userId = locationCurr.getUserId();
        longitude = locationCurr.getLongitude();
        latitude = locationCurr.getLatitude();
        address = locationCurr.getAddress();
        business = locationCurr.getBusiness();
        uploadTime = new ParcelableDate(locationCurr.getUploadTime());
        minLong = locationCurr.getMinLong();
        maxLong = locationCurr.getMaxLong();
        minLat = locationCurr.getMinLat();
        maxLat = locationCurr.getMaxLat();
        u = new ParcelableUser(locationCurr.getU());
    }

    public LocationCurr toLocationCurr() {
        LocationCurr locationCurr = new LocationCurr();
        locationCurr.setUserId(userId);
        locationCurr.setLongitude(longitude);
        locationCurr.setLatitude(latitude);
        locationCurr.setAddress(address);
        locationCurr.setBusiness(business);
        locationCurr.setUploadTime(uploadTime != null ? uploadTime.toDate() : null);
        locationCurr.setMinLong(minLong);
        locationCurr.setMaxLong(maxLong);
        locationCurr.setMinLat(minLat);
        locationCurr.setMaxLat(maxLat);
        locationCurr.setU(u != null ? u.toUser() : null);

        return locationCurr;
    }
}
