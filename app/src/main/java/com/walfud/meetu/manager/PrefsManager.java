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

    public static final String PREFS_PHONE_NUM = "phone_num";
    public String getPhoneNum() {
        return mSharePreference.getString(PREFS_PHONE_NUM, "");
    }
    public void setPhoneNum(String phoneNum) {
        mSharePreference.edit().putString(PREFS_PHONE_NUM, phoneNum).commit();
    }

    public static final String PREFS_PASSWORD = "password";
    public String getPassword() {
        return mSharePreference.getString(PREFS_PASSWORD, "");
    }
    public void setPassword(String password) {
        mSharePreference.edit().putString(PREFS_PASSWORD, password).commit();
    }

    public static final String PREFS_AUTO_REPORT = "auto_report";
    public boolean isAutoReport() {
        return mSharePreference.getBoolean(PREFS_AUTO_REPORT, true);
    }
    public void setAutoReport(boolean autoReport) {
        mSharePreference.edit().putBoolean(PREFS_AUTO_REPORT, autoReport).commit();
    }

    public static final String PREFS_AUTO_SEARCH = "auto_search";
    public boolean isAutoSearch() {
        return mSharePreference.getBoolean(PREFS_AUTO_SEARCH, true);
    }
    public void setAutoSearch(boolean autoSearch) {
        mSharePreference.edit().putBoolean(PREFS_AUTO_SEARCH, autoSearch).commit();
    }

    public static final String PREFS_SPLASH = "splash";
    public void setShowSplash(boolean showSplash) {
        mSharePreference.edit().putBoolean(PREFS_SPLASH, showSplash).commit();
    }
    public boolean getShowSplash() {
        return mSharePreference.getBoolean(PREFS_SPLASH, true);
    }
}
