package com.walfud.meetu.model;

import org.meetu.model.LocationCurr;

import java.io.Serializable;

/**
 * Created by walfud on 2015/8/7.
 */
public class ParcelableLocationCurr extends LocationCurr implements Serializable {
    public ParcelableLocationCurr() {
        super();
    }

    public ParcelableLocationCurr(LocationCurr locationCurr) {
        super();
        super.copyFrom(locationCurr);
    }
}
