package walfud.meetu;

import android.app.Application;
import android.content.Context;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {
    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationContext = this;
    }

    public static Context getContext() {
        return mApplicationContext;
    }
}
