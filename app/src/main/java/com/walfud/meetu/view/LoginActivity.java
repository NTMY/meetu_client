package com.walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.presenter.LoginPresenter;

import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;


/**
 * Created by walfud on 2015/8/2.
 */
public class LoginActivity extends BaseActivity
        implements View.OnLongClickListener {

    public static final String TAG = "LoginActivity";

    private ImageButton mOk;
    private EditText mPhoneNum;
    private EditText mPassword;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mOk = $(R.id.ok);
        mPhoneNum = $(R.id.phone_num);
        mPassword = $(R.id.password);

//        if (BuildConfig.DEBUG) {
//            finish();
//            MainActivity.startActivity(this, null);
//        }

        mPresenter = new LoginPresenter(this);
        mPresenter.setOnLoginListener(new LoginPresenter.OnLoginListener() {
            @Override
            public void onRegister(User user) {
                Toast.makeText(LoginActivity.this,
                        String.format("Register Success: id(%s)", user.getId()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLogin(User user) {
                Toast.makeText(LoginActivity.this,
                        String.format("Login Success: id(%s)", user.getId()),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(UserAccessDto userAccessDto) {
                Toast.makeText(LoginActivity.this,
                        userAccessDto == null ? "Network fail"
                                        : String.format("Register/Login fail: %s(%s)", userAccessDto.getErrMsg(), userAccessDto.getErrCode()),
                        Toast.LENGTH_LONG).show();
            }
        });

        mOk.setOnLongClickListener(this);
    }

    public void onClick(View v) {
        final User user = new User();
        user.setMobile(mPhoneNum.getText().toString());
        user.setPwd(mPassword.getText().toString());

        mPresenter.onClickLogin(user);
    }

    /**
     * Login as Developer
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        mPhoneNum.setText("13800138000");
        mPassword.setText("MeetU");

        mOk.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOk.performClick();
            }
        }, 1000);

        return true;
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
