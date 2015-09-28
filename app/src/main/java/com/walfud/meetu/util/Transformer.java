package com.walfud.meetu.util;

import com.walfud.meetu.database.User;

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
}
