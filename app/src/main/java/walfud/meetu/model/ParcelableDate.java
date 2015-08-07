package walfud.meetu.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by walfud on 2015/8/7.
 */
public class ParcelableDate implements Parcelable {

    private transient long milliseconds;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(milliseconds);
    }

    public static final Parcelable.Creator<ParcelableDate> CREATOR = new Parcelable.Creator<ParcelableDate>() {
        @Override
        public ParcelableDate createFromParcel(Parcel source) {
            return new ParcelableDate(source);
        }

        @Override
        public ParcelableDate[] newArray(int size) {
            return new ParcelableDate[0];
        }
    };

    public ParcelableDate(Parcel source) {
        milliseconds = source.readLong();
    }

    public ParcelableDate(Date date) {
        if (date == null) {
            return;
        }

        milliseconds = date.getTime();
    }

    public Date toDate() {
        return new Date(milliseconds);
    }
}
