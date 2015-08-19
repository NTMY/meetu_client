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
    public static final String KEY_USER_TOKEN = "KEY_USER_TOKEN";
    public static final int VALUE_INVALID_USER_ID = -1;
    public void setUserId(int userId) {
        mSharePreference.edit().putInt(KEY_USER_TOKEN, userId).commit();
    }
    public int getUserId() {
        return mSharePreference.getInt(KEY_USER_TOKEN, VALUE_INVALID_USER_ID);
    }

    private static final String KEY_SPLASH = "KEY_SPLASH";
    public void setShowSplash(boolean showSplash) {
        mSharePreference.edit().putBoolean(KEY_SPLASH, showSplash).commit();
    }
    public boolean getShowSplash() {
        return mSharePreference.getBoolean(KEY_SPLASH, true);
    }
}
