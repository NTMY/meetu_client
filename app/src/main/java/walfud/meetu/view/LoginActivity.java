package walfud.meetu.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

import walfud.meetu.R;
import walfud.meetu.presenter.LoginPresenter;


/**
 * Created by walfud on 2015/8/2.
 */
public class LoginActivity extends Activity {

    public static final String TAG = "LoginActivity";

    private EditText mPhoneNum;
    private EditText mPassword;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this);
        mPresenter.setOnLoginListener(new LoginPresenter.OnLoginListener() {
            @Override
            public void onRegister(User user) {
                Toast.makeText(LoginActivity.this,
                        String.format("注册成功: id(%s)", user.getId()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogin(User user) {
                Toast.makeText(LoginActivity.this,
                        String.format("登陆成功: id(%s)", user.getId()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(UserAccessDto userAccessDto) {
                Toast.makeText(LoginActivity.this,
                        String.format("注册/登陆失败: %s(%s)", userAccessDto.getErrMsg(), userAccessDto.getErrCode()),
                        Toast.LENGTH_LONG).show();
            }
        });

        mPhoneNum = (EditText) findViewById(R.id.phone_num);
        mPassword = (EditText) findViewById(R.id.password);
    }

    public void onClick(View v) {
        final User user = new User();
        user.setMobile(mPhoneNum.getText().toString());
        user.setPwd(mPassword.getText().toString());

        mPresenter.onClickLogin(user);
    }
}
