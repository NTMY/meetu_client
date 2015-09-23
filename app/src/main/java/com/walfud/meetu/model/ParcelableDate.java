package com.walfud.meetu.model;

import java.util.Date;

/**
 * Created by walfud on 2015/8/7.
 */
@com.baoyz.pg.Parcelable
public class ParcelableDate extends Date {
    public ParcelableDate() {
        super();
    }

    public ParcelableDate(Date date) {
        super();
        setTime(date.getTime());
    }
}
