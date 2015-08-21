package walfud.meetu.presenter;

import android.os.AsyncTask;
import android.os.Bundle;

import com.baoyz.pg.PG;

import org.meetu.client.handler.UserHandler;
import org.meetu.client.listener.UserAccessListener;
import org.meetu.constant.Constant;
import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

import walfud.meetu.Constants;
import walfud.meetu.model.ParcelableUser;
import walfud.meetu.model.PrefsModel;
import walfud.meetu.view.LoginActivity;
import walfud.meetu.view.MainActivity;

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
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.KEY_USER, PG.convertParcelable(new ParcelableUser(userAccessDto.getUser())));
                    MainActivity.startActivity(mView, bundle);
                    mView.finish();

                    PrefsModel.getInstance().setUserId(userAccessDto.getUser().getId());

                    if (mOnLoginListener != null) {
                        if (Constant.ACCESS_STATUS_REG.equals(userAccessDto.getAccess_status())) {
                            // Register
                            mOnLoginListener.onRegister(userAccessDto.getUser());
                        } else {
                            // Login
                            mOnLoginListener.onLogin(userAccessDto.getUser());
                        }
                    }
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
}
