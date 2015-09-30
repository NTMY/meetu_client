package com.walfud.meetu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.walfud.meetu.MainService;
import com.walfud.meetu.R;
import com.walfud.meetu.manager.PrefsManager;

/**
 * Created by walfud on 9/24/15.
 */
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "SettingActivity";

    private SwitchPreference mAutoReport;
    private SwitchPreference mAutoSearch;

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        mAutoReport = (SwitchPreference) findPreference(PrefsManager.PREFS_AUTO_REPORT);
        mAutoSearch = (SwitchPreference) findPreference(PrefsManager.PREFS_AUTO_SEARCH);

        // Init state
        mAutoReport.setChecked(PrefsManager.getInstance().isAutoReport());
        mAutoSearch.setChecked(PrefsManager.getInstance().isAutoSearch());

        //
        mAutoReport.setOnPreferenceChangeListener(this);
        mAutoSearch.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        if (false) {

        } else if (PrefsManager.PREFS_AUTO_REPORT.equals(key)) {

            boolean autoReport = (boolean) newValue;
            MainService.getInstance().setAutoReportSelf(autoReport);

        } else if (PrefsManager.PREFS_AUTO_SEARCH.equals(key)) {

            boolean autoSearch = (boolean) newValue;
            MainService.getInstance().setAutoSearchNearby(autoSearch);

        } else {

        }

        return true;
    }

    // Helper
    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, SettingActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        context.startActivity(intent);
    }
}
