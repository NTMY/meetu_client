package walfud.meetu.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.meetu.client.handler.UserAccessHandler;
import org.meetu.client.listener.UserAccessListener;
import org.meetu.constant.Constant;
import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

import walfud.meetu.R;


/**
 * Created by walfud on 2015/8/2.
 */
public class LoginActivity extends Activity {

    public static final String TAG = "LoginActivity";

    private EditText mPhoneNum;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPhoneNum = (EditText) findViewById(R.id.phone_num);
        mPassword = (EditText) findViewById(R.id.password);
    }

    public void onClick(View v) {
        final User user = new User();
        user.setMobile(mPhoneNum.getText().toString());
        user.setPwd(mPassword.getText().toString());

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
                    MainActivity.startActivity(LoginActivity.this);
                    LoginActivity.this.finish();

                    Toast.makeText(LoginActivity.this,
                            String.format("注册成功: id(%s)", userAccessDto.getUser().getId()),
                            Toast.LENGTH_SHORT).show();
                } else if (Constant.ACCESS_STATUS_LOGIN.equals(userAccessDto.getAccess_status())) {
                    MainActivity.startActivity(LoginActivity.this);
                    LoginActivity.this.finish();

                    Toast.makeText(LoginActivity.this,
                            String.format("登陆成功: id(%s)", userAccessDto.getUser().getId()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            String.format("注册/登陆失败: %s(%s)", userAccessDto.getErrMsg(), userAccessDto.getErrCode()),
                            Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

//        MainActivity.startActivity(LoginActivity.this);
//        LoginActivity.this.finish();
    }
}
