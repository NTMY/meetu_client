package walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import walfud.meetu.R;
import walfud.meetu.presenter.LoginPresenter;


/**
 * Created by walfud on 2015/8/2.
 */
public class LoginActivity extends RoboActivity {

    public static final String TAG = "LoginActivity";

    @InjectView(R.id.ok)
    private ImageButton mOk;
    @InjectView(R.id.phone_num)
    private EditText mPhoneNum;
    @InjectView(R.id.password)
    private EditText mPassword;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        if (BuildConfig.DEBUG) {
//            finish();
//            MainActivity.startActivity(this, null);
//        }

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
                        userAccessDto == null ? "网络失败"
                                        : String.format("注册/登陆失败: %s(%s)", userAccessDto.getErrMsg(), userAccessDto.getErrCode()),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View v) {
        final User user = new User();
        user.setMobile(mPhoneNum.getText().toString());
        user.setPwd(mPassword.getText().toString());

        mPresenter.onClickLogin(user);
    }

    // View Function
    public static final int UI_DISABLE  = 1 << 0;
    public static final int UI_ENABLE   = 1 << 1;
    public void setUiState(int state) {
        switch (state) {
            case UI_DISABLE:
                setEnable(false);
                break;

            case UI_ENABLE:
                setEnable(true);
                break;

            default:
                break;
        }
    }

    //
    private void setEnable(boolean enable) {
        mPhoneNum.setEnabled(enable);
        mPassword.setEnabled(enable);
        mOk.setEnabled(enable);
    }

    //
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
