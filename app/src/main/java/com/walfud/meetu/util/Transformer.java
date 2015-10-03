package com.walfud.meetu.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

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
        profileData.portraitUri = !TextUtils.isEmpty(user.getPortraitUrl()) ? Uri.parse(user.getPortraitUrl()) : Uri.EMPTY;
        profileData.nick = String.valueOf(user.getUserId());
        profileData.mood = user.getMood();

        return profileData;
    }

    public static org.meetu.model.User user2ServerUser(User user) {
        org.meetu.model.User serverUser = new org.meetu.model.User();
        serverUser.setId((int) (long) user.getUserId());
        serverUser.setPwd(user.getPassword());
        serverUser.setNickname(user.getNick());
        serverUser.setMood(user.getMood());
        serverUser.setStatus(user.getPortraitUrl());
        serverUser.setMobile(user.getPhoneNum());
        serverUser.setImei(user.getImei());

        return serverUser;
    }

    public static List<org.meetu.model.User> userList2DbUserList(List<User> userList) {
        List<org.meetu.model.User> serverUserList = new ArrayList<>();
        for (User user : userList) {
            serverUserList.add(user2ServerUser(user));
        }
        return serverUserList;
    }

    public static FriendFragment.FriendData user2FriendData(User user) {
        FriendFragment.FriendData friendData = new FriendFragment.FriendData();
        friendData.portraitUri = !TextUtils.isEmpty(user.getPortraitUrl()) ? Uri.parse(user.getPortraitUrl()) : Uri.EMPTY;
        friendData.nick = user.getNick();
        friendData.mood = user.getMood();

        return friendData;
    }

    public static List<FriendFragment.FriendData> userList2FriendDataList(List<User> userList) {
        List<FriendFragment.FriendData> friendDataList = new ArrayList<>();
        for (User user : userList) {
            friendDataList.add(user2FriendData(user));
        }
        return friendDataList;
    }

    // org.meetu.model.User
    public static User serverUser2User(org.meetu.model.User serverUser) {
        User user = new User();
        user.setUserId((long) serverUser.getId());
        user.setPassword(serverUser.getPwd());
        user.setNick(serverUser.getNickname());
        user.setMood(serverUser.getMood());
        user.setPortraitUrl(serverUser.getImgUrlReal());
        user.setPhoneNum(serverUser.getMobile());
        user.setImei(serverUser.getImei());

        return user;
    }
    public static List<User> serverUserList2UserList(List<org.meetu.model.User> serverUserList) {
        List<User> userList = new ArrayList<>();
        for (org.meetu.model.User user : serverUserList) {
            userList.add(serverUser2User(user));
        }
        return userList;
    }

    public static FriendFragment.FriendData serverUser2FriendData(org.meetu.model.User user) {
        FriendFragment.FriendData friendData = new FriendFragment.FriendData();
        friendData.portraitUri = !TextUtils.isEmpty(user.getImgUrlReal()) ? Uri.parse(user.getImgUrlReal()) : Uri.EMPTY;
        friendData.nick = user.getNickname();
        friendData.mood = user.getMood();

        return friendData;
    }
    public static List<FriendFragment.FriendData> serverUserList2FriendDataList(List<org.meetu.model.User> userList) {
        List<FriendFragment.FriendData> friendDataList = new ArrayList<>();
        for (org.meetu.model.User user : userList) {
            friendDataList.add(serverUser2FriendData(user));
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
