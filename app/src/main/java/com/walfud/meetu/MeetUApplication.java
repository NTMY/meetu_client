package com.walfud.meetu;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.walfud.common.Version;
import com.walfud.common.WallE;
import com.walfud.libpuller.Puller;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = this;

        if (BuildConfig.DEBUG) {
            MobclickAgent.setDebugMode(true);
            Bugtags.start("b27243a172339c9df358ab036868ec05", this, Bugtags.BTGInvocationEventBubble);

            Puller.getInstance().initialize(this);
        }

        // Umeng full package update
        UmengUpdateAgent.setDeltaUpdate(false);

        WallE.initialize();

        Picasso.with(this);

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
