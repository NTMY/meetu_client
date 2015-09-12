package walfud.meetu.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import walfud.meetu.MeetUApplication;

/**
 * Created by walfud on 2015/8/18.
 */
public class PrefsModel {

    public static final String TAG = "PrefsModel";

    private static PrefsModel sInstance;
    public static PrefsModel getInstance() {
        if (sInstance == null) {
            sInstance = new PrefsModel(MeetUApplication.getContext());
        }

        return sInstance;
    }

    private SharedPreferences mSharePreference;
    protected PrefsModel(Context context) {
        mSharePreference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //
    private static final String PREFS_USER_TOKEN = "PREFS_USER_TOKEN";
    private static final int VALUE_INVALID_USER_ID = -1;
    public void setUserId(int userId) {
        mSharePreference.edit().putInt(PREFS_USER_TOKEN, userId).apply();
    }
    public int getUserId() {
        return mSharePreference.getInt(PREFS_USER_TOKEN, VALUE_INVALID_USER_ID);
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
