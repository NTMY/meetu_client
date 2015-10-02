package com.walfud.meetu.presenter;

import android.os.AsyncTask;
import android.os.Bundle;

import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.view.LoginActivity;
import com.walfud.meetu.view.MainActivity;

import org.meetu.client.handler.UserHandler;
import org.meetu.client.listener.UserAccessListener;
import org.meetu.constant.Constant;
import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

/**
 * Created by walfud on 2015/8/3.
 */
public class LoginPresenter {

    public static final String TAG = "LoginPresenter";

    private LoginActivity mView;

    public LoginPresenter(LoginActivity view) {
        mView = view;
    }

    public interface OnLoginListener {
        void onRegister(User user);

        void onLogin(User user);

        void onFail(UserAccessDto userAccessDto);
    }

    // View Event
    public void onClickLogin(final User user) {
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
                    UserManager.getInstance().setCurrentUser(serverUser2ClientUser(userAccessDto.getUser()));

                    if (mOnLoginListener != null) {
                        if (Constant.ACCESS_STATUS_REG.equals(userAccessDto.getAccess_status())) {
                            // Register
                            mOnLoginListener.onRegister(userAccessDto.getUser());
                        } else {
                            // Login
                            mOnLoginListener.onLogin(userAccessDto.getUser());
                        }
                    }

                    // Start main activity
                    Bundle bundle = new Bundle();
                    MainActivity.startActivity(mView, bundle);
                    mView.finish();
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
}
