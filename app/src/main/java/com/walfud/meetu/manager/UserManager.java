package com.walfud.meetu.manager;

import android.content.Context;

import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.database.User;

/**
 * Created by walfud on 2015/9/20.
 */
public class UserManager {

    public static final String TAG = "UserManager";
    private static UserManager sInstance;

//    private Context mContext;
//    private PrefsManager mPrefsManager;
//    private DbManager mDbManager;
    private User mUser;

    private UserManager(Context context) {
//        mContext = context;
//        mPrefsManager = PrefsManager.getInstance();
//        mDbManager = DbManager.getInstance();
    }

    // Function
    public User getCurrentUser() {
//        long userId = PrefsManager.getInstance().getUserId();
//        return DbManager.getInstance().getUser(userId);
        return mUser;
    }

    public void setCurrentUser(User user) {
//        mPrefsManager.setUserId(user.getUserId());
//        mDbManager.insertOrUpdate(user);
        mUser = user;
    }

    public String getImei() {
        return mUser.getImei();
    }

    public Long getUserId() {
        return mUser.getUserId();
    }

    public String getPassword() {
        return mUser.getPassword();
    }

    public void setNick(String nick) {
        mUser.setNick(nick);
    }

    public void setUserId(Long userId) {
        mUser.setUserId(userId);
    }

    public void setPassword(String password) {
        mUser.setPassword(password);
    }

    public void setPortraitUri(String portraitUri) {
        mUser.setPortraitUri(portraitUri);
    }

    public void setPhoneNum(String phoneNum) {
        mUser.setPhoneNum(phoneNum);
    }

    public String getPortraitUri() {
        return mUser.getPortraitUri();
    }

    public void setMood(String mood) {
        mUser.setMood(mood);
    }

    public void setImei(String imei) {
        mUser.setImei(imei);
    }

    public String getMood() {
        return mUser.getMood();
    }

    public String getPhoneNum() {
        return mUser.getPhoneNum();
    }

    public String getNick() {
        return mUser.getNick();
    }

    // Helper
    public static UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager(MeetUApplication.getContext());
        }

        return sInstance;
    }
}
