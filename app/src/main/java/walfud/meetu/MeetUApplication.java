package walfud.meetu;

import android.app.Application;

/**
 * Created by song on 2015/6/22.
 */
public class MeetUApplication extends Application {
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
    }

    public static Application getApplication() {
        return mApplication;
    }
}
