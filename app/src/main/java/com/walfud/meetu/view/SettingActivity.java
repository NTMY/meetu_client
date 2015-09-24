package com.walfud.meetu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.walfud.meetu.R;

/**
 * Created by walfud on 9/24/15.
 */
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "SettingActivity";
    private static final String PREFS_AUTO_REPORT = "auto_report";

    private SwitchPreference mAutoReport;

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        mAutoReport = (SwitchPreference) findPreference(PREFS_AUTO_REPORT);

        //
        mAutoReport.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        if (false) {
        } else if (PREFS_AUTO_REPORT.equals(key)) {
            boolean autoReport = (boolean) newValue;

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
