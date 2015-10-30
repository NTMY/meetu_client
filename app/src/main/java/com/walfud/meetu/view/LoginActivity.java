package com.walfud.meetu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.R;
import com.walfud.meetu.manager.UserManager;
import com.walfud.meetu.presenter.LoginPresenter;

import org.meetu.dto.UserAccessDto;
import org.meetu.model.User;


/**
 * Created by walfud on 2015/8/2.
 */
public class LoginActivity extends BaseActivity
        implements View.OnClickListener, TextView.OnEditorActionListener {

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

        mPhoneNum.setOnEditorActionListener(this);
        mPassword.setOnEditorActionListener(this);

        // Auto fill last login info
        com.walfud.meetu.database.User loginUser = UserManager.getInstance().restore();
        mPhoneNum.setText(loginUser.getPhoneNum());
        mPassword.setText(loginUser.getPassword());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.phone_num:
                mPassword.requestFocus();
                return true;

            case R.id.password:
                mOk.performClick();
                return true;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        String phoneNum = mPhoneNum.getText().toString();
        String password = mPassword.getText().toString();

        // For dev
        if (TextUtils.isEmpty(phoneNum) && TextUtils.isEmpty(password)) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            if (false) {
                // Stub
            } else if (TextUtils.equals("355470063007237", imei)) {
                // walfud -- 4559
                mPhoneNum.setText("18511334559");
                mPassword.setText("walfud");
            } else if (TextUtils.equals("867569026088001", imei)) {
                // walfud -- 2475
                mPhoneNum.setText("13911592475");
                mPassword.setText("walfud");
            } else if (TextUtils.equals("355464060277356", imei)) {
                // Murphy
                mPhoneNum.setText("15011448840");
                mPassword.setText("1");
            } else if (TextUtils.equals("000000000000000", imei)) {
                // Test account
                mPhoneNum.setText("13800138000");
                mPassword.setText("MeetU");
            } else {
                // Nothing
            }

            if (!TextUtils.isEmpty(mPhoneNum.getText().toString())) {
                return;
            }
        }

        User user = new User();
        user.setMobile(phoneNum);
        user.setPwd(password);
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
