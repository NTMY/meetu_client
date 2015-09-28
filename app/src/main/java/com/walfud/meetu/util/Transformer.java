package com.walfud.meetu.util;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.walfud.meetu.R;
import com.walfud.meetu.database.User;
import com.walfud.meetu.view.FriendFragment;
import com.walfud.meetu.view.ProfileCardView;

import org.meetu.model.LocationCurr;

/**
 * Created by walfud on 2015/9/28.
 */
public class Transformer {

    public static User locationCurr2User(LocationCurr locationCurr) {
        User user = new User();
        user.setUserId((long) locationCurr.getUserId());
//        user.setPassword();
//        user.setNick();
//        user.setMood();
//        user.setPortrait();
//        user.setPhoneNum();
//        user.setImei();

        return user;
    }

    public static ProfileCardView.ProfileData locationCurr2ProfileData(LocationCurr locationCurr) {
        ProfileCardView.ProfileData profileData = new ProfileCardView.ProfileData();
//        profileData.portrait
        profileData.nick = String.valueOf(locationCurr.getUserId());
//        profileData.mood

        return profileData;
    }
    public static FriendFragment.FriendData locationCurr2FriendData(Context context, LocationCurr locationCurr) {
        FriendFragment.FriendData friendData = new FriendFragment.FriendData();
        friendData.portrait = BitmapFactory.decodeResource(context.getResources(), R.drawable.portrait);
        friendData.nick = String.valueOf(locationCurr.getUserId());
//        friendData.mood

        return friendData;
    }

    public static ProfileCardView.ProfileData user2ProfileData(Context context, User user) {
        ProfileCardView.ProfileData profileData = new ProfileCardView.ProfileData();
        profileData.portrait = BitmapFactory.decodeResource(context.getResources(), R.drawable.portrait);
        profileData.nick = String.valueOf(user.getUserId());
        profileData.mood = user.getMood();

        return profileData;
    }

    public static ProfileCardView.ProfileData friendData2ProfileData(FriendFragment.FriendData friendData) {
        ProfileCardView.ProfileData profileData = new ProfileCardView.ProfileData();
        profileData.portrait = friendData.portrait;
        profileData.nick = friendData.nick;
        profileData.mood = friendData.mood;

        return profileData;
    }
}
