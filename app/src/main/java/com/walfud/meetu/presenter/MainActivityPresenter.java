package com.walfud.meetu.presenter;

import com.walfud.meetu.MainService;
import com.walfud.meetu.Utils;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.view.MainActivity;

/**
 * Created by song on 2015/6/21.
 */
public class MainActivityPresenter {

    public static final String TAG = "MainActivityPresenter";

    private MainActivity mMainActivity;
    private MainService mMainService;
    private PrefsManager mPrefsManager;

    public MainActivityPresenter(MainActivity view) {
        mMainActivity = view;
        mMainService = MainService.getInstance();
        mPrefsManager = PrefsManager.getInstance();

        //
        mMainService.configWithSetting();
    }

    // Function

    public void onExit() {
        MainService.stopService();
        Utils.clearNotification(mMainActivity, Utils.NOTIFICATION_ID);
        mMainActivity.finish();
    }
}
