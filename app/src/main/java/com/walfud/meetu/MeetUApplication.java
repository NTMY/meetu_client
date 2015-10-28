package com.walfud.meetu;

import android.app.Application;
import android.content.Context;

import com.bugtags.library.Bugtags;
import com.umeng.analytics.MobclickAgent;
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
        }

        Puller.getInstance().initialize(this);

        WallE.initialize();
    }

    private static Context sApplicationContext;
    public static Context getContext() {
        return sApplicationContext;
    }
}
