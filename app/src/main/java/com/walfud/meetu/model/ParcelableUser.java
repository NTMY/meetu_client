package com.walfud.meetu.model;

import org.meetu.model.User;

/**
 * Created by walfud on 2015/8/7.
 */
@com.baoyz.pg.Parcelable
public class ParcelableUser extends User {
    public ParcelableUser() {
    }

    public ParcelableUser(User user) {
        super();
//        super.copyFrom(user);
        setId(user.getId());
        setMobile(user.getMobile());
        setPwd(user.getPwd());
        setImei(user.getImei());
        setName(user.getName());
        setNickname(user.getNickname());
        setBirthdate(user.getBirthdate());
        setGender(user.getGender());
        setQq(user.getQq());
        setWechat(user.getWechat());
        setEmail(user.getEmail());
        setCompany(user.getCompany());
        setCompany_addr(user.getCompany_addr());
        setHome_addr(user.getHome_addr());
        setRegtime(user.getRegtime());
        setMood(user.getMood());
        setStatus(user.getStatus());
    }
}
