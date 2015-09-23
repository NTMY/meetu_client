package com.walfud.meetu.view;

import android.os.Bundle;

import com.walfud.meetu.BaseActivity;
import com.walfud.meetu.manager.PrefsManager;

/**
 * Created by walfud on 2015/8/18.
 */
public class LauncherActivity extends BaseActivity {

    public static final String TAG = "LauncherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (false) {
        } else if (PrefsManager.getInstance().getShowSplash()) {
            SplashActivity.startActivity(this);
        } else {
            LoginActivity.startActivity(this);
        }

        finish();
    }
}
