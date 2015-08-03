package walfud.meetu.presenter;

import android.os.AsyncTask;
import android.widget.Toast;

import org.meetu.client.handler.UserAccessHandler;
import org.meetu.client.listener.UserAccessListener;
import org.meetu.constant.Constant;
import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

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
            protected UserAccessDto doInBackground(Void... params) {
                try {
                    new UserAccessHandler().onAccess(new UserAccessListener() {
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

                if (false) {
                } else if (Constant.ACCESS_STATUS_REG.equals(userAccessDto.getAccess_status())) {
                    // Register success
                    MainActivity.startActivity(mView);
                    mView.finish();

                    if (mOnLoginListener != null) {
                        mOnLoginListener.onRegister(userAccessDto.getUser());
                    }
                } else if (Constant.ACCESS_STATUS_LOGIN.equals(userAccessDto.getAccess_status())) {
                    // Login success
                    MainActivity.startActivity(mView);
                    mView.finish();

                    if (mOnLoginListener != null) {
                        mOnLoginListener.onLogin(userAccessDto.getUser());
                    }
                } else {
                    // Fail
                    if (mOnLoginListener != null) {
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
