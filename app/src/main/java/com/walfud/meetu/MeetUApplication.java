package com.walfud.meetu;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        sApplicationContext = this;

//        Bugtags.start("b27243a172339c9df358ab036868ec05", this, Bugtags.BTGInvocationEventBubble);
        Fresco.initialize(this);
    }

    private static Context sApplicationContext;
    public static Context getContext() {
        return sApplicationContext;
    }
}
