package com.walfud.meetu.presenter;

import android.os.AsyncTask;
import android.os.Bundle;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;
import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.view.LoginActivity;
import com.walfud.meetu.view.MainActivity;
import com.walfud.walle.widget.HardwareUtils;

import org.meetu.client.handler.UserHandler;
import org.meetu.client.listener.UserAccessListener;
import org.meetu.client.listener.UserUpdateListener;
import org.meetu.constant.Constant;
import org.meetu.dto.BaseDto;
import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

/**
 * Created by walfud on 2015/8/3.
 */
public class LoginPresenter {

    public static final String TAG = "LoginPresenter";

    private LoginActivity mView;
    private UserManager mUserManager;

    public LoginPresenter(LoginActivity view) {
        mView = view;
        mUserManager = UserManager.getInstance();
    }

    public interface OnLoginListener {
        void onRegister(User user);

        void onLogin(User user);

        void onFail(UserAccessDto userAccessDto);
    }

    // View Event
    public void onClickLogin(final User user) {
        // Login procedure
        new AsyncTask<Void, Void, UserAccessDto>() {

            private UserAccessDto mUserAccessDto;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mView.setUiState(LoginActivity.UI_DISABLE);
            }

            @Override
            protected UserAccessDto doInBackground(Void... params) {
                try {
                    // Login
                    new UserHandler().onAccess(new UserAccessListener() {
                        @Override
                        public void access(UserAccessDto userAccessDto) {
                            mUserAccessDto = userAccessDto;
                        }
                    }, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return mUserAccessDto;
            }

            @Override
            protected void onPostExecute(UserAccessDto userAccessDto) {
                super.onPostExecute(userAccessDto);

                if (userAccessDto == null) {
                    // Fail
                    if (mOnLoginListener != null) {
                        mView.setUiState(LoginActivity.UI_ENABLE);
                        mOnLoginListener.onFail(userAccessDto);
                    }

                    return;
                }

                if (false) {
                } else if (Constant.ACCESS_STATUS_REG.equals(userAccessDto.getAccess_status())
                        || Constant.ACCESS_STATUS_LOGIN.equals(userAccessDto.getAccess_status())) {
                    // Success
                    mUserManager.setCurrentUser(serverUser2ClientUser(userAccessDto.getUser()));
                    mUserManager.getCurrentUser().setPassword(user.getPwd());

                    // Save login info
                    mUserManager.save(mUserManager.getCurrentUser());

                    if (mOnLoginListener != null) {
                        if (Constant.ACCESS_STATUS_REG.equals(userAccessDto.getAccess_status())) {
                            // Register
                            mOnLoginListener.onRegister(userAccessDto.getUser());
                            Answers.getInstance().logSignUp(new SignUpEvent()
                                    .putCustomAttribute("id", userAccessDto.getUser().getId())
                                    .putCustomAttribute("imei", HardwareUtils.getImei()));
                        } else {
                            // Login
                            mOnLoginListener.onLogin(userAccessDto.getUser());
                            Answers.getInstance().logLogin(new LoginEvent()
                                    .putCustomAttribute("id", userAccessDto.getUser().getId())
                                    .putCustomAttribute("imei", HardwareUtils.getImei()));
                        }
                    }

                    // Start main activity
                    Bundle bundle = new Bundle();
                    MainActivity.startActivity(mView, bundle);
                    mView.finish();

                    // Upload imei
                    uploadImei(userAccessDto.getUser().getId(), HardwareUtils.getImei());
                } else {
                    // Fail
                    if (mOnLoginListener != null) {
                        mView.setUiState(LoginActivity.UI_ENABLE);
                        mOnLoginListener.onFail(userAccessDto);
                    }
                }
            }
        }.execute();
    }

    // Function
    private OnLoginListener mOnLoginListener;

    public void setOnLoginListener(OnLoginListener loginListener) {
        mOnLoginListener = loginListener;
    }

    // Internal
    private com.walfud.meetu.database.User serverUser2ClientUser(User serverUser) {
        return new com.walfud.meetu.database.User(
                null,
                (long) serverUser.getId(),
                serverUser.getPwd(),
                serverUser.getNickname(),
                serverUser.getMood(),
                serverUser.getImgUrlReal(),
                serverUser.getMobile(),
                serverUser.getImei()
        );
    }

    private void uploadImei(long userId, String imei) {
        User imeiInfo = new User();
        imeiInfo.setId((int) userId);
        imeiInfo.setImei(imei);
        new AsyncTask<User, Void, Void>() {
            @Override
            protected Void doInBackground(User... params) {
                User user = params[0];
                new UserHandler().onUpdate(new UserUpdateListener() {
                    @Override
                    public void update(BaseDto baseDto) {

                    }
                }, user);

                return null;
            }
        }.execute(imeiInfo);
    }
}
