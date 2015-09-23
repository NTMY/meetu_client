package com.walfud.meetu.model;

import org.meetu.model.LocationCurr;

/**
 * Created by walfud on 2015/8/7.
 */
@com.baoyz.pg.Parcelable
public class ParcelableLocationCurr extends LocationCurr {
    public ParcelableLocationCurr() {
        super();
    }

    public ParcelableLocationCurr(LocationCurr locationCurr) {
        super();
        super.copyFrom(locationCurr);
    }
}
