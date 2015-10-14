package com.walfud.meetu;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by walfud on 10/14/15.
 */
public class Statistics {

    public static final String TAG = "Statistics";
    private static Statistics sInstance;

    private Context mContext;

    private Statistics(Context context) {
        mContext = context;
    }

    // Function
    public void search() {
        MobclickAgent.onEvent(mContext, "search");
    }

    // Helper
    public static Statistics getInstance() {
        if (sInstance == null) {
            sInstance = new Statistics(MeetUApplication.getContext());
        }

        return sInstance;
    }
}
