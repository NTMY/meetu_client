package com.walfud.meetu.manager;

import android.content.Context;

import com.walfud.meetu.Constants;
import com.walfud.meetu.MeetUApplication;
import com.walfud.meetu.database.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/9/20.
 */
public class UserManager {

    public static final String TAG = "UserManager";
    private static UserManager sInstance;

    private User mUser;
    private List<User> mFriendList;
    private PrefsManager mPrefsManager;

    private UserManager(Context context) {
        mUser = new User();
        mFriendList = new ArrayList<>();
        mPrefsManager = PrefsManager.getInstance();
    }

    // Function
    public User getCurrentUser() {
        return mUser;
    }

    public void setCurrentUser(User user) {
        mUser = user;
    }

    public List<User> getFriendList() {
        return mFriendList;
    }

    public void setFriendList(List<User> friendList) {
        mFriendList = friendList;
    }

    public User getUser(long userId) {
        User user = new User();
        if (userId == mUser.getUserId()) {
            user = mUser;
        } else {
            for (User friendUser : mFriendList) {
                if (friendUser.getUserId() == userId) {
                    user = friendUser;
                    break;
                }
            }
        }

        return user;
    }

    public void save(User user) {
        mPrefsManager.setUserId(user.getUserId());
        mPrefsManager.setPhoneNum(user.getPhoneNum());
        mPrefsManager.setPassword(user.getPassword());
    }

    public User restore() {
        return new User(
                Constants.INVALID_ID,
                mPrefsManager.getUserId(),
                mPrefsManager.getPassword(),
                "",
                "",
                "",
                mPrefsManager.getPhoneNum(),
                ""
        );
    }

    // Helper
    public static UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager(MeetUApplication.getContext());
        }

        return sInstance;
    }
}
