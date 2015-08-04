package walfud.meetu.model;

import android.content.Context;
import android.content.SharedPreferences;

import walfud.meetu.MeetUApplication;

/**
 * Created by walfud on 2015/8/3.
 */
public class SettingModel {

    private static SettingModel mInstance;
    public static SettingModel getInstance() {
        if (mInstance == null) {
            mInstance = new SettingModel();
        }

        return mInstance;
    }

    private static final String PREFS_NAME = "settings";
    private SharedPreferences mSharePreferences;
    private Context mContext;
    protected SettingModel() {
        mContext = MeetUApplication.getContext();
        mSharePreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static final String KEY_USER_TOKEN = "key_user_token";
    public void saveToken(String token) {
        mSharePreferences.edit().putString(KEY_USER_TOKEN, token).commit();
    }
    public void retriveToken(String token) {
        mSharePreferences.getString(KEY_USER_TOKEN, "");
    }
}
