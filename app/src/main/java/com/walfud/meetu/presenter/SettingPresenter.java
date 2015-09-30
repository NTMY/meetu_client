package com.walfud.meetu.presenter;

import android.preference.Preference;

import com.walfud.meetu.MainService;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.view.SettingActivity;

/**
 * Created by walfud on 9/30/15.
 */
public class SettingPresenter implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "SettingPresenter";

    private SettingActivity mView;
    private MainService mMainService;

    public SettingPresenter(SettingActivity view) {
        mView = view;
        mMainService = MainService.getInstance();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        boolean suc = false;

        String key = preference.getKey();
        if (false) {

        } else if (PrefsManager.PREFS_AUTO_REPORT.equals(key)) {

            boolean autoReport = (boolean) newValue;
            mMainService.setAutoReportSelf(autoReport);

            suc = autoReport == mMainService.isAutoReport();

        } else if (PrefsManager.PREFS_AUTO_SEARCH.equals(key)) {

            boolean autoSearch = (boolean) newValue;
            mMainService.setAutoSearchNearby(autoSearch);

            suc = autoSearch == mMainService.isAutoSearch();

        } else {

        }

        //
        mView.showTip(suc, preference);

        return true;
    }
}
