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
            sInstance = new PrefsModel();
            sInstance.init();
        }

        return sInstance;
    }

    private Context mContext;
    private SharedPreferences mSharePreference;
    private void init() {
        mContext = MeetUApplication.getContext();
        mSharePreference = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    //
    private static final String PREFS_KEY_SPLASH = "PREFS_KEY_SPLASH";
    public void setShowSplash(boolean showSplash) {
        mSharePreference.edit().putBoolean(PREFS_KEY_SPLASH, showSplash).commit();
    }
    public boolean getShowSplash() {
        return mSharePreference.getBoolean(PREFS_KEY_SPLASH, true);
    }
}
