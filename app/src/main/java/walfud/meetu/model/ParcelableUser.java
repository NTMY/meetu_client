package walfud.meetu.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.meetu.model.User;

/**
 * Created by walfud on 2015/8/7.
 */
public class ParcelableUser implements Parcelable {
    private Integer id;
    private String mobile;
    private String pwd;
    private String imei;
    private String name;
    private String nickname;
    private String birthdate;
    private String gender;
    private String qq;
    private String wechat;
    private String email;
    private String company;
    private String company_addr;
    private String home_addr;
    private String regtime;
    private String mood;
    private String status;
    private ParcelableLocationCurr locCurr;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id != null ? id : 0);
        dest.writeString(mobile != null ? mobile : "");
        dest.writeString(pwd != null ? pwd : "");
        dest.writeString(imei != null ? imei : "");
        dest.writeString(name != null ? name : "");
        dest.writeString(nickname != null ? nickname : "");
        dest.writeString(birthdate != null ? birthdate : "");
        dest.writeString(gender != null ? gender : "");
        dest.writeString(qq != null ? qq : "");
        dest.writeString(wechat != null ? wechat : "");
        dest.writeString(email != null ? email : "");
        dest.writeString(company != null ? company : "");
        dest.writeString(company_addr != null ? company_addr : "");
        dest.writeString(home_addr != null ? home_addr : "");
        dest.writeString(regtime != null ? regtime : "");
        dest.writeString(mood != null ? mood : "");
        dest.writeString(status != null ? status : "");
        dest.writeParcelable(locCurr, flags);
    }

    public static final Parcelable.Creator<ParcelableUser> CREATOR = new Parcelable.Creator<ParcelableUser>() {
        @Override
        public ParcelableUser createFromParcel(Parcel source) {
            return new ParcelableUser(source);
        }

        @Override
        public ParcelableUser[] newArray(int size) {
            return new ParcelableUser[0];
        }
    };

    public ParcelableUser(Parcel source) {
        id = source.readInt();
        mobile = source.readString();
        pwd = source.readString();
        imei = source.readString();
        name = source.readString();
        nickname = source.readString();
        birthdate = source.readString();
        gender = source.readString();
        qq = source.readString();
        wechat = source.readString();
        email = source.readString();
        company = source.readString();
        company_addr = source.readString();
        home_addr = source.readString();
        regtime = source.readString();
        mood = source.readString();
        status = source.readString();
        locCurr = source.readParcelable(ParcelableUser.class.getClassLoader());
    }

    public ParcelableUser(User user) {
        if (user == null) {
            return;
        }

        id = user.getId();
        mobile = user.getMobile();
        pwd = user.getPwd();
        imei = user.getImei();
        name = user.getName();
        nickname = user.getNickname();
        birthdate = user.getBirthdate();
        gender = user.getGender();
        qq = user.getQq();
        wechat = user.getWechat();
        email = user.getEmail();
        company = user.getCompany();
        company_addr = user.getCompany_addr();
        home_addr = user.getHome_addr();
        regtime = user.getRegtime();
        mood = user.getMood();
        status = user.getStatus();
        locCurr = new ParcelableLocationCurr(user.getLocCurr());
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setMobile(mobile);
        user.setPwd(pwd);
        user.setImei(imei);
        user.setName(name);
        user.setNickname(nickname);
        user.setBirthdate(birthdate);
        user.setGender(gender);
        user.setQq(qq);
        user.setWechat(wechat);
        user.setEmail(email);
        user.setCompany(company);
        user.setCompany_addr(company_addr);
        user.setHome_addr(home_addr);
        user.setRegtime(regtime);
        user.setMood(mood);
        user.setStatus(status);
        user.setLocCurr(locCurr.toLocationCurr());

        return user;
    }
}
