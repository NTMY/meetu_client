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
            // Nothing
        } else if (PrefsManager.PREFS_AUTO_REPORT.equals(key)) {
            // Auto Report
            boolean autoReport = (boolean) newValue;
            mMainService.setAutoReportSelf(autoReport);

            suc = autoReport == mMainService.isAutoReport();

        } else if (PrefsManager.PREFS_AUTO_SEARCH.equals(key)) {
            // Auto search
            boolean autoSearch = (boolean) newValue;
            mMainService.setAutoSearchNearby(autoSearch);

            suc = autoSearch == mMainService.isAutoSearch();

        } else {
            // Nothing
        }

        //
        mView.showTip(suc, preference);

        return true;
    }
}
