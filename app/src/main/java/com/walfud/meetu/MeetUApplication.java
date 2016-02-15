package com.walfud.meetu;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.walfud.walle.Version;
import com.walfud.walle.WallE;

import io.fabric.sdk.android.Fabric;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sApplicationContext = this;

        WallE.initialize(this);

        Stetho.initializeWithDefaults(this);

        try {
            mVersion = Version.parse(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Context sApplicationContext;
    public static Context getContext() {
        return sApplicationContext;
    }

    private static Version mVersion;
    public static Version getVersion() {
        return mVersion;
    }
}
