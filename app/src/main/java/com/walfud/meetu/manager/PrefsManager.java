package com.walfud.meetu.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.walfud.meetu.MeetUApplication;

/**
 * Created by walfud on 2015/8/18.
 */
public class PrefsManager {

    public static final String TAG = "PrefsManager";

    private static PrefsManager sInstance;
    public static PrefsManager getInstance() {
        if (sInstance == null) {
            sInstance = new PrefsManager(MeetUApplication.getContext());
        }

        return sInstance;
    }

    private SharedPreferences mSharePreference;
    private PrefsManager(Context context) {
        mSharePreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //
    private static final String PREFS_CURRENT_USER_ID = "PREFS_CURRENT_USER_ID";
    private static final long VALUE_INVALID_USER_ID = -1;
    public void setCurrentUserId(long userId) {
        mSharePreference.edit().putLong(PREFS_CURRENT_USER_ID, userId).apply();
    }
    public long getCurrentUserId() {
        return mSharePreference.getLong(PREFS_CURRENT_USER_ID, VALUE_INVALID_USER_ID);
    }

    private static final String PREFS_AUTO_REPORT = "PREFS_AUTO_REPORT";
    public boolean isAutoReport() {
        return mSharePreference.getBoolean(PREFS_AUTO_REPORT, true);
    }
    public void setAutoReport(boolean autoReport) {
        mSharePreference.edit().putBoolean(PREFS_AUTO_REPORT, autoReport).apply();
    }

    private static final String PREFS_AUTO_SEARCH = "PREFS_AUTO_SEARCH";
    public boolean isAutoSearch() {
        return mSharePreference.getBoolean(PREFS_AUTO_SEARCH, true);
    }
    public void setAutoSearch(boolean autoSearch) {
        mSharePreference.edit().putBoolean(PREFS_AUTO_SEARCH, autoSearch).apply();
    }

    private static final String PREFS_SPLASH = "PREFS_SPLASH";
    public void setShowSplash(boolean showSplash) {
        mSharePreference.edit().putBoolean(PREFS_SPLASH, showSplash).apply();
    }
    public boolean getShowSplash() {
        return mSharePreference.getBoolean(PREFS_SPLASH, true);
    }
}
