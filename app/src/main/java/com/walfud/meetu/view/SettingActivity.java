package com.walfud.meetu.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.view.View;

import com.walfud.meetu.R;
import com.walfud.meetu.manager.PrefsManager;
import com.walfud.meetu.presenter.SettingPresenter;

/**
 * Created by walfud on 9/24/15.
 */
public class SettingActivity extends PreferenceActivity {

    public static final String TAG = "SettingActivity";

    private SettingPresenter mPresenter;
    private PrefsManager mPrefsManager;
    private EditTextPreference mNick;
    private EditTextPreference mMood;
    private SwitchPreference mAutoReport;
    private SwitchPreference mAutoSearch;

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        mNick = (EditTextPreference) findPreference(PrefsManager.PREFS_NICK);
        mMood = (EditTextPreference) findPreference(PrefsManager.PREFS_MOOD);
        mAutoReport = (SwitchPreference) findPreference(PrefsManager.PREFS_AUTO_REPORT);
        mAutoSearch = (SwitchPreference) findPreference(PrefsManager.PREFS_AUTO_SEARCH);

        // Init state
        mPrefsManager = PrefsManager.getInstance();
        mNick.setSummary(mPrefsManager.getNick());
        mMood.setSummary(mPrefsManager.getMood());
        mAutoReport.setChecked(mPrefsManager.isAutoReport());
        mAutoSearch.setChecked(mPrefsManager.isAutoSearch());

        //
        mPresenter = new SettingPresenter(this);
        mNick.setOnPreferenceChangeListener(mPresenter);
        mMood.setOnPreferenceChangeListener(mPresenter);
        mAutoReport.setOnPreferenceChangeListener(mPresenter);
        mAutoSearch.setOnPreferenceChangeListener(mPresenter);
    }

    // Function
    public void showTip(boolean success, Preference preference) {
        // Background color
        View view = getListView().getChildAt(preference.getOrder());
        view.setBackgroundColor(success ? 0x2000ff00 : 0x20ff0000);
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
