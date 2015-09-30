package com.walfud.meetu.util;

import android.content.Context;

import com.walfud.meetu.database.User;
import com.walfud.meetu.view.FriendFragment;
import com.walfud.meetu.view.ProfileCardView;

import org.meetu.model.LocationCurr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walfud on 2015/9/28.
 */
public class Transformer {

    // LocationCurr
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
//        friendData.portraitUri =
        friendData.nick = String.valueOf(locationCurr.getUserId());
//        friendData.mood

        return friendData;
    }

    // com.walfud.meetu.database.User
    public static ProfileCardView.ProfileData user2ProfileData(Context context, User user) {
        ProfileCardView.ProfileData profileData = new ProfileCardView.ProfileData();
        profileData.portraitUri = user.getPortraitUri();
        profileData.nick = String.valueOf(user.getUserId());
        profileData.mood = user.getMood();

        return profileData;
    }

    public static org.meetu.model.User user2User(User dbUser) {
        org.meetu.model.User user = new org.meetu.model.User();
        user.setId((int) (long) dbUser.getUserId());
        user.setPwd(dbUser.getPassword());
        user.setNickname(dbUser.getNick());
        user.setMood(dbUser.getMood());
        user.setStatus(dbUser.getPortraitUri());
        user.setMobile(dbUser.getPhoneNum());
        user.setImei(dbUser.getImei());

        return user;
    }

    public static FriendFragment.FriendData user2FriendData(User user) {
        FriendFragment.FriendData friendData = new FriendFragment.FriendData();
        friendData.portraitUri = user.getPortraitUri();
        friendData.nick = user.getNick();
        friendData.mood = user.getMood();

        return friendData;
    }

    // org.meetu.model.User
    public static User user2User(org.meetu.model.User user) {
        User dbUser = new User();
        dbUser.setUserId((long) user.getId());
        dbUser.setPassword(user.getPwd());
        dbUser.setNick(user.getNickname());
        dbUser.setMood(user.getMood());
//        dbUser.setPortrait(user.getImgUrl());
        dbUser.setPortraitUri("");
        dbUser.setPhoneNum(user.getMobile());
        dbUser.setImei(user.getImei());

        return dbUser;
    }
    public static List<User> userList2UserList(List<org.meetu.model.User> userList) {
        List<User> dbUserList = new ArrayList<>();
        for (org.meetu.model.User user : userList) {
            dbUserList.add(user2User(user));
        }
        return dbUserList;
    }

    public static FriendFragment.FriendData user2FriendData(org.meetu.model.User user) {
        FriendFragment.FriendData friendData = new FriendFragment.FriendData();
//        friendData.portraitUri = user.getImgUrl();
        friendData.portraitUri = "";
        friendData.nick = user.getNickname();
        friendData.mood = user.getMood();

        return friendData;
    }
    public static List<FriendFragment.FriendData> userList2FriendDataList(List<org.meetu.model.User> userList) {
        List<FriendFragment.FriendData> friendDataList = new ArrayList<>();
        for (org.meetu.model.User user : userList) {
            friendDataList.add(user2FriendData(user));
        }
        return friendDataList;
    }

    // FriendData
    public static ProfileCardView.ProfileData friendData2ProfileData(FriendFragment.FriendData friendData) {
        ProfileCardView.ProfileData profileData = new ProfileCardView.ProfileData();
        profileData.portraitUri = friendData.portraitUri;
        profileData.nick = friendData.nick;
        profileData.mood = friendData.mood;

        return profileData;
    }
}
