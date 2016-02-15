package com.walfud.meetu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by walfud on 2015/8/7.
 */
public class ParcelableDate extends Date implements Serializable {
    public ParcelableDate() {
        super();
    }

    public ParcelableDate(Date date) {
        super();
        setTime(date.getTime());
    }
}
